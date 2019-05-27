package com.nuu.httpserver;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.nuu.MiFiManager;
import com.nuu.config.AppConfig;
import com.nuu.http.OkHttpConnector;
import com.nuu.report.ConfigManager;
import com.nuu.util.ShellUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class SimpleServer extends NanoHTTPD {

    public static final String TAG = SimpleServer.class.getSimpleName();
    private static final String REQUEST_ROOT = "/";
    private static final Map<String, String> router = new HashMap<>();
    private int serverPort;
    private Context mContext;

    public SimpleServer(Context context, int port) {
        super(port);
        this.mContext = context;
        this.serverPort = port;
        registRouter();

    }

    public void startServer() {
        try {
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
            return;

        }
        Log.d(TAG, "Running! Point your browsers to http://localhost:" + serverPort);

    }

    //对于请求根目录的，返回首页
    public Response responseRootPage(IHTTPSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPER html><html><body>");
        builder.append("Hello");
        builder.append("</body></html>\n");
        return newFixedLengthResponse(String.valueOf(builder));
    }


    //页面不存在，或者文件不存在时
    public Response response404(IHTTPSession session, String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html>body>");
        builder.append("Sorry,Can't Found" + url + " !");
        builder.append("</body></html>\n");
        return newFixedLengthResponse(builder.toString());
    }

    private void registRouter() {
        String routerStr = ConfigManager.instance().getCurConfig().getRouter();
        router.put("/info", "/info");
        router.put("/index", "/index");
        router.put("/shutdown", "/shutdown");
        router.put("/reboot", "/reboot");
        router.put("/setWifiAp", "/setWifiAp");
        router.put("/setWifiApPrimary", "/setWifiApPrimary");
        router.put("/transfer", "/transfer");
        router.put(routerStr, routerStr);
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        if (REQUEST_ROOT.equals(session.getUri()) || session.getUri().equals("")) {
            return responseRootPage(session);
        }

        String uri = session.getUri();
        if (!router.containsKey(uri)) {
            return response404(session, uri);
        }
        Log.d(TAG, "NanoHTTPD serve uri:" + uri);

        String routerStr = ConfigManager.instance().getCurConfig().getRouter();
        Log.d(TAG, "NanoHTTPD serve routerStr:" + routerStr);

        if (uri.equals("/info")) {
            return getCurInfo();
        } else if (uri.equals("/index")) {
            return newFixedLengthResponse("========/index=======");
        } else if (uri.equals("/shutdown")) {
            String shutdownStr = shutdownDevice();
            return newFixedLengthResponse(shutdownStr);
        } else if (uri.equals("/reboot")) {
            String rebootStr = rebootDevice();
            return newFixedLengthResponse(rebootStr);
        } else if (uri.equals("/setWifiAp")) {
            Map<String, String> parms = session.getParms();
            final String ssid = parms.get("ssid");
            final String password = parms.get("password");

            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setWifiAp(ssid, password);
                }
            }.start();
            return newFixedLengthResponse("---setWifiAp--");
        } else if (uri.equals("/setWifiApPrimary")) {
            Map<String, String> params = session.getParms();
            final String ssidp = params.get("ssid");
            final String passwordp = params.get("password");

            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setWifiAp(ssidp, passwordp);
                }
            }.start();
            return newFixedLengthResponse("---setWifiApPrimary--");
        } else if (uri.equals(routerStr)) {
            String url = AppConfig.getHost();

            Method method = session.getMethod();
            if (Method.GET.equals(method)) {
                // or you can access the GET request's parameters
                Map<String, String> getParam = session.getParms();
                String response = OkHttpConnector.httpGet(url, null, getParam);
                return newFixedLengthResponse(response);
            } else if (Method.POST.equals(method) || Method.PUT.equals(method)) {
                Map<String, String> files = new HashMap<>();
                try {
                    session.parseBody(files);
                } catch (IOException ioe) {
                    return newFixedLengthResponse("Internal Error IO Exception: " + ioe.getMessage());
                } catch (ResponseException re) {
                    return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
                }

                Map<String, String> postParam = session.getParms();
                String body = session.getQueryParameterString();  // get the POST body
                // or you can access the POST request's parameters
                String response = OkHttpConnector.httpPost(url, null, postParam, body);
                return newFixedLengthResponse(response);
            }
        }

        return newFixedLengthResponse("=======default========");
    }

    private Response getCurInfo() {
        String json = MiFiManager.instance().getDeviceInfo();
        return newFixedLengthResponse(Response.Status.OK, "application/json", json);
    }

    private String shutdownDevice() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("reboot -p", false);
        return "{'code':200,'msg':'shutdown'}";
    }

    private String rebootDevice() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("reboot", false);
        return "{'code':200,'msg':'reboot'}";
    }


    private void setWifiAp(String ssid, String password) {
        try {
            Context app = mContext.getApplicationContext();
            WifiManager wifiManager = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
            java.lang.reflect.Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifiManager);

            if (ssid != null) {
                String realSsid = ssid.trim();
                if (!realSsid.isEmpty()) {
                    wifiConfig.SSID = realSsid;
                }
            }
            if (password != null) {
                String realPwd = password.trim();
                if (!realPwd.isEmpty() && realPwd.length() >= 8) {
                    wifiConfig.preSharedKey = realPwd;
                }
            }
//            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA2_PSK);//WPA2_PSK====4
            wifiConfig.allowedKeyManagement.set(4);
            wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

            java.lang.reflect.Method setConfigMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setConfigMethod.invoke(wifiManager, wifiConfig);

            java.lang.reflect.Method setWifiApEnabledMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            setWifiApEnabledMethod.invoke(wifiManager, wifiConfig, false);
            setWifiApEnabledMethod.invoke(wifiManager, wifiConfig, true);
//            wifiManager.setWifiApEnabled(wifiManager, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
