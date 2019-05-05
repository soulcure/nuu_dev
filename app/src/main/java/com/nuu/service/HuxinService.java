package com.nuu.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.google.protobuf.GeneratedMessageV3;
import com.nuu.config.AppConfig;
import com.nuu.socket.NotifyListener;
import com.nuu.socket.ReceiveListener;
import com.nuu.socket.TcpClient;
import com.nuu.util.AppUtils;

import java.net.InetSocketAddress;


public class HuxinService extends Service {

    private static final String TAG = HuxinService.class.getSimpleName();

    public static final String BOOT_SERVICE = "com.youmai.huxin.service.BOOT_SERVICE"; //启动服务
    public static final String IM_LOGIN_OUT = "com.youmai.huxin.service.IM_LOGIN_OUT";  //im login out

    private Context mContext;

    /**
     * socket client
     */
    private TcpClient mClient;


    private ServiceHandler mServiceHandler;

    private NetWorkChangeReceiver mNetWorkReceiver;
    private BroadcastReceiver mScreenReceiver;

    /**
     * Activity绑定后回调
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "service onBind...");
        return new HuxinServiceBinder();
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /*case HX_ALL_CONFIG:
                    break;
                case HX_ALL_SHOW:
                    break;
                case HX_ALL_CONT:
                    break;*/
            }
        }
    }


    /**
     * activity和service通信接口
     */
    public class HuxinServiceBinder extends Binder {

        /**
         * 发送socket协议
         *
         * @param msg       消息体
         * @param commandId 命令码
         * @param callback  回调
         */
        public void sendProto(GeneratedMessageV3 msg, short commandId,
                              short rspId, ReceiveListener callback) {
            mClient.sendProto(msg, commandId, rspId, callback);
        }

        public void setNotifyListener(NotifyListener listener) {
            mClient.setNotifyListener(listener);
        }

        public void clearNotifyListener(NotifyListener listener) {
            mClient.clearNotifyListener(listener);
        }


        public boolean isLogin() {
            return mClient.isLogin();
        }

        public boolean isConnect() {
            return mClient.isConnect();
        }

        public void reConnect() {
            mClient.reConnect();
        }

        public void close() {
            mClient.close();
            mClient.setCallBack(null);
        }


        public void connectTcp(final String uuid, InetSocketAddress isa) {
            if (mClient == null) {
                return;
            }

            if (mClient.isConnect() && mClient.isLogin()) {
                return;
            }

            mClient.close();
            mClient.setRemoteAddress(isa);

            TcpClient.IClientListener callback = new TcpClient.IClientListener() {
                @Override
                public void connectSuccess() {
                    tcpLogin();
                }
            };
            mClient.connect(callback);
        }

    }


    /**
     * wifi监测广播.
     */
    private class NetWorkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (AppUtils.isWifi(context)) {
                    /*Message msg0 = mServiceHandler.obtainMessage(HX_ALL_CONFIG);
                    mServiceHandler.sendMessageDelayed(msg0, 1000 * 60);

                    Message msg1 = mServiceHandler.obtainMessage(HX_ALL_SHOW);
                    mServiceHandler.sendMessageDelayed(msg1, 1000 * 60 * 2);

                    Message msg2 = mServiceHandler.obtainMessage(HX_ALL_CONT);
                    mServiceHandler.sendMessageDelayed(msg2, 1000 * 60 * 3);*/

                }
            }
        }
    }


    /**
     * 屏幕on/off监听
     */
    public class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // DO WHATEVER YOU NEED TO DO HERE
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // AND DO WHATEVER YOU NEED TO DO HERE
                /*if (mClient != null) {
                    mClient.reConnect();
                }*/
            }
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();//this
        mClient = new TcpClient(this);

        HandlerThread thread = new HandlerThread("IntentService");
        thread.start();
        Looper looper = thread.getLooper();
        mServiceHandler = new ServiceHandler(looper);

        IntentFilter netFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetWorkReceiver = new NetWorkChangeReceiver();
        registerReceiver(mNetWorkReceiver, netFilter);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenReceiver = new ScreenReceiver();
        registerReceiver(mScreenReceiver, filter);

        createTcp();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String action = null;

        if (intent != null) {
            action = intent.getAction();
        }

        if (!TextUtils.isEmpty(action)) {
            switch (action) {
                case BOOT_SERVICE:
                    if (mClient.isIdle()) {
                        Log.v(TAG, "tcp is reconnect");
                        mClient.reConnect();
                    } else if (mClient.isConnect()) {
                        if (!mClient.isLogin()) {  //登录后的重连，不需要二次登录，服务器做支持
                            tcpLogin();
                        }
                    }
                    break;
                case IM_LOGIN_OUT:
                    if (mClient != null && mClient.isConnect() && mClient.isLogin()) {
                        mClient.close();
                    }
                    break;
            }
        }

        // 系统就会重新创建这个服务并且调用onStartCommand()方法
        return START_STICKY;
    }


    @Override
    public void onTrimMemory(int level) {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClient.close();
        mClient = null;

        unregisterReceiver(mNetWorkReceiver);
        unregisterReceiver(mScreenReceiver);

        Log.v(TAG, "HuXinService is onDestroy");
    }


    private void createTcp() {
        String ip = AppConfig.getSocketHost();
        int port = AppConfig.getSocketPort();

        if (!mClient.isConnect()) {
            InetSocketAddress isa = new InetSocketAddress(ip, port);
            mClient.setRemoteAddress(isa);
            TcpClient.IClientListener callback = new TcpClient.IClientListener() {
                @Override
                public void connectSuccess() {
                    tcpLogin();
                }
            };
            mClient.connect(callback);
        }

    }

    /**
     * 发送登录IM服务器请求
     */
    private void tcpLogin() {
        /*MiFiManager.instance().chargerAuth(new ReceiveListener() {
            @Override
            public void OnRec(ByteBuffer buffer) {
                mClient.setLogin(true);
                short test1 = buffer.getShort();  //预留
                short test2 = buffer.getShort();  //预留
                int matchCode = buffer.getInt();  //报文随机数应答
                byte loginAuth = buffer.get();  //登入验证
                byte encryption = buffer.get();  //加密标志
                byte[] rsaPublic = new byte[128];//RSA 公共模数
                buffer.get(rsaPublic);
                int rsaSecret = buffer.getInt();
            }
        });*/
    }
}
