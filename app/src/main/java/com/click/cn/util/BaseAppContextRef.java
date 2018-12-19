package com.click.cn.util;

import android.content.Context;

/**
 * 主进程的Application的因引用
 */
public class BaseAppContextRef {

    private static Context appContext;

    public static void attach(Context context) {
        appContext = context;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
