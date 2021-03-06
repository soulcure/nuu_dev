package com.nuu.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PackageRsp implements Parcelable {

    private String itf_name;
    private String trans_serial;
    private int err_code;
    private String err_desc;
    @SerializedName("package")
    private ArrayList<PackageBean> packageX;

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

    public ArrayList<PackageBean> getPackageX() {
        return packageX;
    }

    public void setPackageX(ArrayList<PackageBean> packageX) {
        this.packageX = packageX;
    }


    public int data() {
        int data = 0;
        if (packageX != null && packageX.size() > 0) {
            for (PackageBean item : packageX) {
                if (item.getStatus() == 1) {
                    data += item.getData();
                }
            }
        }
        return data;
    }

    public int dataUsed() {
        int dataUsed = 0;
        if (packageX != null && packageX.size() > 0) {
            for (PackageBean item : packageX) {
                if (item.getStatus() == 1) {
                    dataUsed += item.getData_used();
                }

            }
        }
        return dataUsed;
    }

    public int percent() {
        long dataUsed = dataUsed() * 100;
        long data = data();

        return (int) (dataUsed / data);
    }

    public String percentStr() {
        int data = 0;
        int dataUsed = 0;

        if (packageX != null && packageX.size() > 0) {
            for (PackageBean item : packageX) {
                if (item.getStatus() == 1) {
                    data += item.getData();
                    dataUsed += item.getData_used();
                }

            }
        }

        if (data > 0 && dataUsed > 0) {
            double d = dataUsed * 100.0f / data;
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(d) + "%";
        }

        return "0%";
    }

    public static class PackageBean implements Parcelable {

        private int package_id;
        private String package_name;
        private int device_package_id;
        private String begin_date;
        private String end_date;
        private String order_time;
        private int data;
        private int data_used;
        private int status;

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

        public int getDevice_package_id() {
            return device_package_id;
        }

        public void setDevice_package_id(int device_package_id) {
            this.device_package_id = device_package_id;
        }

        public String getBegin_date() {
            return begin_date;
        }

        public void setBegin_date(String begin_date) {
            this.begin_date = begin_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public int getData_used() {
            return data_used;
        }

        public void setData_used(int data_used) {
            this.data_used = data_used;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.package_id);
            dest.writeString(this.package_name);
            dest.writeInt(this.device_package_id);
            dest.writeString(this.begin_date);
            dest.writeString(this.end_date);
            dest.writeString(this.order_time);
            dest.writeInt(this.data);
            dest.writeInt(this.data_used);
            dest.writeInt(this.status);
        }

        public PackageBean() {
        }

        protected PackageBean(Parcel in) {
            this.package_id = in.readInt();
            this.package_name = in.readString();
            this.device_package_id = in.readInt();
            this.begin_date = in.readString();
            this.end_date = in.readString();
            this.order_time = in.readString();
            this.data = in.readInt();
            this.data_used = in.readInt();
            this.status = in.readInt();
        }

        public static final Creator<PackageBean> CREATOR = new Creator<PackageBean>() {
            @Override
            public PackageBean createFromParcel(Parcel source) {
                return new PackageBean(source);
            }

            @Override
            public PackageBean[] newArray(int size) {
                return new PackageBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itf_name);
        dest.writeString(this.trans_serial);
        dest.writeInt(this.err_code);
        dest.writeString(this.err_desc);
        dest.writeList(this.packageX);
    }

    public PackageRsp() {
    }

    protected PackageRsp(Parcel in) {
        this.itf_name = in.readString();
        this.trans_serial = in.readString();
        this.err_code = in.readInt();
        this.err_desc = in.readString();
        this.packageX = new ArrayList<PackageBean>();
        in.readList(this.packageX, PackageBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<PackageRsp> CREATOR = new Parcelable.Creator<PackageRsp>() {
        @Override
        public PackageRsp createFromParcel(Parcel source) {
            return new PackageRsp(source);
        }

        @Override
        public PackageRsp[] newArray(int size) {
            return new PackageRsp[size];
        }
    };
}
