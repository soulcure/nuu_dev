package com.nuu.report;

import android.content.Context;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.util.Log;


public class ConfigFileObserver extends FileObserver {
    private static String TAG = ConfigFileObserver.class.getSimpleName();
    private Context mContext;

    public ConfigFileObserver(String path, Context context) {
        super(path);
        this.mContext = context;
    }


    @Override
    public void onEvent(int event, @Nullable String path) {
        Log.d(TAG, "ConfigFileObserver onEvent:" + event);
        switch (event) {
            case FileObserver.ACCESS:
                Log.d(TAG, "ACCESS");
                break;
            case FileObserver.MODIFY:
            case FileObserver.ATTRIB:
            case FileObserver.CREATE:
            case FileObserver.DELETE:
                Log.d(TAG, "configFileChanged");
                configFileChanged();
                break;

        }


    }

    private void configFileChanged() {
        ConfigManager.instance().setCurConfig();// 重新设置配置
    }
}
