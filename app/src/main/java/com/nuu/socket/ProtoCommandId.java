package com.nuu.socket;

import android.util.Log;


public class ProtoCommandId {
    private static final String TAG = "TcpClient";

    public enum Category {
        PMSG_CAT_UNIVERSAL,  /* unsupported */
        PMSG_CAT_REPORT,     /* report msg  */
        PMSG_CAT_REQUEST,    /* request msg */
        PMSG_CAT_ANSWER,     /* answer msg  */
        PMSG_CAT_ACK,        /* ack msg  */
        PMSG_CAT_MAX,        /* unsupported */
    }

    public static final short VERSION = 1;  //协议版本

    public static final short REPORT_DEVICE_STATUS = 50;  //上报设备状态
    public static final short REQUEST_DEVICE_UPGRADE = 51;  //版本升级


    private static short getMsgType(Category category, short msgType) {
        int cate = category.ordinal();
        int value = (VERSION << 12 & 0xF000) | (cate << 8 & 0x0F00) | (msgType & 0x00FF);

        Log.d(TAG, "tcp send commandId:" + msgType);
        return (short) (value & 0xFFFF);
    }


    /**
     * 上报设备状态msgType
     *
     * @return
     */
    public static short reportDeviceStatusMsgType() {
        return getMsgType(Category.PMSG_CAT_REPORT, REPORT_DEVICE_STATUS);
    }

    /**
     * 获取版本升级msgType
     *
     * @return
     */
    public static short requestDeviceUpgrade() {
        return getMsgType(Category.PMSG_CAT_REQUEST, REQUEST_DEVICE_UPGRADE);
    }
}
