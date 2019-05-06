package com.nuu.config;

import android.os.Environment;

import java.io.File;

public class FileConfig {

    public static final String NuuPath = "/nuu";
    public static final String ApkPaths = NuuPath + "/Apk/";
    public static final String PicPaths = NuuPath + "/Pic/";
    public static final String VideoPaths = NuuPath + "/Video/";
    public static final String LogPaths = NuuPath + "/Log/";
    public static final String CrashPaths = NuuPath + "/Crash/";
    public static final String AudioPaths = NuuPath + "/Audio/";
    public static final String FilePaths = NuuPath + "/File/";
    public static final String SDKPaths = NuuPath + "/Sdk/";
    public static final String InfoPaths = NuuPath + "/Info/";
    public static final String VcardPaths = NuuPath + "/vcard/";
    public static final String UserPaths = NuuPath + "/User/";
    public static final String HeaderPaths = NuuPath + "/Head/";
    public static final String HeaderLargePaths = NuuPath + "/HeadLarge/";
    public static final String GlideCachePaths = NuuPath + "/GlideCache/";
    public static final String ThumbImagePaths = NuuPath + "/ThumbImage";

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

    /**
     * HuXin 全路径
     *
     * @return
     */
    public static String getHuXinCachePath() {
        if (!isCanUseSD()) {
            return null;
        }
        String path = Environment.getExternalStorageDirectory().getPath() + NuuPath;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * 文件下载路径
     *
     * @return
     * @author
     * @date 2017年1月17日
     */
    public static String getFileDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + FilePaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * Apk下载路径
     *
     * @return
     * @author
     * @date 2017年1月17日
     */
    public static String getApkDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + ApkPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * 视频文件下载路径
     *
     * @return
     * @author
     * @date 2017年1月17日
     */
    public static String getVideoDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + VideoPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * 获取音频路径
     *
     * @return
     */
    public static String getAudioDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + AudioPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }


    /**
     * 图片下载路径
     *
     * @return
     * @author
     * @date 2017年1月17日
     */
    public static String getPicDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + PicPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }


    /**
     * 异常数据路径
     *
     * @return
     * @author
     * @date 2016年8月19日
     */
    public static String getExceptionPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + CrashPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }


    /**
     * 获取日志文件路径
     *
     * @return
     */
    public static String getLogPaths() {
        String path = Environment.getExternalStorageDirectory().getPath() + LogPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }


    /**
     * 获取SDK文件信息路径
     *
     * @return
     */
    public static String getSdkPaths() {
        String path = Environment.getExternalStorageDirectory().getPath() + SDKPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }


    /**
     * 获取设备信息
     *
     * @return
     * @author
     * @date 2017年1月17日
     */
    public static String getInfoPaths() {
        String path = Environment.getExternalStorageDirectory().getPath() + InfoPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * 名片文件路径
     */
    public static String getVcardPaths() {
        String path = Environment.getExternalStorageDirectory().getPath() + VcardPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * 本手机用户头像图片下载路径
     *
     * @return
     * @author
     * @date 2017年1月17日
     */
    public static String getHeaderPicPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + HeaderPaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * 手机Glide缓存图片SD卡路径
     *
     * @return
     * @author
     * @date 2017年1月17日
     */
    public static String getGlideCachePath() {
        String path = Environment.getExternalStorageDirectory().getPath() + GlideCachePaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * 非原图发送图片时，缩略图路径
     *
     * @return
     */
    public static String getThumbImagePaths() {
        String path = Environment.getExternalStorageDirectory().getPath() + ThumbImagePaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

    /**
     * 头像大图路径
     *
     * @return
     */
    public static String getHeaderLargePaths() {
        String path = Environment.getExternalStorageDirectory().getPath() + HeaderLargePaths;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath();
    }

}
