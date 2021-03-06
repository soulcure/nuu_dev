package com.nuu.httpserver;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.nuu.MiFiManager;
import com.nuu.config.AppConfig;
import com.nuu.http.OkHttpConnector;
import com.nuu.report.ConfigManager;
import com.nuu.util.AppUtils;
import com.nuu.util.DeviceInfo;
import com.nuu.util.DeviceUtils;
import com.nuu.util.ShellUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


public class SimpleServerWeb extends NanoHTTPD {

    public static final String TAG = SimpleServer.class.getSimpleName();
    private Context mContext;

    public SimpleServerWeb(Context context, int port) {
        super(port);
        this.mContext = context;

        String ip = DeviceUtils.getLocalIpAddress(true);
        Log.d(TAG, "NanoHTTPD listener to " + ip + ":" + port);
    }


    public void startServer() {
        try {
            start();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Log.d(TAG, "NanoHTTPD serve uri:" + uri);

        try {

            if (uri.equals("/info")) {   //begin for api
                return getCurInfo();
            } else if (uri.equals("/index")) {
                return newFixedLengthResponse("========/index=======");
            } else if (uri.equals("/shutdown")) {
                return shutdownDevice();
            } else if (uri.equals("/reboot")) {
                return rebootDevice();
            } else if (uri.equals("/setWifiAp")) {
                Map<String, List<String>> params = session.getParameters();
                final String ssid = params.get("ssid").get(0);
                final String password = params.get("password").get(0);
                DeviceInfo.setWifiAp(mContext, ssid, password);

                return newFixedLengthResponse("---setWifiAp--");
            } else if (uri.equals("/setWifiApPrimary")) {
                Map<String, List<String>> params = session.getParameters();
                final String ssid = params.get("ssid").get(0);
                final String password = params.get("password").get(0);
                DeviceInfo.setWifiAp(mContext, ssid, password);

                return newFixedLengthResponse("---setWifiApPrimary--");
            } else if (uri.equals("/transfer")) {

                Method method = session.getMethod();
                if (Method.GET.equals(method)) {
                    try {
                        // or you can access the GET request's parameters

                        Map<String, String> getParam = session.getParms();
                        String host = getParam.get("host");
                        String path = getParam.get("url");
                        getParam.remove("host");
                        getParam.remove("url");

                        getParam.put("hwid", DeviceInfo.getDeviceId());
                        getParam.put("device_sn", DeviceInfo.getDeviceSN());

                        if (TextUtils.isEmpty(host)) {
                            host = ConfigManager.instance().getCurConfig().getRouterHost();
                        }

                        if (TextUtils.isEmpty(host)) {
                            host = AppConfig.getRouterHost();
                        }

                        if (TextUtils.isEmpty(path)) {
                            path = ConfigManager.instance().getCurConfig().getRouterPath();
                        }

                        if (TextUtils.isEmpty(path)) {
                            path = AppConfig.getRouterPath();
                        }

                        String url = host + path;

                        okhttp3.Response response = OkHttpConnector.httpGet1(url, null, getParam);
                        if (response != null) {
                            String json;
                            if (response.isSuccessful()) {
                                json = response.body().string();
                            } else {
                                json = response.toString();
                            }
                            int code = response.code();
                            Response.Status status = Response.Status.lookup(code);

                            Response res = newFixedLengthResponse(status, "application/json", json);
                            res.addHeader("Access-Control-Allow-Origin", "*");
                            return res;
                        }

                    } catch (IOException e) {
                        return newFixedLengthResponse("Internal Error IO Exception: " + e.getMessage());
                    } catch (NullPointerException e) {
                        return newFixedLengthResponse("Internal  NullPointerException: " + e.getMessage());
                    } catch (Exception e) {
                        return newFixedLengthResponse("Internal  Exception: " + e.getMessage());
                    }

                } else if (Method.POST.equals(method) || Method.PUT.equals(method)) {
                    Map<String, String> files = new HashMap<>();
                    try {
                        session.parseBody(files);

                        Map<String, String> postParam = session.getParms();
                        String host = postParam.get("host");
                        String path = postParam.get("url");
                        postParam.remove("host");
                        postParam.remove("url");

                        String deviceId = DeviceInfo.getDeviceId();
                        String token = AppUtils.md5("@com.nuu@" + deviceId);

                        Log.d(TAG,"token: "+token);

                        postParam.put("token", token);
                        postParam.put("hwid", deviceId);
                        postParam.put("device_sn", DeviceInfo.getDeviceSN());

                        if (TextUtils.isEmpty(host)) {
                            host = ConfigManager.instance().getCurConfig().getRouterHost();
                        }

                        if (TextUtils.isEmpty(host)) {
                            host = AppConfig.getRouterHost();
                        }

                        if (TextUtils.isEmpty(path)) {
                            path = ConfigManager.instance().getCurConfig().getRouterPath();
                        }

                        if (TextUtils.isEmpty(path)) {
                            path = AppConfig.getRouterPath();
                        }


                        String url = host + path;

                        String body = session.getQueryParameterString();  // get the POST body
                        // or you can access the POST request's parameters
                        okhttp3.Response response = OkHttpConnector.httpPost1(url, null, postParam, body);

                        if (response != null) {
                            String json;
                            if (response.isSuccessful()) {
                                json = response.body().string();
                            } else {
                                json = response.toString();
                            }

                            int code = response.code();
                            Response.Status status = Response.Status.lookup(code);

                            Response res = newFixedLengthResponse(status, "application/json", json);
                            res.addHeader("Access-Control-Allow-Origin", "*");
                            return res;
                        }

                    } catch (IOException e) {
                        return newFixedLengthResponse("Internal Error IO Exception: " + e.getMessage());
                    } catch (ResponseException e) {
                        return newFixedLengthResponse(e.getStatus(), MIME_PLAINTEXT, e.getMessage());
                    } catch (NullPointerException e) {
                        return newFixedLengthResponse("Internal  NullPointerException: " + e.getMessage());
                    } catch (Exception e) {
                        return newFixedLengthResponse("Internal  Exception: " + e.getMessage());
                    }
                }
            }

            if (uri.equals("/")) {
                uri = "index.html";
            }

            String filename = uri;

            if (uri.substring(0, 1).equals("/")) { //begin for web
                filename = uri.substring(1);
            }
            InputStream fis = mContext.getResources().getAssets().open(filename);

            if (uri.endsWith(".ico")) {
                return newChunkedResponse(Response.Status.OK, "image/x-icon", fis);
            } else if (uri.endsWith(".png") || uri.endsWith(".PNG")) {
                return newChunkedResponse(Response.Status.OK, "image/png", fis);
            } else if (uri.endsWith(".jpg") || uri.endsWith(".JPG") || uri.endsWith(".jpeg") || uri.endsWith(".JPEG")) {
                return newChunkedResponse(Response.Status.OK, "image/jpeg", fis);
            } else if (uri.endsWith(".js")) {
                return newChunkedResponse(Response.Status.OK, "application/javascript", fis);
            } else if (uri.endsWith(".css")) {
                return newChunkedResponse(Response.Status.OK, "text/css", fis);
            } else if (uri.endsWith(".html") || uri.endsWith(".htm")) {
                return newChunkedResponse(Response.Status.OK, "text/html", fis);
            } else if (uri.endsWith(".map")) {
                return newChunkedResponse(Response.Status.OK, "application/json", fis);
            }


        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", e.getMessage());
        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", uri);
    }

    private Response getCurInfo() {
        String json = MiFiManager.instance().getDeviceInfo();

        Response res = newFixedLengthResponse(Response.Status.OK, "application/json", json);
        res.addHeader("Access-Control-Allow-Origin", "*");
        return res;
    }

    private Response shutdownDevice() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("reboot -p", false);
        return newFixedLengthResponse(Response.Status.OK, "text/plain", result.toString());
    }

    private Response rebootDevice() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("reboot", false);
        return newFixedLengthResponse(Response.Status.OK, "text/plain", result.toString());
    }


}
