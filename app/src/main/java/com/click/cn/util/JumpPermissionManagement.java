package com.click.cn.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by vito-xa49 on 2017/9/1.
 */

public class JumpPermissionManagement {

    /**
     * 跳转至系统设置界面
     * 或者
     * rom中包含权限管理app
     * <p>
     * 部分手机修改权限后，App会被系统kill掉,属于正常现象
     *
     * @param activity
     */
    public static void GoToSetting(Activity activity) {

//        try {
//            BuglyLog.i("prop", SysUtl.getBuildProperties().toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String romVersion = SysUtl.MIUI.getMiuiVersionName();
        if (!TextUtils.isEmpty(romVersion)) {
            SysUtl.MIUI.gotoPermissionPage(activity, romVersion);
            return;
        }
        if (SysUtl.Flyme.isFlyme()) {
            SysUtl.Flyme.gotoPermissionPage(activity);
            return;
        }

        if (!TextUtils.isEmpty(SysUtl.EMUI.getEmuiVersion())) {
            SysUtl.EMUI.gotoPermissionPage(activity);
            return;
        }

        if (SysUtl.QIKU.is360Rom()) {
            SysUtl.QIKU.gotoPermissionPage(activity);
            return;
        }

        gotoAppSettingsPage(activity);
        Log.e("goToSetting", "目前可能暂不支持准确此系统");
    }

    /**
     * 应用信息界面
     *
     * @param activity
     */
    public static void gotoAppSettingsPage(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivity(localIntent);
    }

    /**
     * 系统设置界面
     *
     * @param activity
     */
    public static void SystemConfig(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }

    public static void gotoWifiSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        activity.startActivity(intent);
    }
}
