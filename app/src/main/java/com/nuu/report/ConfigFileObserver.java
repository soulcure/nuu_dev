package com.nuu.report;

import android.content.Context;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.util.Log;


public class ConfigFileObserver extends FileObserver {
    private Context mContext;
    private static String TAG = "ConfigFileObserver";

    public ConfigFileObserver(String path, Context context) {
        super(path);
        this.mContext = context;
    }


    @Override
    public void onEvent(int event, @Nullable String path) {

        switch (event) {
            case FileObserver.ACCESS:
                Log.d(TAG, "ACCESS");
                break;
            case FileObserver.MODIFY:
                Log.d(TAG, "MODIFY");
                configFileChanged();
                break;
            case FileObserver.CREATE:
                Log.d(TAG, "CREATE");
                configFileChanged();
                break;
            case FileObserver.DELETE:
                Log.d(TAG, "DELETE");
                configFileChanged();
                break;

        }


    }

    private void configFileChanged() {
        new Thread() {
            @Override
            public void run() {
                ConfigManager.instance().setCurConfig();// 重新设置配置
            }
        }.start();
    }
}
