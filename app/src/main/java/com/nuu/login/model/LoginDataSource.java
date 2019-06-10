package com.nuu.login.model;

import android.content.ContentValues;
import android.text.TextUtils;

import com.nuu.config.AppConfig;
import com.nuu.entity.DetailRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.util.DeviceInfo;
import com.nuu.util.GsonUtil;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public void login(final String username, String password,
                      final IPostListener listener) {
        reqLogin(username, password, listener);
    }

    public void logout() {
        // TODO: revoke authentication
    }


    private void reqLogin(String username, String auth_code, IPostListener listener) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_device_status");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", username);
        params.put("auth_code", auth_code);
        params.put("device_sn", DeviceInfo.getDeviceSN());

        OkHttpConnector.httpPost(url, params, listener);

    }
}
