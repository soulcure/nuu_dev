package com.nuu.entity;

public class Devices {
    private String devicesId;
    private String devicesSn;
    private boolean status;

    public String getDevicesId() {
        return devicesId;
    }

    public void setDevicesId(String devicesId) {
        this.devicesId = devicesId;
    }

    public String getDevicesSn() {
        return devicesSn;
    }

    public void setDevicesSn(String devicesSn) {
        this.devicesSn = devicesSn;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
