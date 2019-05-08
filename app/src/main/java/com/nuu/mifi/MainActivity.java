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
import com.nuu.http.IGetListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.proto.UpdateRequest;
import com.nuu.socket.ReceiveListener;
import com.nuu.util.AppUtils;

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
        findViewById(R.id.btn_web).setOnClickListener(this);
    }


    private void setNotifyListener() {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_update: {
                String devId = Build.SERIAL;
                String curVerCode = AppUtils.getVersion(this);
                String brand = Build.BRAND;
                String model = Build.MODEL;
                if (BuildConfig.DEBUG) {
                    devId = "8a9adcd4";
                    curVerCode = "4";
                    brand = "NUU";
                    model = "i1";
                }
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
                                String deviceId = Build.SERIAL;
                                if (BuildConfig.DEBUG) {
                                    deviceId = "8a9adcd4";
                                }
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
                MiFiManager.instance().reportDeviceInfo();
                break;
            case R.id.btn_web:
                String url = "http://localhost:8088/info";
                OkHttpConnector.httpGet(url, new IGetListener() {
                    @Override
                    public void httpReqResult(String response) {
                        Log.d("TcpClient", response);
                    }
                });
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


}
