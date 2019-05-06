package com.nuu.mifi;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nuu.MiFiManager;
import com.nuu.config.FileConfig;
import com.nuu.http.DownloadListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.proto.DeviceStatus;
import com.nuu.proto.ServerResponse;
import com.nuu.proto.UpdateRequest;
import com.nuu.socket.ReceiveListener;
import com.nuu.util.AppUtils;
import com.nuu.util.DeviceUtils;

import java.io.File;
import java.lang.ref.WeakReference;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TcpClient";

    private static final int CHARGER_INFO = 104;
    private static final int CHARGER_COMPLIED = 202;

    private NormalHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new NormalHandler(this);

        initView();

        setNotifyListener();
    }

    private void initView() {
        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.btn_info).setOnClickListener(this);
    }


    private void setNotifyListener() {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_update: {
                /*String devId = Build.SERIAL;
                String curVerCode = AppUtils.getVersion(this);
                String brand = Build.BRAND;
                String model = Build.MODEL;*/

                String devId = "8a9adcd4";
                String curVerCode = "4";
                String brand = "NUU";
                String model = "i1";
                ReceiveListener callback = new ReceiveListener() {
                    @Override
                    public void OnRec(byte[] body) {
                        try {
                            final UpdateRequest.DeviceUpgradeResp ack = UpdateRequest.DeviceUpgradeResp.parseFrom(body);
                            String newVerCode = ack.getNewVerCode();
                            String url = ack.getUrl();
                            boolean result = ack.getResult();

                            if (result && !TextUtils.isEmpty(url)) {
                                //String deviceId = Build.SERIAL;
                                String deviceId = "8a9adcd4";
                                String token = AppUtils.md5("@com.nuu@" + deviceId);
                                String reqUrl = url + "?hwid=" + deviceId + "&vercode=" + newVerCode + "&token=" + token;

                                final String filePath = FileConfig.getApkDownLoadPath();
                                String fileName = "app.apk";
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

                            Log.d(TAG, result + "@" + newVerCode + "@" + url);
                        } catch (ExceptionInInitializerError e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } catch (NoClassDefFoundError e) {
                            e.printStackTrace();
                        }
                    }
                };
                MiFiManager.instance().checkUpdate(devId, curVerCode, brand, model, callback);
            }
            break;
            case R.id.btn_info:
                test();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(CHARGER_INFO);
    }

    private static class NormalHandler extends Handler {
        private final WeakReference<MainActivity> mTarget;

        NormalHandler(MainActivity target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity act = mTarget.get();
            switch (msg.what) {
                case CHARGER_INFO:
                    break;
                case CHARGER_COMPLIED:
                    break;
                default:
                    break;
            }
        }
    }


    private void test() {
        new Thread() {
            @Override
            public void run() {
                String devId = Build.SERIAL;
                int status = 1;
                int utc = 1;
                String ip = DeviceUtils.getIp();
                String mac = DeviceUtils.getMacAddress(mContext);
                DeviceStatus.SimCardSlot sim1 = DeviceUtils.getSimCard(mContext);
                ReceiveListener callback = new ReceiveListener() {
                    @Override
                    public void OnRec(byte[] body) {
                        try {
                            final ServerResponse.ReportDeviceStatusInfoResp ack = ServerResponse.ReportDeviceStatusInfoResp.parseFrom(body);
                            String test = ack.getDeviceId();
                        } catch (ExceptionInInitializerError e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } catch (NoClassDefFoundError e) {
                            e.printStackTrace();
                        }
                    }
                };
                MiFiManager.instance().deviceStatus(devId, status, utc, ip, mac,
                        sim1, null, callback);
            }
        }.start();
    }

}
