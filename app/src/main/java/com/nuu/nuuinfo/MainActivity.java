package com.nuu.nuuinfo;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nuu.MiFiManager;
import com.nuu.config.AppConfig;
import com.nuu.config.FileConfig;
import com.nuu.http.IGetListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.install.AppMuteInstall;

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
        findViewById(R.id.btn_install).setOnClickListener(this);
        findViewById(R.id.btn_pm).setOnClickListener(this);
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
                String url = "http://localhost:8088/info";
                OkHttpConnector.httpGet(url, new IGetListener() {
                    @Override
                    public void httpReqResult(String response) {
                        Log.d("TcpClient", response);
                    }
                });
                break;
            case R.id.btn_install:
                installApp1();
                break;
            case R.id.btn_pm:
                installApp2();
                break;
        }
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
