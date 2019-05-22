package com.nuu.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.TimeZone;

public class PackageDetailByCountryRsp {

    /**
     * itf_name : query_package_for_device_resp
     * trans_serial : 1234cde
     * err_code : 0
     * err_desc : ok
     * package : [{"package_id":8,"package_name":"中日韩7天","currency":"MCY","cost":"1500.00","data":500,"cycle_time_type":3,"cycle_time":1,"time_zone":8,"remark":"","country":["China","Korea (South)","Japan"]},{"package_id":12,"package_name":"mifi_thirtydays","currency":"MCY","cost":"100.00","data":10485760,"cycle_time_type":5,"cycle_time":30,"time_zone":8,"remark":"","country":["China","Hong Kong","Taiwan","Macao","Japan","Thailand","Singapore","Indonesia"]},{"package_id":16,"package_name":"使用时激活套餐","currency":"MCY","cost":"0.00","data":10485760,"cycle_time_type":5,"cycle_time":30,"time_zone":0,"remark":"","country":["China","Hong Kong"]},{"package_id":23,"package_name":"100M测试套餐","currency":"MCY","cost":"0.00","data":51200,"cycle_time_type":3,"cycle_time":1,"time_zone":0,"remark":"","country":["China","Hong Kong"]},{"package_id":30,"package_name":"100M无限包","currency":"MCY","cost":"0.00","data":0,"cycle_time_type":5,"cycle_time":1,"time_zone":8,"remark":"","country":["China","Hong Kong"]},{"package_id":31,"package_name":"使用时激活连续天24小时套餐","currency":"MCY","cost":"0.00","data":1048576,"cycle_time_type":5,"cycle_time":10,"time_zone":8,"remark":"","country":["China","Hong Kong"]},{"package_id":32,"package_name":"使用时激活连续按日24小时结算","currency":"MCY","cost":"0.00","data":51200,"cycle_time_type":5,"cycle_time":10,"time_zone":8,"remark":"","country":["China","Hong Kong"]}]
     */

    private String itf_name;
    private String trans_serial;
    private int err_code;
    private String err_desc;
    @SerializedName("package")
    private List<PackageBean> packageX;

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

    public List<PackageBean> getPackageX() {
        return packageX;
    }

    public void setPackageX(List<PackageBean> packageX) {
        this.packageX = packageX;
    }

    public static class PackageBean {
        /**
         * package_id : 8
         * package_name : 中日韩7天
         * currency : MCY
         * cost : 1500.00
         * data : 500
         * cycle_time_type : 3
         * cycle_time : 1
         * time_zone : 8
         * remark :
         * country : ["China","Korea (South)","Japan"]
         */

        private int package_id;
        private String package_name;
        private String currency;
        private String cost;
        private int data;
        private int cycle_time_type;
        private int cycle_time;
        private int time_zone;
        private String remark;
        private List<String> country;

        public int getPackage_id() {
            return package_id;
        }

        public void setPackage_id(int package_id) {
            this.package_id = package_id;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public String getCycle_time_type() {
            String type;
            switch (cycle_time_type) {
                case 1:
                    type = "Day";
                    break;
                case 3:
                    type = "Month";
                    break;
                case 4:
                    type = "Year";
                    break;
                case 5:
                    type = "Day";
                    break;
                default:
                    type = "Day";
                    break;
            }

            return type;
        }

        public void setCycle_time_type(int cycle_time_type) {
            this.cycle_time_type = cycle_time_type;
        }

        public String getCycle_time() {
            return String.valueOf(cycle_time);
        }

        public void setCycle_time(int cycle_time) {
            this.cycle_time = cycle_time;
        }

        public String getTime_zone() {
            String[] ids = TimeZone.getAvailableIDs();
            if (time_zone < ids.length) {
                return ids[time_zone];
            } else {
                return TimeZone.getDefault().getDisplayName();
            }
        }

        public void setTime_zone(int time_zone) {
            this.time_zone = time_zone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public List<String> getCountry() {
            return country;
        }

        public void setCountry(List<String> country) {
            this.country = country;
        }
    }
}
