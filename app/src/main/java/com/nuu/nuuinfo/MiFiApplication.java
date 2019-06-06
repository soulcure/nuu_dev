package com.nuu.nuuinfo;

import android.content.ContentValues;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nuu.MiFiManager;
import com.nuu.config.AppConfig;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;


public class MiFiApplication extends MultiDexApplication {

    private static final String TAG = MiFiApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化
        MiFiManager.instance().init(this, new MiFiManager.InitListener() {
            @Override
            public void success() {
                Log.d(TAG, "初始化成功");
            }

            @Override
            public void fail() {
                Log.e(TAG, "初始化失败");
            }
        });
    }

    /**
     * 查询购买的流量包
     */
    public void reqPurchasedPackage(IPostListener listener) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        OkHttpConnector.httpPost(url, params, listener);


    }

}

