package com.nuu.config;

import android.os.Environment;

import java.io.File;

public class AppConfig {

    public static final int SEND_BUFFER_SIZE = 1024; //1KB

    private static boolean developMode = false;

    /**
     * pref文件名定义
     */
    public static final String SHARED_PREFERENCES = "sdk_app";


    public static String getIp() {//配置默认ip
        return "119.23.74.49";
    }

    public static int getPort() {//配置默认端口
        return 18990;
    }

    /**
     * 配置文件路径
     */
    public static String getConfigFilePath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator + "nuuinfo.json";
    }


    public static String getReportFilePath() {//配置默认日志目录
        return Environment.getExternalStorageDirectory().getPath() + File.separator + "nuu";
    }


    public static int getSendReportRate() {//默认10分钟上报一次
        if (developMode) {
            return 20;
        }
        return 10 * 60;
    }

    public static int getReportStoreKeepDays() {//默认文件保存30天以内的
        if (developMode) {
            return 5;
        }
        return 30;
    }

    public static int getObtainReportRate() {//默认2分钟
        if (developMode) {
            return 5;
        }
        return 2 * 60;
    }

    public static String getDownloadApkName() {//配置下载升级app文件名
        return "app.apk";
    }


}
