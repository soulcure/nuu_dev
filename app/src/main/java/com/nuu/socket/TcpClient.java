package com.nuu.socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.protobuf.GeneratedMessageV3;
import com.nuu.util.AppUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TcpClient extends PduUtil implements Runnable {

    private static final String TAG = TcpClient.class.getSimpleName();

    private static final int TCP_RE_CONNECT = 0;

    //tcp 连接状态
    private enum TCP_STATUS {
        IDLE, CONNECTING, CONNECTED
    }

    private static final int SOCKET_BUFFER_SIZE = 500 * 1024; //500KB
    private static final int HEART_BEAT_INTERVAL = 15;//心跳间隔单位为秒
    private static final int MAX_HEARTBEAT = 1;  //最大的心跳丢失次数

    private ScheduledExecutorService heartBeatScheduled;

    private volatile TCP_STATUS tcpStatus = TCP_STATUS.IDLE;

    private boolean isLogin = false;

    private int heartBeatCount = 0;

    private final Context mContext;
    private final Handler mHandler;

    private SocketChannel socketChannel;
    private InetSocketAddress mRemoteAddress;

    private IClientListener mCallBack;  //socket连接成功回调
    /**
     * socket网络发送线程对象
     **/
    private TcpSendThread mSender;

    /**
     * 发送队列
     */
    private final LinkedBlockingQueue<ByteBuffer> mSendQueue;
    /**
     * 接收buffer
     */
    private final ByteBuffer receiveBuffer;


    /**
     * 协议监听
     */
    private final List<NotifyListener> mNotifyListener;

    /**
     * 协议消息派发
     */
    private final ConcurrentHashMap<Short, ReceiveListener> mCommonListener;

    /**
     * socket连接成功回调
     */
    public interface IClientListener {
        void connectSuccess();
    }


    public TcpClient(Context context) {
        mContext = context;
        mHandler = new TcpHandler(this);
        mNotifyListener = new ArrayList<>();
        mCommonListener = new ConcurrentHashMap<>();

        mSendQueue = new LinkedBlockingQueue<>();
        receiveBuffer = ByteBuffer.allocate(SOCKET_BUFFER_SIZE);
        Log.v(TAG, "new TcpClient() be called");
    }

    /**
     * tcp 管理类
     *
     * @param remoteAddress 地址
     */
    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        mRemoteAddress = remoteAddress;
    }

    public void setCallBack(IClientListener callBack) {
        mCallBack = callBack;
    }

    /**
     * 连接socket
     */
    private void connect() {
        connect(null);
    }

    /**
     * 连接socket
     *
     * @param callback 连接成功回调
     */
    public void connect(IClientListener callback) {
        if (mContext != null
                && mRemoteAddress != null
                && AppUtils.isNetworkConnected(mContext)) {
            if (tcpStatus == TCP_STATUS.IDLE) {
                if (callback != null) {
                    mCallBack = callback;
                }
                Thread thread = new Thread(this);
                thread.setName("socket thread");
                thread.start();
                Log.v(TAG, "tcp is connecting");
            }
        } else {
            Log.e(TAG, "mobile network not connected or not init");
        }
    }


    /**
     * 发送协议接口
     *
     * @param msg      消息体
     * @param callback 回调
     */
    public synchronized void sendProto(GeneratedMessageV3 msg, short msgType,
                                       ReceiveListener callback) {
        PduBase pduBase = new PduBase();

        pduBase.msgType = msgType;
        pduBase.length = (short) msg.getSerializedSize();
        pduBase.body = msg.toByteArray();

        short key = (short) (msgType & 0x00FF);

        Log.d(TAG, "length:" + pduBase.length);
        if (callback != null) {
            mCommonListener.put(key, callback);
        }
        sendPdu(pduBase);
    }


    public void setNotifyListener(NotifyListener listener) {
        for (int i = 0; i < mNotifyListener.size(); i++) {
            NotifyListener item = mNotifyListener.get(i);
            if (item.getCommandId() == listener.getCommandId()) {
                mNotifyListener.set(i, listener);
                return;
            }
        }
        mNotifyListener.add(listener);
    }

    public void clearNotifyListener(NotifyListener listener) {
        mNotifyListener.remove(listener);
    }

    /**
     * 关闭socket
     */
    public void close() {
        heartBeatCount = 0;
        tcpStatus = TCP_STATUS.IDLE;
        isLogin = false;
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
            if (mSender != null) {
                mSender.close();
            }
            if (heartBeatScheduled != null
                    && !heartBeatScheduled.isShutdown()) {
                heartBeatScheduled.shutdown();
            }

            mSendQueue.clear();

            if (mHandler.hasMessages(TCP_RE_CONNECT)) {
                mHandler.removeMessages(TCP_RE_CONNECT);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socketChannel = null;
            mSender = null;
            heartBeatScheduled = null;
        }
        Log.e(TAG, "tcp is closed");
    }


    /**
     * Socket连接是否是正常的
     *
     * @return 是否连接
     */
    public boolean isConnect() {
        return tcpStatus == TCP_STATUS.CONNECTED && socketChannel != null && socketChannel.isConnected();
    }

    public boolean isIdle() {
        return tcpStatus == TCP_STATUS.IDLE;
    }

    /**
     * 关闭socket 重新连接
     */
    public void reConnect() {
        close();
        connect();
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        if (!login) {
            close();
        }
        isLogin = login;
    }

    private synchronized void sendPdu(PduBase pduBase) {

        ByteBuffer buffer = serializePdu(pduBase);
        if (mSender != null
                && isConnect()) {
            mSender.send(buffer);
        } else {
            Log.e(TAG, "tcp is not connect");
        }
    }


    @Override
    public void OnRec(final PduBase pduBase) {
        final byte ver = (byte) ((pduBase.msgType >> 12) & 0x000F);
        final byte cate = (byte) ((pduBase.msgType >> 8) & 0x000F);
        final short key = (short) (pduBase.msgType & 0x00FF);

        String log = "tcp rec ver:" + ver + "& tcp rec cate:" + cate + "& tcp rec commandId:" + key;
        Log.d(TAG, log);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ReceiveListener callback = mCommonListener.get(key);

                if (callback != null) {
                    callback.OnRec(pduBase.body);
                    mCommonListener.remove(key);
                } else {
                    OnCallback(pduBase);
                }
            }
        });


    }


    @Override
    public void OnCallback(PduBase pduBase) {
        for (NotifyListener item : mNotifyListener) {
            if (item.getCommandId() == pduBase.msgType) {
                item.OnRec(pduBase.body);
                break;
            }
        }
    }


    @Override
    public void run() {
        try {
            socketConnect();
            tcpReceive();
        } catch (IOException e) {
            Log.e(TAG, "TcpClient error " + e.toString());
        }
    }


    /**
     * 开始心跳
     */
    public void startHeartBeat() {
        heartBeatScheduled = Executors.newScheduledThreadPool(1);
        heartBeatScheduled.scheduleAtFixedRate(new Runnable() {
            public void run() {
                heatBeat();
            }
        }, HEART_BEAT_INTERVAL, HEART_BEAT_INTERVAL, TimeUnit.SECONDS);
    }


    /**
     * 心跳协议请求
     */
    private void heatBeat() {
        ++heartBeatCount;
        if (heartBeatCount > MAX_HEARTBEAT) {
            reConnect();
        }


    }


    /**
     * 连接socket
     *
     * @throws IOException
     */
    private void socketConnect() {
        tcpStatus = TCP_STATUS.CONNECTING;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.socket().setSendBufferSize(SOCKET_BUFFER_SIZE);
            socketChannel.socket().setReceiveBufferSize(SOCKET_BUFFER_SIZE);
            socketChannel.socket().setKeepAlive(true);
            //socketChannel.socket().setReuseAddress(false);
            socketChannel.socket().setSoLinger(false, 0);
            //socketChannel.socket().setSoTimeout(0);  //超时5秒
            //socketChannel.socket().setTcpNoDelay(true);
            socketChannel.connect(mRemoteAddress);

            while (!socketChannel.finishConnect()) {  //非阻塞模式,必需设置
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e(TAG, "socket connect" + e.toString());
                }
            }
        } catch (Exception e) {
            tcpStatus = TCP_STATUS.IDLE;
            Log.e(TAG, "socketConnect exception" + e.toString());
        }

        if (socketChannel.isConnected()) {
            mSender = new TcpSendThread();
            mSender.start();    //开启发送线程

            tcpStatus = TCP_STATUS.CONNECTED;

            if (mCallBack != null) {
                mCallBack.connectSuccess();//socket连接成功回调
            }

            Log.v(TAG, "tcp is connect success");
        }
    }

    /**
     * socket receive
     *
     * @throws IOException
     */

    private void tcpReceive() throws IOException {
        try {
            Log.v(TAG, "tcp is Blocking model read buffer");
            receiveBuffer.clear();
            while (socketChannel != null && socketChannel.isConnected()
                    && (socketChannel.read(receiveBuffer)) > 0) {
                receiveBuffer.flip();
                Log.v(TAG, "tcp read buffer");
                while (ParsePdu(receiveBuffer) > 0) {
                    Log.v(TAG, "read while loop");
                }
            }
        } catch (AsynchronousCloseException e) {
            Log.e(TAG, "tcpReceive exception" + e.toString());
        }

    }


    private void reconnect() {
        if (!mHandler.hasMessages(TCP_RE_CONNECT)) {
            Message msg = mHandler.obtainMessage(TCP_RE_CONNECT);
            mHandler.sendMessageDelayed(msg, 2000);
        }
    }

    /**
     * socket 发送线程类
     */
    private class TcpSendThread implements Runnable {
        boolean isExit = false;  //是否退出

        /**
         * 发送线程开启
         */
        public void start() {
            Thread thread = new Thread(this);
            thread.setName("tcpSend-thread");
            thread.start();
        }

        public void send(ByteBuffer buffer) {
            synchronized (this) {
                if (buffer != null) {
                    mSendQueue.offer(buffer);
                    notify();
                }
            }

        }


        /**
         * 发送线程关闭
         */
        public void close() {
            synchronized (this) { // 激活线程
                isExit = true;
                notify();
            }
        }

        @Override
        public void run() {
            while (!isExit) {
                Log.v(TAG, "tcpSend-thread is running");

                synchronized (mSendQueue) {
                    while (!mSendQueue.isEmpty()
                            && socketChannel != null
                            && socketChannel.isConnected()) {
                        ByteBuffer buffer = mSendQueue.poll();
                        if (buffer == null) {
                            continue;
                        }
                        buffer.flip();

                        if (buffer.remaining() > 0) {
                            int count;
                            try {
                                while (buffer.hasRemaining() && (count = socketChannel.write(buffer)) > 0) {
                                    Log.v(TAG, "tcp send buffer success and count:" + count);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "tcp send buffer error " + e.toString());
                            } finally {
                                buffer.clear();
                            }
                        }

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "tcp error " + e.toString());
                        }

                    }//#while
                }

                synchronized (this) {
                    try {
                        wait();// 发送完消息后，线程进入等待状态
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }//#run


    }//# TcpSendThread


    /**
     * service handler
     */
    public static class TcpHandler extends Handler {
        private final WeakReference<TcpClient> mTarget;

        TcpHandler(TcpClient target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            final TcpClient tcpClient = mTarget.get();
            switch (msg.what) {
                case TCP_RE_CONNECT:
                    if (tcpClient != null) {
                        Log.v(TAG, "tcp is reconnect");
                        if (tcpClient.isIdle()
                                && AppUtils.isNetworkConnected(tcpClient.mContext)) {
                            tcpClient.reConnect();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


}