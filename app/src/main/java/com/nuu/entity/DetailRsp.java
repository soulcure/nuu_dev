package com.nuu.entity;

import java.util.List;

public class DetailRsp {


    /**
     * itf_name : query_device_package_info_resp
     * trans_serial : 1234cde
     * err_code : 0
     * err_desc : ok
     * date :
     * used_data : 25163
     * total_data : 0
     * used_dtl : [{"mcc":460,"country":"China","used_data":25163}]
     */

    private String itf_name;
    private String trans_serial;
    private int err_code;
    private String err_desc;
    private String date;
    private int used_data;
    private int total_data;
    private List<UsedDtlBean> used_dtl;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUsed_data() {
        return used_data;
    }

    public void setUsed_data(int used_data) {
        this.used_data = used_data;
    }

    public int getTotal_data() {
        return total_data;
    }

    public void setTotal_data(int total_data) {
        this.total_data = total_data;
    }

    public List<UsedDtlBean> getUsed_dtl() {
        return used_dtl;
    }

    public void setUsed_dtl(List<UsedDtlBean> used_dtl) {
        this.used_dtl = used_dtl;
    }

    public static class UsedDtlBean {
        /**
         * mcc : 460
         * country : China
         * used_data : 25163
         */

        private int mcc;
        private String country;
        private int used_data;

        public int getMcc() {
            return mcc;
        }

        public void setMcc(int mcc) {
            this.mcc = mcc;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getUsed_data() {
            return used_data;
        }

        public void setUsed_data(int used_data) {
            this.used_data = used_data;
        }
    }
}
