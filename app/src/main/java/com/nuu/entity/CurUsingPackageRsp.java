package com.nuu.entity;

public class CurUsingPackageRsp {


    /**
     * itf_name : query_using_package_resp
     * trans_serial : 1234cde
     * err_code : 0
     * err_desc : ok
     * device_package_id : 2096
     * package_name : mifi_thirtydays
     * used_data : 106690
     * device_online : 1
     * order_id : 2019050906490715825
     * expire_time : 2019-06-07 23:59:59
     */

    private String itf_name;
    private String trans_serial;
    private int err_code;
    private String err_desc;
    private int device_package_id;
    private String package_name;
    private int used_data;
    private int device_online;
    private String order_id;
    private String expire_time;

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

    public int getDevice_package_id() {
        return device_package_id;
    }

    public void setDevice_package_id(int device_package_id) {
        this.device_package_id = device_package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public int getUsed_data() {
        return used_data;
    }

    public void setUsed_data(int used_data) {
        this.used_data = used_data;
    }

    public int getDevice_online() {
        return device_online;
    }

    public void setDevice_online(int device_online) {
        this.device_online = device_online;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }
}
