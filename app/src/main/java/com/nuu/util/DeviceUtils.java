package com.nuu.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class DeviceUtils {

    private static final String TAG = DeviceUtils.class.getSimpleName();

    private DeviceUtils() {
        throw new AssertionError();
    }


    /**
     * 获取设备的mac地址
     *
     * @param context
     * @return
     */
    //Android 6.0 : Access to mac address from WifiManager forbidden
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String defaultMacAddress = "00:90:4c:11:22:33";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getMacAddress(Context context) {
        String mac = null;
        WifiManager wifiMan = (WifiManager) context.getApplicationContext()
                .getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiMan.getConnectionInfo();

        if (Build.VERSION.SDK_INT < 23) {
            mac = info.getMacAddress();
        }

        if (isDefault(mac)) {
            try {
                mac = getAdressMacByInterface();
                if (isDefault(mac)) {
                    mac = getAddressMacByFile(wifiMan);
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        }
        if (isDefault(mac) && Build.VERSION.SDK_INT <= 23) {
            mac = getLocalMacAddressFromIp();
        }

        if (isDefault(mac)) {
            mac = "";
            try {
                Toast.makeText(context, "获取wifi mac地址失败！,请连接wifi重试", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return mac;
    }

    private static boolean isDefault(String mac) {
        return TextUtils.isEmpty(mac)
                || mac.equals(marshmallowMacAddress)
                || mac.equals(defaultMacAddress);
    }


    private static String getAdressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }

                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String res;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File file = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(file);
        res = StreamUtils.readStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return res;
    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    private static String getLocalMacAddressFromIp() {
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    sb.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                sb.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = sb.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().contains(":"))
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }


    /**
     * 获取设备信息
     *
     * @param context
     * @return
     */
    public static String getDeviceSuperInfo(Context context) {
        String s = "Debug-infos:";
        s += "\n Android Version: " + getOsVersion();
        s += "\n BRAND: " + Build.BRAND;
        s += "\n Model: " + Build.MODEL;
        s += "\n MANUFACTURER: " + Build.MANUFACTURER;
        s += "\n SERIAL: " + Build.SERIAL;
        s += "\n MAC: " + getMacAddress(context);
        s += "\n IMEI: " + getIMEI(context);
        s += "\n IMSI: " + getIMSI(context);
        s += "\n AndroidID: " + getAndroidId(context);
        s += "\n ICCID: " + getICCID(context);

        Log.i(TAG + " | Device Info > ", s);

        return s;
    }//en

    /**
     * 获取系统版本好
     */
    public static String getOsVersion() {
        String osVersion = "unknown";
        switch (Build.VERSION.SDK_INT) {
            case 14:
                osVersion = "android4.0";
                break;
            case 15:
                osVersion = "android4.0.3";
                break;
            case 16:
                osVersion = "android4.1";
                break;
            case 17:
                osVersion = "android4.2";
                break;
            case 18:
                osVersion = "android4.3";
                break;
            case 19:
                osVersion = "android4.4";
                break;
            case 20:
                osVersion = "android4.4W";
                break;
            case 21:
                osVersion = "android5.0";
                break;
            case 22:
                osVersion = "android5.1";
                break;
            case 23:
                osVersion = "android6.0";
                break;
            case 24:
                osVersion = "android7.0";
                break;
            case 25:
                osVersion = "android7.1";
                break;
            case 26:
                osVersion = "android8.0";
                break;
            default:
                osVersion = "unknown";
                break;
        }
        return osVersion;
    }


    /**
     * 获取手机IMSI
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        String imsi = null;
        try {
            TelephonyManager phoneManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imsi = phoneManager.getSubscriberId();
            Log.v(TAG, imsi);
        } catch (Exception e) {
            Log.e(TAG, "getIMSI error!");
            imsi = "";
        }

        if (imsi == null) {
            imsi = "";
        }
        return imsi;
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String imei;
        try {
            TelephonyManager phoneManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = phoneManager.getDeviceId();
        } catch (Exception e) {
            Log.e(TAG, "getIMEI error!");
            imei = "";
        }
        if (TextUtils.isEmpty(imei)) {
            imei = getAndroidId(context);
        }
        return imei;
    }

    /**
     * 获取android id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * 获取iccid SIM卡序列号
     *
     * @param context
     * @return
     */
    public static String getICCID(Context context) {
        String iccid = "";
        try {
            TelephonyManager phoneManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            iccid = phoneManager.getSimSerialNumber();
        } catch (Exception e) {
            Log.e(TAG, "getIMEI error!");
            iccid = "";
        }
        if (iccid == null) {
            iccid = "";
        }
        return iccid;
    }

    public static String getModel() {
        return Build.BRAND + " " + Build.MODEL;
    }


    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }


    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getLocalIpAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
