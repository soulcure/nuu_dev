package com.nuu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.nuu.MiFiManager;
import com.nuu.service.NuuService;


public class NuuReceiver extends BroadcastReceiver {
    private static final String TAG = NuuReceiver.class.getSimpleName();

    public static final String ACTION_START_SERVICE = "huxin.intent.action.START_SERVER";
    public static final String SHOW_FLOAT_VIEW = "huxin.intent.action.SHOW_FLOAT_VIEW";
    public static final String HIDE_FLOAT_VIEW = "huxin.intent.action.HIDE_FLOAT_VIEW";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (TextUtils.isEmpty(action)) {
            return;
        }

        if (action.equals(ACTION_START_SERVICE)
                || action.equals(Intent.ACTION_BOOT_COMPLETED)
                || action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            MiFiManager.instance().init(context);

            Intent in = new Intent(context, NuuService.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.setAction(NuuService.BOOT_NUU_SERVICE);
            context.startService(in);//启动服务

        } else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            Intent in = new Intent(context, NuuService.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(in);//启动服务

        }
    }

}