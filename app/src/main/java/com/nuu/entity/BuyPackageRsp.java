package com.nuu.entity;

public class BuyPackageRsp {


    /**
     * itf_name : buy_package_resp
     * trans_serial : 1234cde
     * err_code : 0
     * err_desc : ok
     * device_package_id : 2099
     * device_package_id_list : 2099
     * order_id : 2019052209200315841
     */

    private String itf_name;
    private String trans_serial;
    private int err_code;
    private String err_desc;
    private int device_package_id;
    private String device_package_id_list;
    private String order_id;

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

    public String getDevice_package_id_list() {
        return device_package_id_list;
    }

    public void setDevice_package_id_list(String device_package_id_list) {
        this.device_package_id_list = device_package_id_list;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
