package com.nuu.socket;

import com.nuu.config.AppConfig;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class ProtoCommandId {
    private static final String TAG = "TcpClient";

    public static final String CHARGE_CODE = "N010116980100003";  //充电桩编码
    public static final String CARD_CODE = "o9o2u4lNQnp7hEFv9i6J2dAUjj2g";  //充电卡号
    public static final String VIN_CODE = "1z69f";  //vin
    public static final String ORDE_CODE = "charger_order_";  //充电流水号

    public static final String SOFT_VERSION = "104.10";  //充电桩软件版本
    public static final String PROTO_VER = "V2.6";  //充电桩软件版本
    public static final byte HEAD_CODE = 0x01;  //协议头 序列号

    public static final String LOGIN_PW = "123456";  //登录服务器密码

    public static final short CMD_CHARGER_INT_SETTING_RSP = 1;  //服务器下发充电桩整形工作参数
    public static final short CMD_CHARGER_INT_SETTING_REQ = 2;  //充电桩向服务器请求整形工作参数

    public static final short CMD_CHARGER_STR_SETTING_RSP = 3;  //服务器下发充电桩字符型工作参数
    public static final short CMD_CHARGER_STR_SETTING_REQ = 4;  //充电桩参数字符形设置/查询应答

    public static final short CMD_CHARGER_CONTROL_RSP = 5;  //服务器下发充电桩控制命令
    public static final short CMD_CHARGER_CONTROL_REQ = 6;  //充电桩对后台控制命令应答

    public static final short CMD_CHARGER_OPEN_RSP = 7;  //服务器下发充电桩开启充电控制命令
    public static final short CMD_CHARGER_OPEN_REQ = 8;  //充电桩对后台下发的充电桩开启充电控制应答

    public static final short CMD_UPLOAD_RSP = 9;  //服务器应答桩上传命令请求
    public static final short CMD_UPLOAD_REQ = 10;  //充电桩上传命令请求（预留）

    public static final short CMD_HEART_BEAT_RSP = 101;  //服务器应答心跳包信息
    public static final short CMD_HEART_BEAT_REQ = 102;  //充电桩上传心跳包信息

    public static final short CMD_STATUS_RSP = 103;  //服务器应答充电桩状态信息包
    public static final short CMD_STATUS_REQ = 104;  //充电桩状态信息包上报

    public static final short CMD_LOGIN_RSP = 105;  //服务器回复充电桩鉴权
    public static final short CMD_LOGIN_REQ = 106;  //充电桩向服务器鉴权请求

    public static final short CMD_WARNING_RSP = 107;  //服务器应答充电桩告警信息(服务器暂时不用回复）
    public static final short CMD_WARNING_REQ = 108;  //充电桩告警信息上报（预留）

    public static final short CMD_BOOT_COMPLETE_RSP = 109;  //服务器应答充电桩启动完成命令（预留）
    public static final short CMD_BOOT_COMPLETE_REQ = 110;  //充电桩上报启动完成命令（直流预留）

    public static final short CMD_LAST_CHARGE_INFO_RSP = 201;  //服务器应答充电桩充电上报最新一次充电信息报文
    public static final short CMD_LAST_CHARGE_INFO_REQ = 202;  //充电桩上报充电记录信息

    public static final short CMD_ACCOUT_QUERY_RSP = 203;  //服务器应答帐户查询信息
    public static final short CMD_ACCOUT_QUERY_REQ = 204;  //充电桩充电上传用户帐户查询报文

    public static final short CMD_CHARGE_PASSWORD_RSP = 205;  //服务器应答充电密码验证报文
    public static final short CMD_CHARGE_PASSWORD_REQ = 206;  //充电桩上传用户密码验证报文

    public static final short CMD_REPORT_BMS_RSP = 301;  //服务器应答充电桩上报BMS信息
    public static final short CMD_REPORT_BMS_REQ = 302;  //充电桩上报BMS信息（预留）

    public static final short CMD_REPORT_CHARGE_RSP = 303;  //服务器应答充电桩上报
    public static final short CMD_REPORT_CHARGE_REQ = 304;  //充电桩上报BMS信息

    public static final short CMD_CHARGE_HISTORY_RSP = 401;  //服务器查询充电桩历史充电记录
    public static final short CMD_CHARGE_HISTORY_REQ = 402;  //充电桩上报历史的记录

    public static final short CMD_ERASE_BIN_RSP = 1001;  //服务器下发擦除指令
    public static final short CMD_ERASE_BIN_REQ = 1002;  //充电桩回复服务器下发擦除指令


    public static final short CMD_UPDATE_BIN_RSP = 1003;  //服务器下发升级文件名指令
    public static final short CMD_UPDATE_BIN_REQ = 1004;  //充电桩回复服务器下发擦除指令

    public static final short CMD_BIN_SIZE_RSP = 1005;  //服务器下发升级文件大小
    public static final short CMD_BIN_SIZE_REQ = 1006;  //充电桩应答服务器下发升级文件大小指令

    public static final short CMD_BIN_RSP = 1007;  //服务器下发升级文件数据
    public static final short CMD_BIN_REQ = 1008;  //充电桩应答服务器下发升级文件数据指令

    public static final short CMD_UPDATE_FINISH_RSP = 1009;  //服务器下发升级文件数据结束指令
    public static final short CMD_UPDATE_FINISH_REQ = 1010;  //充电桩应答服务器下发升级文件数据结束指令

    public static final short CMD_REBOOT_RSP = 1011;  //服务器下发重启指令
    public static final short CMD_REBOOT_REQ = 1012;  //充电桩应答服务器下发重启指令

    public static final short CMD_ELECTRICITY_PRICE_RSP = 1101;  //后台服务器查询24时电费计价策略信息
    public static final short CMD_ELECTRICITY_PRICE_REQ = 1102;  //充电桩应答后台服务器查询24时电费计价策略信息

    public static final short CMD_ELEC_PRICE_RSP = 1103;  //服务器设置24时段电费计价策略信息
    public static final short CMD_ELEC_PRICE_REQ = 1104;  //充电桩应答后台服务器查询24时电费计价策略信息

    public static final short CMD_ENCRYPTION_PACKAGE_RSP = 1201;  //服务器回复充电桩密码登入报文
    public static final short CMD_ENCRYPTION_PACKAGE_REQ = 1202;  //充电桩密码登入报文


    public static ByteBuffer sendBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(AppConfig.SEND_BUFFER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer;
    }


    public static byte[] getMatchCode() {
        byte[] matchCode = new byte[32];

        byte[] codeBytes = CHARGE_CODE.getBytes(Charset.forName("US-ASCII"));
        if (codeBytes.length <= matchCode.length) {
            System.arraycopy(codeBytes, 0, matchCode, 0, codeBytes.length);
        }
        return matchCode;
    }

    public static byte[] getCardCode() {
        byte[] cardCode = new byte[32];

        byte[] codeBytes = CARD_CODE.getBytes(Charset.forName("US-ASCII"));
        if (codeBytes.length <= cardCode.length) {
            System.arraycopy(codeBytes, 0, cardCode, 0, codeBytes.length);
        }
        return cardCode;
    }

    public static byte[] getVinCode() {
        byte[] vinCode = new byte[17];

        byte[] codeBytes = VIN_CODE.getBytes(Charset.forName("US-ASCII"));
        if (codeBytes.length <= vinCode.length) {
            System.arraycopy(codeBytes, 0, vinCode, 0, codeBytes.length);
        }
        return vinCode;
    }

    public static byte[] getOrderCode() {
        byte[] orderCode = new byte[32];

        String code = ORDE_CODE + System.currentTimeMillis();
        byte[] codeBytes = code.getBytes(Charset.forName("US-ASCII"));

        if (codeBytes.length <= orderCode.length) {
            System.arraycopy(codeBytes, 0, orderCode, 0, codeBytes.length);
        }
        return orderCode;
    }


    public static byte[] getLoginPassWord() {
        byte[] pw = new byte[16];

        byte[] codeBytes = LOGIN_PW.getBytes(Charset.forName("US-ASCII"));
        if (codeBytes.length <= pw.length) {
            System.arraycopy(codeBytes, 0, pw, 0, codeBytes.length);
        }
        return pw;
    }


    public static PduBase buildProto(short commandId, byte[] params) {

        PduBase pdu = new PduBase();
        pdu.commandId = commandId;
        if (params != null) {
            pdu.params = params;
            pdu.length = (short) params.length;
        } else {
            pdu.params = null;
            pdu.length = 0;
        }

        return pdu;
    }


}
