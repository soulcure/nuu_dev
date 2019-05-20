package com.nuu.entity;

public class SettingSsidPwRsp {


    /**
     * itf_name : device_wifi_setup_resp
     * trans_serial : 1234cde
     * err_code : 0
     * task_id : 9640
     * err_desc : ok
     */

    private String itf_name;
    private String trans_serial;
    private int err_code;
    private int task_id;
    private String err_desc;

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

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getErr_desc() {
        return err_desc;
    }

    public void setErr_desc(String err_desc) {
        this.err_desc = err_desc;
    }
}
