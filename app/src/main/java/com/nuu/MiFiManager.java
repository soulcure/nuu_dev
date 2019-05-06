package com.nuu;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.protobuf.GeneratedMessageV3;
import com.nuu.mifi.MiFiApplication;
import com.nuu.config.AppConfig;
import com.nuu.proto.DeviceStatus;
import com.nuu.proto.UpdateRequest;
import com.nuu.service.HuxinService;
import com.nuu.socket.NotifyListener;
import com.nuu.socket.ProtoCommandId;
import com.nuu.socket.ReceiveListener;
import com.nuu.util.AppUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MiFiManager {
    private static final String TAG = "TcpClient";

    private static final int HANDLER_THREAD_INIT_CONFIG_START = 1;
    private static final int HANDLER_THREAD_AUTO_LOGIN = 2;

    private static MiFiManager instance;


    private enum BIND_STATUS {
        IDLE, BINDING, BINDED
    }

    private HuxinService.HuxinServiceBinder huxinService = null;
    private BIND_STATUS binded = BIND_STATUS.IDLE;

    private Context mContext;
    private MiFiApplication app;

    private List<InitListener> mInitListenerList;

    private ProcessHandler mProcessHandler;


    /**
     * SDK初始化结果监听器
     */
    public interface InitListener {
        void success();

        void fail();
    }


    /**
     * 私有构造函数
     */
    private MiFiManager() {
        mInitListenerList = new ArrayList<>();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 获取呼信sdk单例索引
     *
     * @return
     */
    public static MiFiManager instance() {
        if (instance == null) {
            instance = new MiFiManager();
        }
        return instance;
    }


    /**
     * 呼信sdk初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.init(context, null);
    }


    /**
     * 呼信sdk初始化
     *
     * @param context
     */
    public void init(final Context context, InitListener listener) {
        app = (MiFiApplication) context.getApplicationContext();
        mContext = context.getApplicationContext();

        String processName = AppUtils.getProcessName(context, android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals(context.getPackageName());
            if (!defaultProcess) {
                return;
            }
        }

        if (listener != null) {
            mInitListenerList.add(listener);
        }

        if (binded == BIND_STATUS.IDLE) {
            binded = BIND_STATUS.BINDING;
            initHandler();

            mProcessHandler.sendEmptyMessage(HANDLER_THREAD_INIT_CONFIG_START);

        } else if (binded == BIND_STATUS.BINDING) {

            //do nothing

        } else if (binded == BIND_STATUS.BINDED) {
            for (InitListener item : mInitListenerList) {
                item.success();
            }
            mInitListenerList.clear();
        }
    }


    private void initWork(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), HuxinService.class);
        context.getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        Log.v(TAG, "MiFiManager in init");

    }


    /**
     * 呼信sdk销毁
     *
     * @param
     */
    public void destroy() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            binded = BIND_STATUS.IDLE;
            mContext.getApplicationContext().unbindService(serviceConnection);
        }

    }


    public void loginOut() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            clearUserData();
        }
    }

    /**
     * 判断SDK是否登录
     *
     * @return
     */
    public boolean isLogin() {
        boolean res = false;
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            res = huxinService.isLogin();
        }
        return res;
    }


    private void initAppForMainProcess(Context context) {
        String processName = AppUtils.getProcessName(context, android.os.Process.myPid());
        Log.e("colin", "processName:" + processName);
        if (processName != null) {
            boolean defaultProcess = processName.equals(context.getPackageName());
            if (defaultProcess) {
                MiFiManager.instance().init(context);
            }
        }
    }


    /**
     * 判断SDK服务是否已经绑定成功
     *
     * @return
     */
    public boolean isBinded() {
        boolean res = false;
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            res = true;
        }
        return res;
    }


    /**
     * 判断tcp是否连接成功
     *
     * @return
     */
    public boolean isConnect() {
        boolean res = false;
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            res = huxinService.isConnect();
        }
        return res;
    }


    /**
     * tcp 重新连接
     */
    public void reConnect() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.reConnect();
        }
    }


    /**
     * 关闭tcp连接
     *
     * @return
     */
    public void close() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.close();
        }
    }

    private void clearUserData() {
        close();
    }


    private void waitBindingProto(final GeneratedMessageV3 msg, final short msgType,
                                  final ReceiveListener callback) {
        init(mContext, new InitListener() {
            @Override
            public void success() {
                huxinService.sendProto(msg, msgType, callback);
            }


            @Override
            public void fail() {
                String log = "bind server fail!";
            }
        });
    }


    private void waitBindingNotify(final NotifyListener listener) {
        init(mContext, new InitListener() {
            @Override
            public void success() {
                huxinService.setNotifyListener(listener);
            }

            @Override
            public void fail() {
                Log.e(TAG, "bind server fail!");
            }
        });
    }


    /**
     * 发送socket协议
     *
     * @param msgType 命令码
     * @param callback  数据
     */
    public void sendProto(GeneratedMessageV3 msg, short msgType,
                          ReceiveListener callback) {
        if (mContext != null) {
            if (binded == BIND_STATUS.BINDED) {
                huxinService.sendProto(msg, msgType, callback);
            } else {
                waitBindingProto(msg, msgType, callback);
            }
        } else {
            throw new IllegalStateException("huxin sdk no init");
        }

    }


    public void setNotifyListener(NotifyListener listener) {
        if (mContext != null) {
            if (binded == BIND_STATUS.BINDED) {
                huxinService.setNotifyListener(listener);
            } else {
                waitBindingNotify(listener);
            }
        } else {
            throw new IllegalStateException("huxin sdk no init");
        }

    }

    public void clearNotifyListener(NotifyListener listener) {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.clearNotifyListener(listener);
        }
    }


    /**
     * req socket ip and port
     * tcp login
     */
    private void socketLogin(final String uuid) {
        String ip = AppConfig.getSocketHost();
        int port = AppConfig.getSocketPort();
        InetSocketAddress isa = new InetSocketAddress(ip, port);
        connectTcp(uuid, isa);
    }


    /**
     * 用户tcp协议重登录，仅仅用于测试
     *
     * @param uuid
     * @param isa
     */
    public void connectTcp(String uuid, InetSocketAddress isa) {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.connectTcp(uuid, isa);
        }
    }

    /**
     * @param callback
     */
    public void checkUpdate(String devId, String curVerCode, String brand,
                            String model, ReceiveListener callback) {
        short msgType = ProtoCommandId.requestDeviceUpgrade();

        UpdateRequest.DeviceUpgradeReq.Builder builder = UpdateRequest.DeviceUpgradeReq.newBuilder();
        builder.setDevId(devId);
        builder.setCurVerCode(curVerCode);
        builder.setBrand(brand);
        builder.setModel(model);
        UpdateRequest.DeviceUpgradeReq msg = builder.build();

        sendProto(msg, msgType, callback);
    }

    /**
     * @param callback
     */
    public void deviceStatus(String devId, int status, int utc, String ip, String mac,
                             DeviceStatus.SimCardSlot sim1, DeviceStatus.SimCardSlot sim2,
                             ReceiveListener callback) {
        short msgType = ProtoCommandId.reportDeviceStatusMsgType();

        DeviceStatus.ReportDeviceStatusReq.Builder builder = DeviceStatus.ReportDeviceStatusReq.newBuilder();
        builder.setDeviceId(devId);//set device id
        builder.setMac(mac);//设置mac
        builder.setIp(ip);//设置ip
        builder.setDeviceStatus(status);
        builder.setUtc(utc);
        builder.setSlot1(sim1);

        if (sim2 != null) {
            builder.setSlot2(sim2);
        }

        DeviceStatus.ReportDeviceStatusReq msg = builder.build();

        sendProto(msg, msgType, callback);
    }


    /**
     * bind service callback
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof HuxinService.HuxinServiceBinder) {
                huxinService = (HuxinService.HuxinServiceBinder) service;
                binded = BIND_STATUS.BINDED;
                for (InitListener item : mInitListenerList) {
                    item.success();
                }
                mInitListenerList.clear();
                Log.v(TAG, "Service Connected...");
            }
        }

        // 连接服务失败后，该方法被调用
        @Override
        public void onServiceDisconnected(ComponentName name) {
            huxinService = null;
            binded = BIND_STATUS.IDLE;
            for (InitListener item : mInitListenerList) {
                item.fail();
            }
            mInitListenerList.clear();
            Log.e(TAG, "Service Failed...");
        }
    };

    private void autoLogin() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            mProcessHandler.sendEmptyMessage(HANDLER_THREAD_AUTO_LOGIN);
        }
    }


    /**
     * 线程初始化
     */
    private void initHandler() {
        if (mProcessHandler == null) {
            HandlerThread handlerThread = new HandlerThread(
                    "handler looper Thread");
            handlerThread.start();
            mProcessHandler = new ProcessHandler(handlerThread.getLooper());
        }
    }

    /**
     * 子线程handler,looper
     *
     * @author Administrator
     */
    private class ProcessHandler extends Handler {

        public ProcessHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_THREAD_INIT_CONFIG_START:
                    initWork(mContext);
                    break;
                case HANDLER_THREAD_AUTO_LOGIN:
                    break;
                default:
                    break;
            }

        }

    }


}
