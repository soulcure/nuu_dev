package com.nuu.config;

import android.os.Environment;

import com.nuu.entity.ReportData;
import com.nuu.report.ConfigManager;
import com.nuu.util.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FileConfig {

    private static final String TAG = "TcpClient";

    public static final String NuuPath = "/nuu";
    public static final String ApkPaths = NuuPath + "/Apk/";
    public static final String LogPaths = NuuPath + "/Log/";
    public static final String FilePaths = NuuPath + "/File/";
    public static final String InfoPaths = NuuPath + "/Info/";

    private static final String filePrefix = "log";
    private static final String fileSubfix = ".txt";

    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getFileDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + FilePaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    public static String getApkDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + ApkPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }


    public static String getLogPaths() {
        String path = Environment.getExternalStorageDirectory().getPath() + LogPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }


    public static String getInfoPaths() {
        String path = Environment.getExternalStorageDirectory().getPath() + InfoPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }


    public static void writeFile(ReportData reportData) {
        String content = reportData.toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm", Locale.CHINA);
        String dateStr = sdf.format(new Date(System.currentTimeMillis()));

        String path = ConfigManager.instance().getCurConfig().getReportStorePath();
        String filePath = path + "/" + filePrefix + dateStr + fileSubfix;
        FileUtils.writeFile(filePath, content, true);
    }


    public static void writeFile(List<ReportData> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        List<String> strList = new ArrayList<>();

        for (ReportData item : list) {
            strList.add(item.toString());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd", Locale.CHINA);
        String dateStr = sdf.format(new Date(System.currentTimeMillis()));

        String path = ConfigManager.instance().getCurConfig().getReportStorePath();
        String filePath = path + "/" + filePrefix + dateStr + fileSubfix;
        FileUtils.writeFile(filePath, strList, true);
    }


    public static void delLogFileMonthBefore() {
        String dir = ConfigManager.instance().getCurConfig().getReportStorePath();
        File file = new File(dir);
        if (file.isDirectory()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MONTH, -1); //保留1个月
            //calendar.add(Calendar.DATE, -3); //保留3天

            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd", Locale.CHINA);
            String dateStr = sdf.format(date);
            String filePath = dir + "/" + filePrefix + dateStr + fileSubfix;

            for (File f : file.listFiles()) {
                if (f.isFile() && f.getAbsolutePath().compareTo(filePath) < 0) {
                    f.delete();
                }
            }
        }

    }
}
