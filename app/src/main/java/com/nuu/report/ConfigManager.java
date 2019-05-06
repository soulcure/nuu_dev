package com.nuu.report;

import android.text.TextUtils;

import com.nuu.config.AppConfig;
import com.nuu.entity.ReportConfig;
import com.nuu.util.FileUtils;
import com.nuu.util.GsonUtil;

public class ConfigManager {
    private static String TAG = "ConfigManager";

    private static ConfigManager configManager;
    private static ReportConfig curConfig;


    public static ConfigManager instance() {
        synchronized (ConfigManager.class) {
            if (configManager == null) {
                configManager = new ConfigManager();
            }
            return configManager;
        }
    }

    private ConfigManager() {
        String json = FileUtils.readFile(AppConfig.getConfigFilePath());
        if (!TextUtils.isEmpty(json)) {
            curConfig = GsonUtil.parse(json, ReportConfig.class);
        } else {
            defaultConfig();
        }
    }


    private void defaultConfig() {
        curConfig = new ReportConfig();
        curConfig.setObtainReportRate(AppConfig.getObtainReportRate());//获取频率 两分钟
        curConfig.setReportStoreKeepDays(AppConfig.getReportStoreKeepDays());//保存三十天
        curConfig.setReportStorePath(AppConfig.getReportFilePath());//保存路径
        curConfig.setSendReportRate(AppConfig.getSendReportRate());//发送频率
        curConfig.setSendToIp(AppConfig.getIp());//发送的目标 ip
        curConfig.setPort(AppConfig.getPort()); //发送的目标 端口
    }

}
