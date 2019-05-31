package com.nuu.entity;

import android.content.Context;

import com.nuu.MiFiManager;
import com.nuu.nuuinfo.BuildConfig;
import com.nuu.util.DeviceInfo;
import com.nuu.util.GsonUtil;
import com.nuu.util.TimeUtils;

import java.util.List;

public class ReportData {
    private String deviceSN;  //persist.telephony.imei1
    private String deviceId;  //Build.SERIAL
    private int unixTime;  //unix时间戳 单位秒
    private String ip;    //真实IP地址
    private String mac;   //mac地址
    private Sim1Bean sim1;  //卡1信息
    private Sim2Bean sim2; //卡2信息
    private int pow;   //电量
    private int charge;  //1 isCharging; 0 not charge
    private int netStatus; //网络状态  1 isAvailable；0 noAvailable
    private int hotPoint;  //热点开关 状态 1开启; 0 关闭
    private int adb;  //ADB开关 状态 1开启; 0 关闭
    private int hotAmount;  //热点连接数量
    private String speedState;  //网络速度
    private int netBrock;


    public ReportData(Context context) {
        deviceId = DeviceInfo.getDeviceId();
        deviceSN = DeviceInfo.getDeviceSN();
        unixTime = DeviceInfo.getUnixTimeStamp();
        ip = DeviceInfo.getIPAddress();

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
        sim2 = MiFiManager.instance().getCurSim2();
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
        private String imsi = ""; //sim卡ID
        private String plmn = "";//国家营运商编码
        private int signal;  //the signal strength as dBm
        private int lac;  //16-bit Location Area Code
        private int ci; //Either 16-bit GSM Cell Identity described 基站编号
        private int psc; ////16位跟踪区域代码 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown
        private int netMode;  //网络类型

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
        private String imsi = ""; //sim卡ID
        private String plmn = "";//国家营运商编码
        private int signal;  //the signal strength as dBm
        private int lac;  //16-bit Location Area Code
        private int ci; //Either 16-bit GSM Cell Identity described 基站编号
        private int psc; ////16位跟踪区域代码 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown
        private int netMode;  //网络类型

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
