package com.nuu.entity;

public class CardItem {
    private String itfName;
    private int packageId;
    private int usedData;
    private int totalData;
    private String expireTime;
    private PackageRsp packageRsp;
    private DevicesStatusRsp devicesStatusRsp;

    public CardItem(String itfName) {
        this.itfName = itfName;
    }

    public String getItfName() {
        return itfName;
    }

    public void setItfName(String itfName) {
        this.itfName = itfName;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getUsedData() {
        return usedData;
    }

    public void setUsedData(int usedData) {
        this.usedData = usedData;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }


    public PackageRsp getPackageRsp() {
        return packageRsp;
    }

    public void setPackageRsp(PackageRsp packageRsp) {
        this.packageRsp = packageRsp;
    }

    public DevicesStatusRsp getDevicesStatusRsp() {
        return devicesStatusRsp;
    }

    public void setDevicesStatusRsp(DevicesStatusRsp devicesStatusRsp) {
        this.devicesStatusRsp = devicesStatusRsp;
    }
}
