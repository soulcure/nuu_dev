package com.nuu.config;

import android.os.Environment;

import com.nuu.entity.ReportData;
import com.nuu.util.FileUtils;
import com.nuu.util.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileConfig {

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
        String dateStr = TimeUtils.getTime(System.currentTimeMillis());
        String filePath = filePrefix + dateStr + fileSubfix;
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

        String dateStr = TimeUtils.getTime(System.currentTimeMillis());
        String filePath = filePrefix + dateStr + fileSubfix;
        FileUtils.writeFile(filePath, strList, true);
    }

}
