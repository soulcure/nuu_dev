package com.nuu.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;

import com.nuu.entity.BatteryInfo;
import com.nuu.entity.ReportData;
import com.nuu.entity.WifiClient;
import com.nuu.proto.DeviceStatus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceInfo {

    public static String getDeviceId() {
        return Build.SERIAL;
    }

    public static String getDeviceSN() {
        String imei1 = SystemProperties.get("persist.telephony.imei1");
        if (imei1 == null) {
            imei1 = "";
        }
        return imei1;
    }

    public static int getUnixTimeStamp() {
        int unixTime = (int) (System.currentTimeMillis() / 1000);
        return unixTime;
    }

    public static BatteryInfo getBatteryInfo(Context context) {

        BatteryInfo batteryInfo = new BatteryInfo();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.getApplicationContext().registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING) ||
                (status == BatteryManager.BATTERY_STATUS_FULL);
        if (isCharging) {
            batteryInfo.setCharge(1);
        } else {
            batteryInfo.setCharge(0);
        }


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) (level / (float) scale * 100);
        batteryInfo.setPow(batteryPct);

        return batteryInfo;
    }


    public static int getNetStatus(Context context) {

        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空
        if (networkInfo != null) {
            if (networkInfo.isAvailable()) {
                return 1;
            }
            return 0;
        }
        return 0;
    }


    public static int getAdbStatus(Context context) {
        //获取 ADB开关 状态 1开启 0 关闭
        return Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
    }

    public static int getHotPointState(Context context) {

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        try {
            Class<?> wifiManagerClass = Class.forName(wifiManager.getClass().getName());

            Method getWifiApState = wifiManagerClass.getMethod("getWifiApState");

            Object ob_status = getWifiApState.invoke(wifiManager);
            if (ob_status != null) {
                int wifiState = Integer.parseInt(ob_status.toString());
//                WIFI_AP_STATE_DISABLED = 11;
//                WIFI_AP_STATE_ENABLING = 12
//                WIFI_AP_STATE_ENABLED = 13;
                if (wifiState == 13) {
                    return 1;
                }
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public static List<WifiClient> getWifiApClientList() {
        BufferedReader reader = null;
        ArrayList<WifiClient> wifiClients = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("/proc/net/arp"));
            //读取第一行信息，就是IP address HW type Flags HW address Mask Device
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("[ ]+");
                if (tokens.length < 6) {
                    continue;
                }
                String ip = tokens[0]; //ip
                String mac = tokens[3];  //mac 地址
                String flag = tokens[2];//表示连接状态


                if (mac.matches("..:..:..:..:..:..")) {
                    if ("0x2".equals(flag) && InetAddress.getByName(ip).isReachable(1000)) {
                        WifiClient wifiClient = new WifiClient();
                        wifiClient.setIp(ip);
                        wifiClient.setMac(mac);
                        wifiClient.setFlag(flag);
                        wifiClients.add(wifiClient);
                    }


                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wifiClients;
    }

    public static String getIPAddress() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("ifconfig rmnet_data0", false);
        String ip = "";
        if (result.result != -1) {
            String[] split = result.successMsg.split(" ");
            if (split.length > 3) {
                boolean is = isIp(split[2]);
                ip = is ? split[2] : "";
            }
        }
        return ip;
    }

    public static String getSpeedStateStr() {
        ShellUtils.CommandResult upSpeedState = ShellUtils.execCmd("tc qdisc | grep rmnet_data0 | busybox awk '{print $9}'", false);
        String upState = "-1";
        if (upSpeedState.result != -1) {
            upState = upSpeedState.successMsg == null || upSpeedState.successMsg.isEmpty() ? "-" : upSpeedState.successMsg;
        }
        ShellUtils.CommandResult downSpeedState = ShellUtils.execCmd("tc qdisc | grep ifb0 | busybox awk '{print $9}'", false);
        String downState = "-1";
        if (downSpeedState.result != -1) {
            downState = downSpeedState.successMsg == null || downSpeedState.successMsg.isEmpty() ? "-" : downSpeedState.successMsg;
        }
        return upState.trim() + "|" + downState.trim();
    }

    /**
     * 判断是否为合法IP * @return the ip
     */
    public static boolean isIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }


    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getMacAddress(List<WifiClient> list) {
        String mac;
        int size = list.size();
        mac = size > 0 ? list.get(size - 1).getMac() : "";
        return mac;
    }

    public static String getMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }


    public static ReportData.Sim1Bean getSim1(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        ReportData.Sim1Bean sim1 = new ReportData.Sim1Bean();
        try {
            //返回设备的当前位置
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            String plmn = tm.getNetworkOperator();//默认plmn
            String imsi = tm.getSubscriberId();//默认imsi

            if (imsi == null) {
                imsi = "";
            }
            if (plmn == null) {
                plmn = "";
            }

            sim1.setPlmn(plmn);
            sim1.setImsi(imsi);
            int networkType = tm.getNetworkType();
            sim1.setNetMode(networkType);

            for (CellInfo cellInfo : cellInfoList) {
                //GSM手机信息
                if (cellInfo instanceof CellInfoGsm) {
                    CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                    int asuLevel = cellSignalStrength.getAsuLevel();
                    int dbm = cellSignalStrength.getDbm();
                    int level = cellSignalStrength.getLevel();

                    CellInfoGsm cgsm = (CellInfoGsm) cellInfo;
                    CellIdentityGsm cellIdentity = cgsm.getCellIdentity();
                    int cid = cellIdentity.getCid();
                    int lac = cellIdentity.getLac();
                    int mcc = cellIdentity.getMcc();
                    int mnc = cellIdentity.getMnc();

                    sim1.setLac(lac);
                    sim1.setCi(cid);
                    sim1.setSignal(dbm);
                    sim1.setPsc(0);

                    return sim1;
                }
                //小区LTE
                if (cellInfo instanceof CellInfoLte) {
                    CellSignalStrengthLte cellSignalStrength = ((CellInfoLte) cellInfo).getCellSignalStrength();
                    int dbm = cellSignalStrength.getDbm();
                    int asuLevel = cellSignalStrength.getAsuLevel();
                    int timingAdvance = cellSignalStrength.getTimingAdvance();
                    int level = cellSignalStrength.getLevel();

                    CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                    int mcc = cellIdentity.getMcc();
                    int mnc = cellIdentity.getMnc();
                    int ci = cellIdentity.getCi();
                    int pci = cellIdentity.getPci();
                    int tac = cellIdentity.getTac();

                    sim1.setCi(ci);
                    sim1.setLac(pci);
                    sim1.setSignal(dbm);
                    sim1.setPsc(tac);
                    return sim1;
                }
                //CDMA手机信息
                if (cellInfo instanceof CellInfoCdma) {
                    CellSignalStrengthCdma cellSignalStrength = ((CellInfoCdma) cellInfo).getCellSignalStrength();
                    int asuLevel = cellSignalStrength.getAsuLevel();
                    int cdmaDbm = cellSignalStrength.getCdmaDbm();
                    int cdmaEcio = cellSignalStrength.getCdmaEcio();
                    int cdmaLevel = cellSignalStrength.getCdmaLevel();
                    int dbm = cellSignalStrength.getDbm();
                    int evdoDbm = cellSignalStrength.getEvdoDbm();
                    int evdoEcio = cellSignalStrength.getEvdoEcio();
                    int evdoLevel = cellSignalStrength.getEvdoLevel();
                    int evdoSnr = cellSignalStrength.getEvdoSnr();
                    int level = cellSignalStrength.getLevel();
                    CellIdentityCdma cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
                    int basestationId = cellIdentity.getBasestationId();
                    int latitude = cellIdentity.getLatitude();
                    int longitude = cellIdentity.getLongitude();
                    int networkId = cellIdentity.getNetworkId();
                    int systemId = cellIdentity.getSystemId();

                    sim1.setCi(basestationId);
                    sim1.setLac(networkId);
                    sim1.setSignal(dbm);
                    sim1.setPsc(systemId);

                    return sim1;
                }
                //WCDMA手机信息
                if (cellInfo instanceof CellInfoWcdma) {
                    CellSignalStrengthWcdma cellSignalStrength = ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                    int asuLevel = cellSignalStrength.getAsuLevel();
                    int dbm = cellSignalStrength.getDbm();
                    int level = cellSignalStrength.getLevel();
                    CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                    int cid = cellIdentity.getCid();
                    int lac = cellIdentity.getLac();
                    int mcc = cellIdentity.getMcc();
                    int psc = cellIdentity.getPsc();

                    sim1.setCi(cid);
                    sim1.setLac(lac);
                    sim1.setSignal(dbm);
                    sim1.setPsc(psc);
                    return sim1;
                }
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return sim1;
    }

    public static DeviceStatus.SimCardSlot getProtoSim1(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        DeviceStatus.SimCardSlot.Builder build = DeviceStatus.SimCardSlot.newBuilder();

        try {
            //返回设备的当前位置
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            //Returns the numeric name (MCC+MNC) of current registered operator.
            String plmn = tm.getNetworkOperator();//默认plmn
            String imsi = tm.getSubscriberId();//默认imsi
            if (imsi == null) {
                imsi = "";
            }
            if (plmn == null) {
                plmn = "";
            }
            int plmnInt = 0;
            try {
                plmnInt = Integer.valueOf(plmn);
            } catch (Exception e) {
                plmnInt = 0;
            }

            build.setImsi(imsi);
            build.setPlmn(plmnInt);
            int networkType = tm.getNetworkType();

            DeviceStatus.NetworkMode networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_UNKNOWN;

            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_UNKNOWN;
                    break;
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_GPRS;
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_EDGE;
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_UMTS;
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_CDMA;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_EVDO_0;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_EVDO_A;
                    break;
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_1xRTT;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_HSDPA;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_HSUPA;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_HSPA;
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_IDEN;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_EVDO_B;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_LTE;
                    break;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_EHRPD;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_HSPAP;
                    break;
                case TelephonyManager.NETWORK_TYPE_GSM:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_GSM;
                    break;
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_TD_SCDMA;
                    break;
                case TelephonyManager.NETWORK_TYPE_IWLAN:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_IWLAN;
                    break;
                case 19:
                    networkMode = DeviceStatus.NetworkMode.NETWORK_TYPE_LTE_CA;
                    break;
            }
            build.setMode(networkMode);//network type

            for (CellInfo cellInfo : cellInfoList) {
                //GSM手机信息
                if (cellInfo instanceof CellInfoGsm) {
                    CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                    int asuLevel = cellSignalStrength.getAsuLevel();
                    int dbm = cellSignalStrength.getDbm();
                    int level = cellSignalStrength.getLevel();

                    CellInfoGsm cgsm = (CellInfoGsm) cellInfo;
                    CellIdentityGsm cellIdentity = cgsm.getCellIdentity();
                    int cid = cellIdentity.getCid();
                    int lac = cellIdentity.getLac();
                    int mcc = cellIdentity.getMcc();
                    int mnc = cellIdentity.getMnc();

                    build.setLac(lac);
                    build.setCi(cid);

                    build.setSignal(dbm);
                    build.setPsc(0);
                }
                //小区LTE
                if (cellInfo instanceof CellInfoLte) {
                    CellSignalStrengthLte cellSignalStrength = ((CellInfoLte) cellInfo).getCellSignalStrength();
                    int dbm = cellSignalStrength.getDbm();
                    int asuLevel = cellSignalStrength.getAsuLevel();
                    int timingAdvance = cellSignalStrength.getTimingAdvance();
                    int level = cellSignalStrength.getLevel();

                    CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                    int mcc = cellIdentity.getMcc();
                    int mnc = cellIdentity.getMnc();
                    int ci = cellIdentity.getCi();
                    int pci = cellIdentity.getPci();
                    int tac = cellIdentity.getTac();

                    build.setCi(ci);
                    build.setLac(pci);
                    build.setSignal(dbm);
                    build.setPsc(tac);
                }
                //CDMA手机信息
                if (cellInfo instanceof CellInfoCdma) {
                    CellSignalStrengthCdma cellSignalStrength = ((CellInfoCdma) cellInfo).getCellSignalStrength();
                    int asuLevel = cellSignalStrength.getAsuLevel();
                    int cdmaDbm = cellSignalStrength.getCdmaDbm();
                    int cdmaEcio = cellSignalStrength.getCdmaEcio();
                    int cdmaLevel = cellSignalStrength.getCdmaLevel();
                    int dbm = cellSignalStrength.getDbm();
                    int evdoDbm = cellSignalStrength.getEvdoDbm();
                    int evdoEcio = cellSignalStrength.getEvdoEcio();
                    int evdoLevel = cellSignalStrength.getEvdoLevel();
                    int evdoSnr = cellSignalStrength.getEvdoSnr();
                    int level = cellSignalStrength.getLevel();
                    CellIdentityCdma cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
                    int basestationId = cellIdentity.getBasestationId();
                    int latitude = cellIdentity.getLatitude();
                    int longitude = cellIdentity.getLongitude();
                    int networkId = cellIdentity.getNetworkId();
                    int systemId = cellIdentity.getSystemId();

                    build.setCi(basestationId);
                    build.setLac(networkId);
                    build.setSignal(dbm);
                    //build.setNetMode(ReportData.NetworkMode.CDMA);
                    build.setPsc(systemId);
                }
                //WCDMA手机信息
                if (cellInfo instanceof CellInfoWcdma) {
                    CellSignalStrengthWcdma cellSignalStrength = ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                    int asuLevel = cellSignalStrength.getAsuLevel();
                    int dbm = cellSignalStrength.getDbm();
                    int level = cellSignalStrength.getLevel();
                    CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                    int cid = cellIdentity.getCid();
                    int lac = cellIdentity.getLac();
                    int mcc = cellIdentity.getMcc();
                    int psc = cellIdentity.getPsc();

                    build.setCi(cid);
                    build.setLac(lac);
                    build.setSignal(dbm);
                    //build.setNetMode(ReportData.NetworkMode.WCDMA);
                    build.setPsc(psc);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return build.build();
    }

}
