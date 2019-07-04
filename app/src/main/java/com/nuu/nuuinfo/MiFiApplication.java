package com.nuu.nuuinfo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nuu.MiFiManager;
import com.nuu.config.AppConfig;
import com.nuu.db.dao.DaoMaster;
import com.nuu.db.dao.DaoSession;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.util.AppUtils;


public class MiFiApplication extends MultiDexApplication {

    private static final String TAG = MiFiApplication.class.getSimpleName();

    private String token;
    private String uuid;
    private long expTime;
    private DaoSession daoSession;

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
        init();
    }

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "nuu.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }


    public DaoSession getDaoSession() {
        return daoSession;
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


    private void init() {
        token = AppUtils.getStringSharedPreferences(this, "token", "");
        uuid = AppUtils.getStringSharedPreferences(this, "uuid", "");
        expTime = AppUtils.getLongSharedPreferences(this, "expTime", 0);

        long curTime = System.currentTimeMillis() / 1000;
        if (expTime < curTime) {
            Log.d(TAG, "token is expired");
            token = "";
            uuid = "";
            expTime = 0;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        AppUtils.setStringSharedPreferences(this, "token", token);
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        AppUtils.setStringSharedPreferences(this, "uuid", uuid);
        this.uuid = uuid;
    }

    public long getExpTime() {
        return expTime;
    }

    public void setExpTime(long expTime) {
        AppUtils.setLongSharedPreferences(this, "expTime", expTime);
        this.expTime = expTime;
    }
}

