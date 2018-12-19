package com.click.cn.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


import com.click.cn.R;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vito-xa49 on 2017/9/13.
 */

public class AppUtil {
    private static final String TAG = AppUtil.class.getSimpleName();

    /**
     * 重启应用的Acriivty
     */
    public static void restartAppByActivity(@NonNull Context context) {
        Context appContext = context.getApplicationContext();
        Intent i = appContext.getPackageManager().getLaunchIntentForPackage(appContext.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//modify code by zyc 2015/11/16 Note:Change the boot mode
        appContext.startActivity(i);
    }

    public static void restartApp(Context context, Class clazz) {
        Context appContext = context.getApplicationContext();
//        System.exit(0);
        String command = "kill -9 " + Process.myPid();
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Intent intent2 = new Intent(appContext, clazz);
            @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(appContext, 0, intent2, Intent.FLAG_ACTIVITY_NEW_TASK);
            //退出程序
            AlarmManager mgr = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1, restartIntent);
        }
    }

    public static void kill() {
        String command = "kill -9 " + Process.myPid();
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 安装应用(fixme: 需要兼容adroid 8.0 安全权限设置)
     * <p>
     * 安装包不完整---解析异常
     *
     * @param context
     * @param apkPath
     */
//    public static void installApk(Context context, String apkPath) {
//        Uri uri = FileUtils.getFileUri(context, apkPath);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        context.startActivity(intent);
//        Process.killProcess(Process.myPid()); // 提示完成
//    }


    /**
     * 判断指定的intent能否被当前手机解析
     *
     * @param intent
     * @param context
     * @return
     */
    public static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

//    @TargetApi(23)
//    public static boolean isIntentAvailable(Context context, Intent intent){
//        if (intent == null) {
//            return false;
//        }
//        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_ALL).size() > 0;
//    }


    /**
     * 获取虚拟导航栏的高度
     *
     * @param context
     * @return
     */
    public static float getVirtualNavigationBarHeight(@NonNull Activity context) {

        // 1.获取不含虚拟键的高度
        float heightWithOutKey = context.getWindowManager().getDefaultDisplay().getHeight();

        // 2.获取包含虚拟键的屏幕高度
//        context.getWindowManager().getDefaultDisplay().getRealMetrics(new DisplayMetrics());
        float heightWithKey = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            Class c;
            try {
                c = Class.forName("android.view.Display");
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, metrics);
                heightWithKey = metrics.heightPixels;
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            heightWithKey = 0;
        }
        // 3.---
        float resultHeight = heightWithKey - heightWithOutKey;
        if (resultHeight <= 0) {
            resultHeight = 0;
        }
        return resultHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static float getStatusBarHeight(Context context) {
        // 两种方式： 反射 + 布局树获取（特定id）
        int result = (int) (context.getResources().getDimension(R.dimen.status_bar_height) + 0.5);
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "状态栏高度" + result);
        return result;
    }


    /**
     * 获取当前进程名称
     * 需要注意的是主进程为appId的值
     */
    @Nullable
    public static String getCurrentProcessName(@NonNull Context context) {
        if (null == context) {
            return null;
        }
        String currentProcName = "";
        final int pid = Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
            if (null == runningServices) {
                for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
                    if (runningServiceInfo.pid == pid) {
                        currentProcName = runningServiceInfo.process;
                        break;
                    }
                }
            }
        } else {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
                if (processInfo.pid == pid) {
                    currentProcName = processInfo.processName;
                    break;
                }
            }
        }

        return currentProcName;
    }


    /**
     * 是否运行在主进程中
     *
     * @param var0
     * @return
     */
    public static boolean isMainProcess(Context var0) {
        ActivityManager var1 = (ActivityManager) var0.getSystemService(Context.ACTIVITY_SERVICE);
        String var4 = var0.getPackageName();
        int var2 = Process.myPid();
        List var3;
        if ((var3 = var1.getRunningAppProcesses()) == null) {
            List var6;
            if ((var6 = var1.getRunningServices(2147483647)) == null) {
                return false;
            } else {
                Iterator var9 = var6.iterator();

                ActivityManager.RunningServiceInfo var7;
                do {
                    if (!var9.hasNext()) {
                        return false;
                    }
                } while ((var7 = (ActivityManager.RunningServiceInfo) var9.next()).pid != var2
                        || !var4.equals(var7.service.getPackageName()));

                return true;
            }
        } else {
            Iterator var5 = var3.iterator();

            ActivityManager.RunningAppProcessInfo var8;
            do {
                if (!var5.hasNext()) {
                    return false;
                }
            } while ((var8 = (ActivityManager.RunningAppProcessInfo) var5.next()).pid != var2
                    || !var4.equals(var8.processName));

            return true;
        }
    }

    /**
     * 获取屏幕的高度
     *
     * @param appContext
     * @return
     */

    public static int getScreenHeight(@NonNull Context appContext) {
        return getScreenHeight(appContext, false);
    }

    public static int getScreenHeight(@NonNull Context appContext, boolean diff) {
        Context context = appContext.getApplicationContext();
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (null == windowMgr) {
                result = 0;
            } else {
                DisplayMetrics dm = new DisplayMetrics();
                Display defaultDisplay = windowMgr.getDefaultDisplay();
                defaultDisplay.getRealMetrics(dm);
                result = dm.heightPixels;
            }

            if (diff && result > 0) {
                result = result - context.getResources().getDisplayMetrics().heightPixels;
            }
        } else {
            if (diff) {
                result = 0;
            } else {
                result = context.getResources().getDisplayMetrics().heightPixels;
            }
        }

        return result;
    }


    /**
     * 获取手机屏幕的宽度
     *
     * @param appContext
     * @return
     */
    public static int getScreenWidth(@NonNull Context appContext) {

        Context context = appContext.getApplicationContext();
        int width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (null == windowMgr) {
                width = 0;
            } else {
                DisplayMetrics dm = new DisplayMetrics();
                Display defaultDisplay = windowMgr.getDefaultDisplay();
                defaultDisplay.getRealMetrics(dm);
                width = dm.widthPixels;
            }
        } else {
            width = context.getResources().getDisplayMetrics().widthPixels;
        }

        return width;
    }

    /**
     * 跳转至播放界面
     *
     * @param context
     * @param phoneNum
     */
    public static void call(@NonNull Context context, @NonNull String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    public static void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseAppContextRef.getAppContext().startActivity(intent);
        }
    }
}
