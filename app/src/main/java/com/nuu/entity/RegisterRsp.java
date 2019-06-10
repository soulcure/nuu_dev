package com.nuu.entity;

public class RegisterRsp {
    /**
     * itf_name : device_customer_register_resp
     * trans_serial : 1234cde
     * err_code : 0
     * err_desc : ok
     * cust_id : 1
     */

    private String itf_name;
    private String trans_serial;
    private int err_code;
    private String err_desc;
    private int cust_id;

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

    public int getCust_id() {
        return cust_id;
    }

    public void setCust_id(int cust_id) {
        this.cust_id = cust_id;
    }
}
