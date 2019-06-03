package com.nuu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.protobuf.GeneratedMessageV3;
import com.nuu.config.AppConfig;
import com.nuu.config.FileConfig;
import com.nuu.entity.ReportData;
import com.nuu.http.DownloadListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.MiFiApplication;
import com.nuu.proto.DeviceStatus;
import com.nuu.proto.ServerResponse;
import com.nuu.proto.UpdateRequest;
import com.nuu.service.NuuService;
import com.nuu.socket.NotifyListener;
import com.nuu.socket.ProtoCommandId;
import com.nuu.socket.ReceiveListener;
import com.nuu.util.AppUtils;
import com.nuu.util.DeviceInfo;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.gosomo.proxy.IProxyCall;
import cn.gosomo.proxy.IProxyCallback;

public class MiFiManager {
    private static final String TAG = "TcpClient";

    private static final int HANDLER_THREAD_INIT_CONFIG_START = 1;
    private static final int HANDLER_REPORT_DEVICE_INFO = 2;
    private static final int HANDLER_OBTAIN_DEVICE_INFO = 3;
    private static final int HANDLER_NUU_CHECK_UPDATE = 4;

    private static MiFiManager instance;


    private enum BIND_STATUS {
        IDLE, BINDING, BINDED
    }

    private NuuService.NuuServiceBinder nuuService = null;
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
        reportDataList = new CopyOnWriteArrayList<>();
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

        Log.d(TAG, "MiFiManager init and binded:" + binded.toString());

        if (binded == BIND_STATUS.IDLE) {
            binded = BIND_STATUS.BINDING;
            bindServer(mContext);

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


    private void bindServer(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), NuuService.class);
        context.getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        bindProxyService();

        Log.v(TAG, "MiFiManager in bind server");

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

