package com.click.cn.constants;

import com.click.cn.MyApplication;

public class Constans {


    /** 默认下载图片文件地址. */
    public static String DOWNLOAD_IMAGE_DIR= "images";
    public static String DOWNLOAD_HEAD_DIR= "heads";

    /** 默认下载文件地址. */
    public static String DOWNLOAD_FILE_DIR= "files";

    /** APP缓存目录. */
    public static String CACHE_DIR= "cache";

    /** DB目录. */
    public static String DB_DIR= "db";

    /** 日志目录. */
    public static String LOG_DIR= "log";

    public static final String PATH_DOWNLOAD_IMAGE_DIR = MyApplication.getMainAppContext().getExternalFilesDir(DOWNLOAD_IMAGE_DIR).getAbsolutePath() ;
    public static final String PATH_DOWNLOAD_HEAD_DIR = MyApplication.getMainAppContext().getExternalFilesDir(DOWNLOAD_HEAD_DIR).getAbsolutePath() ;
    public static final String PATH_DOWNLOAD_FILE_DIR = MyApplication.getMainAppContext().getExternalFilesDir(DOWNLOAD_FILE_DIR).getAbsolutePath() ;







}
