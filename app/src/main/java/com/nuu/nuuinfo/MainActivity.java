package com.nuu.nuuinfo;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nuu.MiFiManager;
import com.nuu.config.AppConfig;
import com.nuu.config.FileConfig;
import com.nuu.entity.CurUsingPackageRsp;
import com.nuu.entity.DetailRsp;
import com.nuu.entity.PackageRsp;
import com.nuu.entity.DevicesStatusRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.install.AppMuteInstall;
import com.nuu.util.GsonUtil;
import com.nuu.util.ShellUtils;
import com.nuu.view.WaveLoadingView;

import java.io.File;
import java.lang.ref.WeakReference;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TcpClient";

    private static final int CHARGER_INFO = 104;
    private static final int CHARGER_COMPLIED = 202;

    private NormalHandler mHandler;

    private WaveLoadingView waveLoadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new NormalHandler(this);

        initView();

        setNotifyListener();

    }

    private void initView() {
        waveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);


        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.btn_info).setOnClickListener(this);
        findViewById(R.id.btn_web).setOnClickListener(this);
        findViewById(R.id.btn_install).setOnClickListener(this);
        findViewById(R.id.btn_pm).setOnClickListener(this);
        findViewById(R.id.btn_reboot).setOnClickListener(this);
        findViewById(R.id.btn_test).setOnClickListener(this);
    }


    private void setNotifyListener() {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_update:
                MiFiManager.instance().checkUpdate();
                break;
            case R.id.btn_info:
                MiFiManager.instance().reportDeviceInfo();
                break;
            case R.id.btn_web:
                webTest1();
                webTest2();
                webTest3();
                webTest4();
                break;
            case R.id.btn_install:
                installApp1();
                break;
            case R.id.btn_pm:
                installApp2();
                break;
            case R.id.btn_reboot:
                ShellUtils.execCmd("reboot", false);
                break;
            case R.id.btn_test:
                //reqStatus();
                //reqDetailToday();
                //reqDetailPeriod();
                //reqPurchasedPackage();
                //reqBuyPackage();
                reqCurUsingPackage();
                break;

        }
    }


    private void webTest1() {
        String url = "http://localhost:8088/transfer";

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                Log.d("TcpClient", "webTest1 transfer:" + response);
            }
        });
    }


    private void webTest2() {
        String url = "http://localhost:8088/transfer";

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_device_status");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                DevicesStatusRsp rsp = GsonUtil.parse(response, DevicesStatusRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    Log.d("TcpClient", "webTest2 transfer:" + response);
                }

            }
        });
    }

    private void webTest3() {
        String url = "http://localhost:8088/transfer";

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_device_package_info");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");


        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                DetailRsp rsp = GsonUtil.parse(response, DetailRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    Log.d("TcpClient", "webTest3 transfer:" + response);
                }
            }
        });
    }


    private void webTest4() {
        String url = "http://localhost:8088/transfer";

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_device_package_info");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                DetailRsp rsp = GsonUtil.parse(response, DetailRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    Log.d("TcpClient", "webTest4 transfer:" + response);
                }
            }
        });

    }

    private void installApp1() {
        final String filePath = FileConfig.getApkDownLoadPath();
        String fileName = AppConfig.getDownloadApkName();
        String path = filePath + File.separator + fileName;

        Log.d(TAG, "install path:" + path);

        AppMuteInstall.installPackage(this, path);


        /*try {
            AppMuteInstall install = new AppMuteInstall(this);
            install.installPackage(path);
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage());
        }*/
    }

    private void installApp2() {
        final String filePath = FileConfig.getApkDownLoadPath();
        String fileName = AppConfig.getDownloadApkName();
        String path = filePath + File.separator + fileName;

        //String path="/storage/emulated/0/test.apk";
        Log.d(TAG, "install path:" + path);

        AppMuteInstall.installByPm(path);
    }


    private void reqDevicesStatus() {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_device_status");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                DevicesStatusRsp rsp = GsonUtil.parse(response, DevicesStatusRsp.class);
            }
        });

    }





    private void reqPurchasedPackage() {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                PackageRsp rsp = GsonUtil.parse(response, PackageRsp.class);
                waveLoadingView.setCenterTitle(rsp.percentStr());
                waveLoadingView.setProgressValue(rsp.percent());
            }
        });

    }


    private void reqBuyPackage() {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");
        params.put("mcclist", "460,260");
        params.put("tz", "8");  // time zone
        params.put("start", "2019-07-05 12:00:00");  //  start time
        params.put("end", "2019-07-08 23:59:59");
        params.put("high", "1024");
        params.put("slow", "-1");
        params.put("slowspeed", "256");
        params.put("price", "500");
        params.put("currency_type", "2");
        params.put("priority", "1");
        params.put("user", "user");
        params.put("daily_plan", "0");
        params.put("day", 3);

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                //PackageRsp rsp = GsonUtil.parse(response, PackageRsp.class);
                //waveLoadingView.setCenterTitle(rsp.percentStr());
                //waveLoadingView.setProgressValue(rsp.percent());
            }
        });

    }


    private void reqCurUsingPackage() {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_using_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                CurUsingPackageRsp rsp = GsonUtil.parse(response, CurUsingPackageRsp.class);
            }
        });

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
