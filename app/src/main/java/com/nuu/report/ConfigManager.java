package com.nuu.report;

import android.text.TextUtils;

import com.nuu.config.AppConfig;
import com.nuu.entity.ReportConfig;
import com.nuu.util.FileUtils;
import com.nuu.util.GsonUtil;

public class ConfigManager {
    private static String TAG = ConfigManager.class.getSimpleName();

    private static ConfigManager instance;

    private ReportConfig curConfig;
    private OnChange changeListener;


    public interface OnChange {
        void onStorePathChange(String path);

        void onStoreKeepDaysChange(int days);

        void onObtainReportRateChange(int freq);

        void onSendReportRateChange(int freq);
    }


    public void setChangeListener(OnChange changeListener) {
        this.changeListener = changeListener;
    }


    public static ConfigManager instance() {
        synchronized (ConfigManager.class) {
            if (instance == null) {
                instance = new ConfigManager();
            }
            return instance;
        }
    }

    private ConfigManager() {
        String json = FileUtils.readFile(AppConfig.getConfigFilePath());
        if (!TextUtils.isEmpty(json)) {
            curConfig = GsonUtil.parse(json, ReportConfig.class);
        }
        if (curConfig == null) {
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


    public ReportConfig getCurConfig() {
        return curConfig;
    }

    /**
     * 配置文件发生改变时会调用该方法
     */
    public void setCurConfig() {
        synchronized (ConfigManager.class) {
            String json = FileUtils.readFile(AppConfig.getConfigFilePath());
            if (!TextUtils.isEmpty(json)) {
                ReportConfig temp = GsonUtil.parse(json, ReportConfig.class);
                if (!curConfig.getReportStorePath().equals(temp.getReportStorePath())
                        && changeListener != null) {
                    changeListener.onStorePathChange(temp.getReportStorePath());
                }
                if (curConfig.getObtainReportRate() != (temp.getObtainReportRate())
                        && changeListener != null) {
                    changeListener.onObtainReportRateChange(temp.getObtainReportRate());
                }

                if (curConfig.getSendReportRate() != (temp.getSendReportRate())
                        && changeListener != null) {
                    changeListener.onSendReportRateChange(temp.getSendReportRate());
                }

                curConfig = temp;
            }
            if (curConfig == null) {
                defaultConfig();
            }
        }

    }


}
