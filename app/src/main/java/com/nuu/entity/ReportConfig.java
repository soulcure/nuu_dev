package com.nuu.entity;

public class ReportConfig {


    /**
     * reportStorePath : /storage/sdcard0/nuu0
     * obtainReportRate : 120
     * sendReportRate : 600
     * sendToIp : 119.23.74.49
     * port : 18990
     * reportStoreKeepDays : 6
     */

    private String reportStorePath;
    private int obtainReportRate;
    private int sendReportRate;
    private String sendToIp;
    private int port;
    private int reportStoreKeepDays;
    private String router;

    public String getReportStorePath() {
        return reportStorePath;
    }

    public void setReportStorePath(String reportStorePath) {
        this.reportStorePath = reportStorePath;
    }

    public int getObtainReportRate() {
        return obtainReportRate;
    }

    public void setObtainReportRate(int obtainReportRate) {
        this.obtainReportRate = obtainReportRate;
    }

    public int getSendReportRate() {
        return sendReportRate;
    }

    public void setSendReportRate(int sendReportRate) {
        this.sendReportRate = sendReportRate;
    }

    public String getSendToIp() {
        return sendToIp;
    }

    public void setSendToIp(String sendToIp) {
        this.sendToIp = sendToIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReportStoreKeepDays() {
        return reportStoreKeepDays;
    }

    public void setReportStoreKeepDays(int reportStoreKeepDays) {
        this.reportStoreKeepDays = reportStoreKeepDays;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }
}
