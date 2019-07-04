package com.nuu.config;

import android.content.ContentValues;
import android.os.Environment;

import com.nuu.nuuinfo.BuildConfig;

import java.io.File;

public class AppConfig {

    /**
     * pref文件名定义
     */
    public static final String SHARED_PREFERENCES = "sdk_app";


    //private static final String HOST = "http://119.23.74.49:80";//开发服务器
    //private static final String HOST = "http://47.91.250.107:80";//测试服务器
    private static final String HOST = "http://192.168.43.168:8899";//测试服务器

    public static final String DEVICE_INFO = "http://192.168.43.1:8088/info";//获取设备状态信息

    public static String getIp() {//配置默认ip
        return "119.23.74.49";
    }

    public static int getPort() {//配置默认端口
        return 18990;
    }


    public static String getHost() {
        return HOST;
    }

    public static String getRouterHost() {
        return "http://47.91.250.107:80";
    }


    public static String getRouterPath() {
        return "/api/v1/api_device";
    }

    public static ContentValues getParam(String apiName) {
        ContentValues params = new ContentValues();
        params.put("itf_name", apiName);  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");
        return params;
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
        if (BuildConfig.DEBUG) {
            return 10 * 60;
        }
        return 10 * 60;
    }

    public static int getReportStoreKeepDays() {//默认文件保存30天以内的
        if (BuildConfig.DEBUG) {
            return 5;
        }
        return 30;
    }

    public static int getObtainReportRate() {//默认2分钟
        if (BuildConfig.DEBUG) {
            return 2 * 60;
        }
        return 2 * 60;
    }

    public static String getDownloadApkName() {//配置下载升级app文件名
        return "app.apk";
    }


}
