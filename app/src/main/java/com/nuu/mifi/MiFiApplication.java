package com.nuu.mifi;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nuu.MiFiManager;


public class MiFiApplication extends MultiDexApplication {

    private static final String TAG = MiFiApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化
        MiFiManager.instance().init(this, new MiFiManager.InitListener() {
            @Override
            public void success() {
                Log.d(TAG, "初始化成功");
            }

            @Override
            public void fail() {
                Log.e(TAG, "初始化失败");
            }
        });
    }

}

