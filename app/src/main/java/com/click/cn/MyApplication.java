package com.click.cn;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.util.Log;

import com.click.cn.base.BaseApplication;
import com.click.cn.util.AppUtil;
import com.click.cn.util.BaseAppContextRef;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.geen.greenlibrary.gen.DaoMaster;
import org.geen.greenlibrary.gen.DaoSession;
import org.green.greenlibrary.DbMange;


public class MyApplication extends BaseApplication {

   private final String TAG=MyApplication.class.getSimpleName();
    private static MyApplication appContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        BaseAppContextRef.attach(this);
        initDb();
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);
        Log.i(TAG, "当前进程名称：" + AppUtil.getCurrentProcessName(this));
        appContext=this;
        addListens();

    }

    private void initDb() {

        DbMange.getInstance().initDb(this);
    }






    private void addListens() {

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Log.i(TAG, "onActivityCreated()--->" + activity.getClass().getSimpleName());

            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.i(TAG, "onActivityStarted()--->" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.i(TAG, "onActivityResumed()--->" + activity.getClass().getSimpleName());

            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.i(TAG, "onActivityPaused()--->" + activity.getClass().getSimpleName());

            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.i(TAG, "onActivityStopped()--->" + activity.getClass().getSimpleName());

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Log.i(TAG, "onActivitySaveInstanceState()--->" + activity.getClass().getSimpleName());

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.i(TAG, "onActivityDestroyed()--->" + activity.getClass().getSimpleName());

            }
        });

    }


    public static MyApplication getMainAppContext() {
        return appContext;
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.i(TAG, "onTerminate");
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.i(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }


}
