package com.nuu.entity;

public class WifiClient {

    private String ip;
    private String mac;
    private String flag;

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    public String getFlag() {
        return flag;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "WifiClient{" +
                "ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
