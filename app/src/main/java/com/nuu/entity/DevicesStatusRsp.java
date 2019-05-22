package com.nuu.entity;

public class DevicesStatusRsp {

    /**
     * itf_name : query_device_status_resp
     * trans_serial : 1234cde
     * err_code : 0
     * err_desc : ok
     * status : enable
     * last_heartbeat : 20190515145149
     * location : China
     */

    private String itf_name;
    private String trans_serial;
    private int err_code;
    private String err_desc;
    private String status;
    private String last_heartbeat;
    private String location;


    public String getItf_name() {
        return itf_name;
    }

    public void setItf_name(String itf_name) {
        this.itf_name = itf_name;
    }

    public String getTrans_serial() {
        return trans_serial;
    }

    public void setTrans_serial(String trans_serial) {
        this.trans_serial = trans_serial;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_desc() {
        return err_desc;
    }

    public void setErr_desc(String err_desc) {
        this.err_desc = err_desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_heartbeat() {
        return last_heartbeat;
    }

    public void setLast_heartbeat(String last_heartbeat) {
        this.last_heartbeat = last_heartbeat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
