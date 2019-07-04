package com.nuu.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Devices {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String deviceSN;  //persist.telephony.imei1
    @Unique
    private String deviceId;  //Build.SERIAL
    @Generated(hash = 1866488382)
    public Devices(Long id, String deviceSN, String deviceId) {
        this.id = id;
        this.deviceSN = deviceSN;
        this.deviceId = deviceId;
    }
    @Generated(hash = 597445211)
    public Devices() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeviceSN() {
        return this.deviceSN;
    }
    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }
    public String getDeviceId() {
        return this.deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}