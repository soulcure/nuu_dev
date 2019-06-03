package com.nuu.report;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nuu.service.NuuService;


public class ReportTaskManager {

    private long sendPeriod;//闹钟执行任务的时间间隔
    private long obtainPeriod;//闹钟执行任务的时间间隔

    private AlarmManager am;
    private PendingIntent sendPendingIntent;
    private PendingIntent obtainPendingIntent;

    public ReportTaskManager(Context context, long sendPeriod, long obtainPeriod) {
        init(context);
        this.sendPeriod = sendPeriod;
        this.obtainPeriod = obtainPeriod;
    }

    public void setSendPeriod(long sendPeriod) {
        this.sendPeriod = sendPeriod;
    }

    public void setObtainPeriod(long obtainPeriod) {
        this.obtainPeriod = obtainPeriod;
    }


    private void init(Context context) {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent sendIntent = new Intent(context, NuuService.class);
        sendIntent.setAction(NuuService.REPORT_DEVICE_AM);
        sendPendingIntent = PendingIntent.getService(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent obtainIntent = new Intent(context, NuuService.class);
        obtainIntent.setAction(NuuService.OBTAIN_DEVICE_AM);
        obtainPendingIntent = PendingIntent.getService(context, 1, obtainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public void updateSendReportTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + sendPeriod, sendPendingIntent);
        }
    }


    public void cleanSendReportTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.cancel(sendPendingIntent);
        }
    }


    public void updateObtainReportTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + obtainPeriod, obtainPendingIntent);
        }
    }


    public void cleanObtainReportTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.cancel(sendPendingIntent);
        }
    }
}