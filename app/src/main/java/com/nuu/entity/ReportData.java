package com.nuu.entity;

import android.content.Context;

import com.nuu.mifi.BuildConfig;
import com.nuu.util.DeviceInfo;
import com.nuu.util.GsonUtil;
import com.nuu.util.TimeUtils;

import java.util.List;

public class ReportData {

    private String deviceSN;
    private String deviceId;
    private int unixTime;
    private String ip;
    private String mac;
    private Sim1Bean sim1;
    private Sim2Bean sim2;
    private int pow;
    private int charge;
    private int netStatus;
    private int hotPoint;
    private int adb;
    private int hotAmount;
    private String speedState;
    private int netBrock;


    public ReportData() {

    }

    public ReportData(Context context) {
        deviceId = DeviceInfo.getDeviceId();
        deviceSN = DeviceInfo.getDeviceSN();
        unixTime = DeviceInfo.getUnixTimeStamp();
        ip = DeviceInfo.getIPAddress();

        if (BuildConfig.DEBUG) {
            deviceId = "8a9adcd4";
        }

        List<WifiClient> wifiApClientList = DeviceInfo.getWifiApClientList();
        hotAmount = wifiApClientList.size();
        mac = DeviceInfo.getMacAddress(wifiApClientList);

        pow = DeviceInfo.getBatteryInfo(context).getPow();
        charge = DeviceInfo.getBatteryInfo(context).getCharge();
        speedState = DeviceInfo.getSpeedStateStr();
        hotPoint = DeviceInfo.getHotPointState(context);
        adb = DeviceInfo.getAdbStatus(context);
        netStatus = DeviceInfo.getNetStatus(context);
        netBrock = 0;

        sim1 = DeviceInfo.getSim1(context);
        sim2 = new ReportData.Sim2Bean();
    }

    public String getDeviceSN() {
        return deviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(int unixTime) {
        this.unixTime = unixTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Sim1Bean getSim1() {
        return sim1;
    }

    public void setSim1(Sim1Bean sim1) {
        this.sim1 = sim1;
    }

    public Sim2Bean getSim2() {
        return sim2;
    }

    public void setSim2(Sim2Bean sim2) {
        this.sim2 = sim2;
    }

    public int getPow() {
        return pow;
    }

    public void setPow(int pow) {
        this.pow = pow;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(int netStatus) {
        this.netStatus = netStatus;
    }

    public int getHotPoint() {
        return hotPoint;
    }

    public void setHotPoint(int hotPoint) {
        this.hotPoint = hotPoint;
    }

    public int getAdb() {
        return adb;
    }

    public void setAdb(int adb) {
        this.adb = adb;
    }

    public int getHotAmount() {
        return hotAmount;
    }

    public void setHotAmount(int hotAmount) {
        this.hotAmount = hotAmount;
    }

    public String getSpeedState() {
        return speedState;
    }

    public void setSpeedState(String speedState) {
        this.speedState = speedState;
    }

    public int getNetBrock() {
        return netBrock;
    }

    public void setNetBrock(int netBrock) {
        this.netBrock = netBrock;
    }

    public static class Sim1Bean {
        private String imsi;
        private String plmn;
        private int signal;
        private int lac;
        private int ci;
        private int psc;
        private int netMode;

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        public String getPlmn() {
            return plmn;
        }

        public void setPlmn(String plmn) {
            this.plmn = plmn;
        }

        public int getSignal() {
            return signal;
        }

        public void setSignal(int signal) {
            this.signal = signal;
        }

        public int getLac() {
            return lac;
        }

        public void setLac(int lac) {
            this.lac = lac;
        }

        public int getCi() {
            return ci;
        }

        public void setCi(int ci) {
            this.ci = ci;
        }

        public int getPsc() {
            return psc;
        }

        public void setPsc(int psc) {
            this.psc = psc;
        }

        public int getNetMode() {
            return netMode;
        }

        public void setNetMode(int netMode) {
            this.netMode = netMode;
        }
    }

    public static class Sim2Bean {
        private String imsi;
        private String plmn;
        private int signal;
        private int lac;
        private int ci;
        private int psc;
        private int netMode;

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        public String getPlmn() {
            return plmn;
        }

        public void setPlmn(String plmn) {
            this.plmn = plmn;
        }

        public int getSignal() {
            return signal;
        }

        public void setSignal(int signal) {
            this.signal = signal;
        }

        public int getLac() {
            return lac;
        }

        public void setLac(int lac) {
            this.lac = lac;
        }

        public int getCi() {
            return ci;
        }

        public void setCi(int ci) {
            this.ci = ci;
        }

        public int getPsc() {
            return psc;
        }

        public void setPsc(int psc) {
            this.psc = psc;
        }

        public int getNetMode() {
            return netMode;
        }

        public void setNetMode(int netMode) {
            this.netMode = netMode;
        }
    }


    @Override
    public String toString() {
        return TimeUtils.getTime(System.currentTimeMillis()) + "   " + GsonUtil.format(this);
    }

    public String toJson() {
        return GsonUtil.format(this);
    }
}