        if (mProxyCallService != null) {
            try {
                mProxyCallService.unregisterCallback(mContext.getPackageName(),
                        mProxyCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
            res = nuuService.isLogin();
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
            res = nuuService.isConnect();
        }
        return res;
    }


    /**
     * tcp 重新连接
     */
    public void reConnect() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            nuuService.reConnect();
        }
    }


    /**
     * 关闭tcp连接
     *
     * @return
     */
    public void close() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            nuuService.close();
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
                nuuService.sendProto(msg, msgType, callback);
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
                nuuService.setNotifyListener(listener);
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
     * @param msgType  命令码
     * @param callback 数据
     */
    public void sendProto(GeneratedMessageV3 msg, short msgType,
                          ReceiveListener callback) {
        if (mContext != null) {
            if (binded == BIND_STATUS.BINDED) {
                nuuService.sendProto(msg, msgType, callback);
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
                nuuService.setNotifyListener(listener);
            } else {
                waitBindingNotify(listener);
            }
        } else {
            throw new IllegalStateException("huxin sdk no init");
        }

    }

    public void clearNotifyListener(NotifyListener listener) {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            nuuService.clearNotifyListener(listener);
        }
    }


    /**
     * req socket ip and port
     * tcp login
     */
    private void socketLogin(final String uuid) {
        /*String ip = AppConfig.getSocketHost();
        int port = AppConfig.getSocketPort();
        InetSocketAddress isa = new InetSocketAddress(ip, port);
        connectTcp(uuid, isa);*/
    }


    /**
     * 用户tcp协议重登录，仅仅用于测试
     *
     * @param uuid
     * @param isa
     */
    public void connectTcp(String uuid, InetSocketAddress isa) {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            nuuService.connectTcp(uuid, isa);
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

        DeviceStatus.ReportDeviceStatusInfoReq.Builder b = DeviceStatus.ReportDeviceStatusInfoReq.newBuilder();
        b.addDeviceStatus(builder);
        DeviceStatus.ReportDeviceStatusInfoReq msg = b.build();

        sendProto(msg, msgType, callback);
    }


    /**
     * @param callback
     */
    public void deviceStatus(List<ReportData> list, ReceiveListener callback) {
        if (list == null || list.size() == 0) {
            return;
        }

        short msgType = ProtoCommandId.reportDeviceStatusMsgType();
        DeviceStatus.ReportDeviceStatusInfoReq.Builder reqBuilder = DeviceStatus.ReportDeviceStatusInfoReq.newBuilder();

        for (ReportData item : list) {
            DeviceStatus.ReportDeviceStatusReq.Builder builder = DeviceStatus.ReportDeviceStatusReq.newBuilder();
            builder.setDeviceId(item.getDeviceId());//set device id
            builder.setMac(item.getMac());//设置mac
            builder.setIp(item.getIp());//设置ip
            builder.setDeviceStatus(item.getNetStatus());
            builder.setUtc(item.getUnixTime());

            ReportData.Sim1Bean sim1Bean = item.getSim1();
            if (sim1Bean != null) {
                DeviceStatus.SimCardSlot.Builder b = DeviceStatus.SimCardSlot.newBuilder();
                b.setCi(sim1Bean.getCi());
                b.setImsi(sim1Bean.getImsi());
                b.setLac(sim1Bean.getLac());
                try {
                    int plmn = Integer.parseInt(sim1Bean.getPlmn());
                    b.setPlmn(plmn);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                b.setPsc(sim1Bean.getPsc());
                b.setSignal(sim1Bean.getSignal());

                int netMode = sim1Bean.getNetMode();
                b.setMode(DeviceStatus.NetworkMode.forNumber(netMode));
                builder.setSlot1(b);
            }

            ReportData.Sim2Bean sim2Bean = item.getSim2();
            if (sim2Bean != null) {
                DeviceStatus.SimCardSlot.Builder b = DeviceStatus.SimCardSlot.newBuilder();
                b.setCi(sim2Bean.getCi());
                b.setImsi(sim2Bean.getImsi());
                b.setLac(sim2Bean.getLac());
                try {
                    b.setPlmn(Integer.parseInt(sim2Bean.getPlmn()));
                } catch (NumberFormatException e) {
                    b.setPlmn(0);
                    e.printStackTrace();
                }
                b.setPsc(sim2Bean.getPsc());
                b.setSignal(sim2Bean.getSignal());

                int netMode = sim2Bean.getNetMode();
                b.setMode(DeviceStatus.NetworkMode.forNumber(netMode));
                builder.setSlot1(b);
            }
            reqBuilder.addDeviceStatus(builder);
        }

        DeviceStatus.ReportDeviceStatusInfoReq msg = reqBuilder.build();
        sendProto(msg, msgType, callback);
    }

    /**
     * bind service callback
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof NuuService.NuuServiceBinder) {
                nuuService = (NuuService.NuuServiceBinder) service;
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
            nuuService = null;
            binded = BIND_STATUS.IDLE;
            for (InitListener item : mInitListenerList) {
                item.fail();
            }
            mInitListenerList.clear();
            Log.e(TAG, "Service Failed...");
        }
    };


    private IProxyCall mProxyCallService;
    private ReportData.Sim2Bean curSim2 = new ReportData.Sim2Bean();

    private final static String PROXY_SERVICE = "cn.gosomo.proxy.ProxyService";
    private final static String PROXY_SERVICE_PACKAGE = "cn.gosomo.proxy";


    public ReportData.Sim2Bean getCurSim2() {
        return curSim2;
    }

    private void bindProxyService() {
        Intent intent = new Intent(PROXY_SERVICE);
        intent.setClassName(PROXY_SERVICE_PACKAGE, PROXY_SERVICE);
        mContext.bindService(intent, mProxyCallConn, Context.BIND_AUTO_CREATE);
    }


    private ServiceConnection mProxyCallConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "ServiceConnection onServiceDisconnected");
            mProxyCallService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "ServiceConnection onServiceConnected");
            mProxyCallService = IProxyCall.Stub.asInterface(service);
            try {
                String packageName = mContext.getPackageName();
                //注册了回调
                mProxyCallService.registerCallback(packageName, mProxyCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private IProxyCallback mProxyCallback = new IProxyCallback.Stub() {
        @Override
        public void onEventSys(int board, int event, int value) {
            Log.d(TAG, "onEventSys board:" + board + "@event:" + event + "@value:" + value);
        }

        @Override
        public void onSimcardStateChange(int board, int slot, String imsi) throws RemoteException {
            Log.d(TAG, "onSimcardStateChange board:" + board + "@slot:" + slot + "@imsi:" + imsi);
            if (board == 2 && slot == 0) {
                String tempImsi = imsi == null ? "" : imsi;
                curSim2.setImsi(tempImsi);
            }

        }

        @Override
        public void onLocationChange(int board, int slot, String plmn, int lac, int cid, int psc) throws RemoteException {
            Log.d(TAG, "onLocationChange board:" + board + "@slot:" + slot + "@plmn:" + plmn + "@lac:" + lac + "@cid:" + cid + "@psc:" + psc);
            if (board == 2 && slot == 0) {
                String tempPlmn = plmn == null ? "" : plmn;
                curSim2.setPlmn(tempPlmn);
                curSim2.setCi(cid);
                curSim2.setLac(lac);
                curSim2.setPsc(psc);
            }
        }

        @Override
        public void onServiceStateChange(int board, int slot, int serviceState, int networkType, int rssi) throws RemoteException {
            Log.d(TAG, "onServiceStateChange board:" + board + "@slot:" + slot + "@serviceState:" + serviceState + "@networkType:" + networkType + "@rssi:" + rssi);
            if (board == 2 && slot == 0) {
                curSim2.setSignal(rssi);
                curSim2.setNetMode(networkType);
            }

        }

        @Override
        public void onDataStateChange(int board, int slot, int dataState, int networkType, int rssi) throws RemoteException {
            Log.d(TAG, "onDataStateChange board:" + board + "@slot:" + slot + "@dataState:" + dataState + "@networkType:" + networkType + "@rssi:" + rssi);
            if (board == 2 && slot == 0) {
                curSim2.setNetMode(networkType);
                curSim2.setSignal(rssi);
            }
        }

        @Override
        public void onSignalStrengthChange(int board, int slot, int rssi) throws RemoteException {
            Log.d(TAG, "onSignalStrengthChange board:" + board + "@slot:" + slot + "@rssi:" + rssi);
            if (board == 2 && slot == 0) {
                curSim2.setSignal(rssi);
            }
        }


    };


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
                    FileConfig.delLogFileMonthBefore();  //删除一定时间前的设备日志信息
                    break;
                case HANDLER_REPORT_DEVICE_INFO:
                    sendDeviceInfoList();
                    break;
                case HANDLER_OBTAIN_DEVICE_INFO:
                    initDeviceInfo();
                    break;
                case HANDLER_NUU_CHECK_UPDATE:
                    checkUpdate();
                    break;
                default:
                    break;
            }

        }

    }

    /////////////////////////////////////////////

    public interface OnDeviceInfo {
        void onSuccess(String json);
    }

    private OnDeviceInfo mOnDeviceInfo;

    private List<ReportData> reportDataList;

    public String getDeviceInfo() {
        ReportData data = new ReportData(mContext);
        return data.toJson();
    }

    public void nuuCheckUpdate() {
        mProcessHandler.sendEmptyMessageDelayed(HANDLER_NUU_CHECK_UPDATE, 30 * 1000);
    }


    public void obtainDeviceInfo(OnDeviceInfo callback) {
        this.mOnDeviceInfo = callback;
        mProcessHandler.sendEmptyMessage(HANDLER_OBTAIN_DEVICE_INFO);
    }


    public void reportDeviceInfo() {
        mProcessHandler.sendEmptyMessage(HANDLER_REPORT_DEVICE_INFO);
    }


    private void initDeviceInfo() {
        ReportData data = new ReportData(mContext);
        reportDataList.add(data);

        if (mOnDeviceInfo != null) {
            mOnDeviceInfo.onSuccess(data.toString());
        }
    }

    private void sendDeviceInfo() {
        ReportData data = new ReportData(mContext);
        String devId = data.getDeviceId();
        int status = data.getNetStatus();
        int utc = data.getUnixTime();
        String ip = data.getIp();
        String mac = data.getMac();
        DeviceStatus.SimCardSlot sim1 = DeviceInfo.getProtoSim1(mContext);
        ReceiveListener callback = new ReceiveListener() {
            @Override
            public void OnRec(byte[] body) {
                try {
                    final ServerResponse.ReportDeviceStatusInfoResp ack = ServerResponse.
                            ReportDeviceStatusInfoResp.parseFrom(body);
                    String test = ack.getDeviceId();
                    int test2 = ack.getUtc();
                    Log.d(TAG, "sendDeviceInfo:" + test + "@" + test2);
                } catch (ExceptionInInitializerError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (NoClassDefFoundError e) {
                    e.printStackTrace();
                }
            }
        };
        deviceStatus(devId, status, utc, ip, mac,
                sim1, null, callback);

        FileConfig.writeFile(data);
    }

    private void sendDeviceInfoList() {
        FileConfig.writeFile(mContext, reportDataList);
        ReceiveListener callback = new ReceiveListener() {
            @Override
            public void OnRec(byte[] body) {
                try {
                    final ServerResponse.ReportDeviceStatusInfoResp ack = ServerResponse.
                            ReportDeviceStatusInfoResp.parseFrom(body);
                    reportDataList.clear();
                    String deviceId = ack.getDeviceId();
                    int utc = ack.getUtc();
                    Log.d(TAG, "sendDeviceInfo:" + deviceId + "@" + utc);
                } catch (ExceptionInInitializerError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (NoClassDefFoundError e) {
                    e.printStackTrace();
                }
            }
        };
        deviceStatus(reportDataList, callback);
    }

    public void checkUpdate() {
        Log.d(TAG, "nuu info check update");
        String devId = DeviceInfo.getDeviceId();
        String curVerCode = String.valueOf(AppUtils.getVerCode(mContext));
        String brand = DeviceInfo.getBrand();
        String model = DeviceInfo.getModel();

        ReceiveListener callback = new ReceiveListener() {
            @Override
            public void OnRec(byte[] body) {
                try {
                    final UpdateRequest.DeviceUpgradeResp ack = UpdateRequest.DeviceUpgradeResp.parseFrom(body);
                    String newVerCode = ack.getNewVerCode();
                    String url = ack.getUrl();
                    boolean result = ack.getResult();
                    Log.d(TAG, result + "@" + newVerCode + "@" + url);

                    if (result && !TextUtils.isEmpty(url)) {
                        String deviceId = DeviceInfo.getDeviceId();

                        String token = AppUtils.md5("@com.nuu@" + deviceId);
                        String reqUrl = url + "?hwid=" + deviceId + "&vercode=" + newVerCode + "&token=" + token;

                        final String filePath = FileConfig.getApkDownLoadPath();
                        String fileName = AppConfig.getDownloadApkName();
                        OkHttpConnector.httpDownload(reqUrl, null,
                                filePath, fileName, new DownloadListener() {
                                    @Override
                                    public void onProgress(int cur, int total) {
                                        int rate = (cur * 100) / total;
                                        Log.d(TAG, "onProgress:" + rate + "%");
                                    }

                                    @Override
                                    public void onFail(String err) {
                                        Log.e(TAG, "onFail");
                                        File file = new File(filePath);
                                        if (file.exists()) {
                                            file.deleteOnExit();
                                        }
                                    }

                                    @Override
                                    public void onSuccess(String path) {
                                        Log.d(TAG, "onSuccess");
                                    }
                                });

                    }

                } catch (ExceptionInInitializerError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (NoClassDefFoundError e) {
                    e.printStackTrace();
                }
            }
        };
        checkUpdate(devId, curVerCode, brand, model, callback);
    }
}
