package com.nuu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


import com.nuu.MiFiManager;
import com.nuu.service.NuuService;
import com.nuu.util.AppUtils;


public class NuuReceiver extends BroadcastReceiver {

    private static final String TAG = "TcpClient";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (TextUtils.isEmpty(action)) {
            return;
        }

        Log.d(TAG, "NuuReceiver onReceive:" + action);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            MiFiManager.instance().init(context);

            Intent in = new Intent(context, NuuService.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.setAction(NuuService.NUU_CHECK_UPDATE);
            context.startService(in);//启动服务

        } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
                || action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            if (AppUtils.isNetworkConnected(context)) {
                MiFiManager.instance().init(context);

                Intent in = new Intent(context, NuuService.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.setAction(NuuService.BOOT_NUU_SERVICE);
                context.startService(in);//启动服务
            }

        } else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
            try {
                String scheme = intent.getData().getSchemeSpecificPart();
                if (scheme != null && scheme.equals(context.getPackageName())) { //Restart services
                    MiFiManager.instance().init(context);

                    Intent in = new Intent(context, NuuService.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.setAction(NuuService.BOOT_NUU_SERVICE);
                    context.startService(in);//启动服务
                }
            } catch (NullPointerException e) {
                Log.e(TAG, e.getMessage());
            }

        }
    }

}