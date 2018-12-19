package com.click.cn.util;

import android.util.Log;

public class LogUtil {
    private static String Tag = "MyApplication";

    public LogUtil() {
    }

    public static void setTag(String tag) {
        Tag = tag;
    }

    public static void i(String msg) {
        Log.i(Tag, appendMsgAndInfo(msg, getCurrentInfo()));
    }

    public static void d(String msg) {
        Log.d(Tag, appendMsgAndInfo(msg, getCurrentInfo()));
    }

    public static void e(String msg, Throwable t) {
        Log.e(Tag, appendMsgAndInfo(msg, getCurrentInfo()), t);
    }

    private static String getCurrentInfo() {
        StackTraceElement[] eles = Thread.currentThread().getStackTrace();
        StackTraceElement targetEle = eles[5];
        String info = "(" + targetEle.getClassName() + "." + targetEle.getMethodName() + ":" + targetEle.getLineNumber() + ")";
        return info;
    }

    private static String appendMsgAndInfo(String msg, String info) {
        return msg + " " + getCurrentInfo();
    }
}
