package com.nuu.report;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nuu.service.NuuService;


public class ReportTaskManager {

    private long period = 2 * 60 * 1000;//闹钟执行任务的时间间隔
    private AlarmManager am;
    private PendingIntent pendingIntent;

    public ReportTaskManager(Context context) {
        init(context);
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    private void init(Context context) {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NuuService.class);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    }


    public void updateReportTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + period, pendingIntent);
        }
    }
}