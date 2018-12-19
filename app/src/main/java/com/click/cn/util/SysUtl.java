package com.click.cn.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by vito-xa49 on 2017/9/15.
 * <p>
 * #注意事项：
 * andorid M的特殊权限按照M的方式处理，低版本跳转至对应Rom的界面
 */

public class SysUtl {
    private static final String TAG = SysUtl.class.getSimpleName();

    /**
     * Build.MANUFACTURER
     */
    private static final String MANUFACTURER_HUAWEI = "Huawei";//华为
    private static final String MANUFACTURER_MEIZU = "Meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "Xiaomi";//小米
    private static final String MANUFACTURER_SONY = "Sony";//索尼
    public static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    private static final String MANUFACTURER_LETV = "Letv";//乐视
    private static final String MANUFACTURER_ZTE = "ZTE";//中兴
    private static final String MANUFACTURER_YULONG = "YuLong";//酷派
    private static final String MANUFACTURER_LENOVO = "LENOVO";//联想
    private static final String MANUFACTURER_360 = "360"; // 360
    private static final String MANUFACTURER_360_2 = "QIKU"; // 360
    // 公共属性key值
    private static final String KEY_FINGER_PRINT = "ro.build.fingerprint";
    private static final String KEY_SDK_VERSION = "ro.build.version.sdk"; // [ro.build.version.sdk]: [23]
    private static final String KEY_PRODUCT = "ro.build.product";    //[ro.build.product]: [QK1505_A01]

    private SysUtl() {
    }

    /**
     * 获取系统属性中指定键对应的值
     * 执行shell命令
     * <p>
     * 注意在Android O 上部分属性已不可获取
     *
     * @param propName
     * @return 返回空标识未获取到指定的属性信息
     */
    public static String getSystemPropertyForKey(String propName) {
        BufferedReader input = null;
        String line = null;
        try {
            Process process = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);
            line = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    return line;
                }
            } else {
                return line;
            }
        }
    }

    /**
     * 从./system/build.prop中查找设备信息(只能获取到标准的属性信息)
     *
     * @param propName
     * @return
     */
    public static String getSystemPropertyForKey2(String propName) {
        String property = null;
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            property = prop.getProperty(propName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return property;
        }
    }

    @Deprecated
    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 获取android版本信息
     *
     * @return
     */
    public static String getOSVersion() {
        return getSystemPropertyForKey(KEY_SDK_VERSION);
    }

    /**
     * 获取设备所有的属性信息(只能获取到标准的属性信息)
     *
     * @return
     * @throws IOException
     */
    public static Properties getBuildProperties() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        return prop;
    }


    // others ...

    public static final class MIUI {
        // MIUI
        //    [ro.miui.ui.version.code]: [6]
        //    [ro.miui.ui.version.name]: [V8]
        //    [ro.miui.version.code_time]: [1482336000]


        private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"; // VERSION
        private static final String KEY_MIUI_CPU_FLAG = "ro.product.cuptsm";
        //ro.fota.oem
        private static final String KEY_MIUI_INCREMENTAL = "ro.build.version.incremental";

        // miui版本与Android版本的对应关系比较复杂(version name)
        private static final String ROM_MIUI_V2 = "V2";
        private static final String ROM_MIUI_V3 = "V3";
        private static final String ROM_MIUI_V4 = "V4";
        private static final String ROM_MIUI_V5 = "V5";
        private static final String ROM_MIUI_V6 = "V6"; // 去V
        private static final String ROM_MIUI_V7 = "V7";
        private static final String ROM_MIUI_V8 = "V8";
        private static final String ROM_MIUI_V9 = "V9";

        // 透明状态栏API变更的分界线
        private static final String MIN_INCREMENTAL_CODE = "7.7.13";

        public static boolean isMIUI() {
            return !TextUtils.isEmpty(getMiuiVersionName());
        }

        /**
         * 判断当前的miui版本是否支持android标准的透明系统栏透明api
         *
         * @return
         */
        public static boolean supportTransluteStdApi() {
            if (isMIUI()) {
                int codeEquals = miuiIncrementalCodeEqualsForStatus();
                Log.i(TAG, codeEquals + "");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (codeEquals == 0 || codeEquals == 1)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new UnsupportedOperationException("非MIUI ROM, 请首先判断是否为MIUI");
            }
        }

        /**
         * 获取MIUI系统的版本名称
         *
         * @return 返回对应的MIUI版本名称， 如果为null,则非MIUI ROM
         */
        @Nullable
        public static String getMiuiVersionName() {
            String versionName = getSystemPropertyForKey(KEY_MIUI_VERSION_NAME);
            if (versionName != null
                    && getSystemPropertyForKey(KEY_MIUI_VERSION_CODE) != null
                    && getSystemPropertyForKey(KEY_MIUI_CPU_FLAG) != null) {
                return versionName;
            } else {
                return "";
            }
        }

        /**
         * 获取miui的内部版本号
         *
         * @return
         */
        public static String getMiuiIncrementalCode() {
            return getSystemPropertyForKey(KEY_MIUI_INCREMENTAL);
        }

        /**
         * 比较miui内部版本号的大小(7.7.13)
         *
         * @return 0表示相等，1表示当前版本较大， -1 表示当前版本较小，-2表示错误
         */
        private static int miuiIncrementalCodeEqualsForStatus() {
            if (!isMIUI()) {
                return -2;
            }

            String currentIncrementalCode = getMiuiIncrementalCode();
            Log.i(TAG, "当前miui内部版本：" + currentIncrementalCode);
            if (TextUtils.isEmpty(currentIncrementalCode)) {
                return -2;
            } else {
                // 依据目前的已知信息，版本号为三位分割数字
                // todo: 稳定版示例： V9.2.7.0.NDECNEK   开发版示例： 7.6.8
                currentIncrementalCode = currentIncrementalCode.toLowerCase().replace("v", ""); // V7.2.7.0.LHPCNDB
                Log.i(TAG, "当前miui内部版本：" + currentIncrementalCode);
                String[] currentVersionCodeArray = currentIncrementalCode.split("\\.");
                String[] minVersionCodeArray = MIN_INCREMENTAL_CODE.split("\\.");
                if (currentVersionCodeArray.length >= 3 && minVersionCodeArray.length >= 3) {
                    if (Integer.parseInt(currentVersionCodeArray[0]) > Integer.parseInt(minVersionCodeArray[0])) {
                        return 1;
                    } else if (Integer.parseInt(currentVersionCodeArray[0]) < Integer.parseInt(minVersionCodeArray[0])) {
                        return -1;
                    } else {
                        if (Integer.parseInt(currentVersionCodeArray[1]) > Integer.parseInt(minVersionCodeArray[1])) {
                            return 1;
                        } else if (Integer.parseInt(currentVersionCodeArray[1]) < Integer.parseInt(minVersionCodeArray[1])) {
                            return -1;
                        } else {
                            if (Integer.parseInt(currentVersionCodeArray[2]) > Integer.parseInt(minVersionCodeArray[2])) {
                                return 1;
                            } else if (Integer.parseInt(currentVersionCodeArray[2]) < Integer.parseInt(minVersionCodeArray[2])) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    }
                } else {
                    return -2;
                }
            }
        }

        /**
         * 跳转至权限设置界面
         *
         * @param context
         */
        public static void gotoPermissionPage(Activity context) {
            gotoPermissionPage(context, SysUtl.MIUI.getMiuiVersionName());
        }

        /**
         * 跳转至小米的权限设置界面
         *
         * @param context
         * @param romVersion
         */
        public static void gotoPermissionPage(@NonNull Activity context, String romVersion) {
            Intent intent = null;
            if (TextUtils.isEmpty(romVersion)) {
                JumpPermissionManagement.gotoAppSettingsPage(context); // 按普通方式处理
                return;
            }

            switch (romVersion) {
                case ROM_MIUI_V5:
                    Uri packageURI = Uri.parse("package:" + context.getApplicationInfo().packageName);
                    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    break;
                case ROM_MIUI_V6:
                case ROM_MIUI_V7:
                    intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", context.getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    break;
                case ROM_MIUI_V8:
                case ROM_MIUI_V9:
                    intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", context.getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    break;
            }

            if (intent != null && AppUtil.isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                JumpPermissionManagement.gotoAppSettingsPage(context);
            }
        }
    }

    public static final class Flyme {
        // flyme从Android 4.0时代诞生

//        [ro.build.display.id]: [Flyme 5.1.12.23R beta]
//        [ro.build.user]: [flyme]
//        [ro.config.ringtone]: [03_Flyme.ogg]
//        [ro.flyme.hideinfo]: [true]
//        [ro.flyme.published]: [false]
//        [ro.flyme.romer]: [Official]

//        [persist.sys.use.flyme.icon]: [true]
//        [init.svc.flymed]: [running]

//        [ro.meizu.autorecorder]: [true]
//                [ro.meizu.contactmsg.auth]: [false]
//                [ro.meizu.customize.pccw]: [false]
//                [ro.meizu.permanentkey]: [false]
//                [ro.meizu.published.type]: [inside]
//                [ro.meizu.region.enable]: [true]
//                [ro.meizu.rom.config]: [true]
//                [ro.meizu.security]: [true]
//                [ro.meizu.setupwizard.flyme]: [true]
//                [ro.meizu.setupwizard.setlang]: [true]
//                [ro.meizu.sip.support]: [true]
//                [ro.meizu.visualvoicemail]: [true]
//                [ro.meizu.voip.support]: [false]

        //Flyme标识
        private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";
        private static final String KEY_FLYME_ID_FALG_VALUE_KEYWORD = "flyme";
        private static final String KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon";
        private static final String KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme";
        private static final String KEY_FLYME_PUBLISH_FALG = "ro.flyme.published";
        // ROM版本  https://baike.baidu.com/item/flyme/612354?fr=aladdin

        /**
         * 检测是否是魅族系统
         *
         * @return
         */
        public static boolean checkIsMeizuRom() {
            Properties prop = null;
            try {
                prop = getBuildProperties();
                String flagID = prop.getProperty(KEY_FLYME_ID_FALG_KEY, null);
                if (!TextUtils.isEmpty(flagID)
                        && (flagID.contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD) || flagID.toLowerCase().contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD))) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 去魅族权限申请页面
         */
        public static void gotoPermissionPage(Activity context) {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
            intent.putExtra("packageName", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (AppUtil.isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                JumpPermissionManagement.gotoAppSettingsPage(context);
            }
        }

        /**
         * 判断是否是魅族flyme系统
         *
         * @return
         */
        public static boolean isFlyme() {
            boolean os = false;
            String meizuFlymeOSFlag = getMeizuFlymeOSFlag();
            if (null != meizuFlymeOSFlag && meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
                os = true;
            } else {
                os = false;
            }
            return os;
        }

        @Nullable
        public static String getMeizuFlymeOSFlag() {
            return getSystemPropertyForKey("ro.build.display.id");
        }

        // fixme:
//        public static void test(Activity activity) {
//            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            intent.putExtra("packageName", BuildConfig.APPLICATION_ID); // todo:---
//            activity.startActivity(intent);
//        }

    }

    // todo : 需要验证
    public static final class EMUI {

        // EMUI(    [ro.build.hw_emui_api_level]: [11]      )
        private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
        //[ro.build.version.emui]: [EmotionUI_5.0]
        private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
        private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";
        // 版本
        public static final String ROM_PREFIX = "EmotionUI_";
        public static final String ROM_EMUI_8_0 = ROM_PREFIX + "5.0"; // android 8.0         http://www.chinaz.com/mobile/2017/1016/817264.shtml
        public static final String ROM_EMUI_5_1 = ROM_PREFIX + "5.1"; // 应该是基于andorid 7.0
        public static final String ROM_EMUI_5_0 = ROM_PREFIX + "5.0"; // android 7.0
        public static final String ROM_EMUI_4_1 = ROM_PREFIX + "4.1"; // android 6.0
        public static final String ROM_EMUI_4_0 = ROM_PREFIX + "4.0"; // android 6.0
        public static final String ROM_EMUI_3_1 = ROM_PREFIX + "3.1"; // android 5.1
        public static final String ROM_EMUI_3_0 = ROM_PREFIX + "3.0"; // android 4.4.2
        public static final String ROM_EMUI_2_3 = ROM_PREFIX + "2.3"; // android 4.4.2
        public static final String ROM_EMUI_2_0 = ROM_PREFIX + "2.0"; // android 4.2.2 、android 4.3
        public static final String ROM_EMUI_1_6 = ROM_PREFIX + "1.6"; // android 4.2.2
        public static final String ROM_EMUI_1_5 = ROM_PREFIX + "1.5"; // android 4.1
        public static final String ROM_EMUI_1_0 = ROM_PREFIX + "1.0"; // andorid 4.0、 android 4.1

        /**
         * 获取EMUI系统的版本
         *
         * @return 如果返回null, 则标识非EMUI系统
         */
        public static String getEmuiVersion() {
            String emuiVersion = null;
//            try {
//                Properties prop = getBuildProperties();
//                emuiVersion = prop.getProperty(KEY_EMUI_VERSION, null);
//                if(emuiVersion != null && prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
//                        && prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
//
//                }

            emuiVersion = getSystemPropertyForKey(KEY_EMUI_VERSION);
            if (emuiVersion != null && getSystemPropertyForKey(KEY_EMUI_API_LEVEL) != null
                    && getSystemPropertyForKey(KEY_EMUI_CONFIG_HW_SYS_VERSION) != null) {

            }
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }finally {
//                return emuiVersion;
//            }
            return emuiVersion;
        }

        public static double getEmuiVersionCode() {
            String emuiVersionStr = getEmuiVersion();
            if (TextUtils.isEmpty(emuiVersionStr)) {
                return 0;
            } else {
                String[] split = emuiVersionStr.split("_");
                if (split.length == 2) {
                    if (ROM_PREFIX.substring(0, ROM_PREFIX.length() - 1).equals(split[0])) {
                        int pointSize = 0;
                        for (char chStr : split[1].toCharArray()) {
                            if ('.' == chStr) {
                                pointSize++;
                            }
                        }
                        if (pointSize <= 0) {
                            return 0.0;
                        } else if (pointSize == 1) {
                            return Double.parseDouble(split[1]);
                        } else if (pointSize >= 2) {
                            return Double.parseDouble(split[1].substring(0, split[1].lastIndexOf(".")));
                        }
                    }
                }
                return 0.0;
            }
        }

        /**
         * 去华为权限申请页面
         */
        @Deprecated
        public static void applyPermission(Context context) {
            String emuiVersion = getEmuiVersion();
            if (TextUtils.isEmpty(emuiVersion)) {
                return;
            }

            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.PhotoActivity");//华为权限管理
                //   ComponentName comp = new ComponentName("com.huawei.systemmanager",
                //      "com.huawei.permissionmanager.ui.SingleAppActivity");//华为权限管理，跳转到指定app的权限管理位置需要华为接口权限，未解决
                ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
                intent.setComponent(comp);
                if (ROM_EMUI_3_1.equals(emuiVersion)) { // todo: --- !!!
                    //emui 3.1 的适配
                    context.startActivity(intent);
                } else {
                    //emui 3.0 的适配
                    comp = new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");//悬浮窗管理页面
                    intent.setComponent(comp);
                    context.startActivity(intent);
                }
            } catch (SecurityException e) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.PhotoActivity");//华为权限管理
                ComponentName comp = new ComponentName("com.huawei.systemmanager",
                        "com.huawei.permissionmanager.ui.PhotoActivity");//华为权限管理，跳转到本app的权限管理页面,这个需要华为接口权限，未解决
//      ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
                intent.setComponent(comp);
                context.startActivity(intent);
                Log.e(TAG, Log.getStackTraceString(e));
            } catch (ActivityNotFoundException e) {
                /**
                 * 手机管家版本较低 HUAWEI SC-UL10
                 */
//   Toast.makeText(PhotoActivity.this, "act找不到", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName comp = new ComponentName("com.Android.settings", "com.android.settings.permission.TabItem");//权限管理页面 android4.4
//   ComponentName comp = new ComponentName("com.android.settings","com.android.settings.permission.single_app_activity");//此处可跳转到指定app对应的权限管理页面，但是需要相关权限，未解决
                intent.setComponent(comp);
                context.startActivity(intent);
                e.printStackTrace();
                Log.e(TAG, Log.getStackTraceString(e));
            } catch (Exception e) {
                //抛出异常时提示信息
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }


        public static void gotoPermissionPage(Activity context) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.PhotoActivity");
            intent.setComponent(comp);

            if (AppUtil.isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                JumpPermissionManagement.GoToSetting(context);
            }
//            ComponentName comp = new ComponentName(“com.android.settings”,”com.android.settings.permission.TabItem”);//权限管理页面 android4.4

//            ComponentName(“com.android.settings”,”com.android.settings.permission.single_app_activity”);//此处可跳转到指定app对应的权限管理页面，但是需要相关权限，未解决
        }

        /**
         * 跳转至华为受保护应用界面
         *
         * @param context
         */
        private static void goProtect(Context context) {
            try {
                Intent intent = new Intent("demo.vincent.com.tiaozhuan");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
                intent.setComponent(comp);
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "跳转失败", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        public static void Huawei(Activity activity, String applicationId) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", applicationId);
            ComponentName comp = new ComponentName("com.huawei.systemmanager",
                    "com.huawei.permissionmanager.ui.PhotoActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        }
    }

    public static final class QIKU {
        //[ro.build.uiversion]: [360UI:V2.0]

        private static final String FLAG_KEY = "ro.build.uiversion";

        public static boolean is360Rom() {
//            if(Build.MANUFACTURER.contains("QIKU") || Build.MANUFACTURER.contains("360")){
//                return true;
//            }else{
//                return false;
//            }

            try {
                Properties prop = getBuildProperties();
                if (TextUtils.isEmpty(prop.getProperty(FLAG_KEY, null))) {
                    return false;
                } else {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 经过测试打开的是360安全卫士
         *
         * @param activity
         */
        public static void gotoPermissionPage(Activity activity) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", activity.getApplication().getPackageName());
            ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            intent.setComponent(comp);
            if (AppUtil.isIntentAvailable(intent, activity)) {
                activity.startActivity(intent);
            } else {
                JumpPermissionManagement.gotoAppSettingsPage(activity);
            }
        }

    }

    public static final class SONY {

        public static void gotoPermissionPage(Context context, @NonNull String packageName) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", packageName);
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        }
    }

    public static final class OPPP_ROM {
        // ro.build.version.opporom

        public static boolean isOppo() {
            return SysUtl.MANUFACTURER_OPPO.toLowerCase(Locale.ENGLISH)
                    .equals(Build.MANUFACTURER.toLowerCase(Locale.ENGLISH));
        }


        public static Intent gotoNotificationPage(Context context, @NonNull String appName) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pkg_name", context.getPackageName());
            intent.putExtra("app_name", appName /*context.getString(R.string.app_name)*/);
            intent.putExtra("class_name", "com.welab.notificationdemo.PhotoActivity");
            ComponentName comp = new ComponentName("com.coloros.notificationmanager", "com.coloros" +
                    ".notificationmanager.AppDetailPreferenceActivity");
            intent.setComponent(comp);
            return intent;
        }

        public static void OPPO(Activity activity, String applicationId) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", applicationId);
            ComponentName comp = new ComponentName("com.color.safecenter",
                    "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        }
    }

    public static final class VIVO_ROM {
        /**
         * VIVO ROM
         * {@see http://www.vivo.com.cn/funtouchos}
         */
        // ro.vivo.os.version
        private static final String FLAY_KEY = "ro.vivo.os.version";

        public static boolean isVivo() {
            try {
                Properties prop = getBuildProperties();
                if (TextUtils.isEmpty(prop.getProperty(FLAY_KEY, null))) {
                    return false;
                } else {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // 未测试
        private void startOppoNotification(Activity activity) {
            //Intent intent = new //Intent("com.coloros.notificationmanager.app.detail_ab//andon");
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.coloros.notificationmanager", "com.coloros.notificationmanager.AppDetailPreferenceActivity"));
            intent.putExtra("pkg_name", "com.coohuaclient");
            intent.putExtra("app_name", "酷划锁屏");
            intent.putExtra("class_name", "com.coohuaclient.ui.activity.SplashActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    public static final class LE_ROM {
        //

        public static void gotoPermissionPage(Activity activity) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", activity.getApplication().getPackageName());
            ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
            intent.setComponent(comp);
            if (AppUtil.isIntentAvailable(intent, activity)) {
                activity.startActivity(intent);
            } else {
                JumpPermissionManagement.gotoAppSettingsPage(activity);
            }
        }
    }


    public static final class LG {

        public static void gotoPermissionPage(Activity activity, String applicationId) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", applicationId);
            ComponentName comp = new ComponentName("com.android.settings",
                    "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            if (AppUtil.isIntentAvailable(intent, activity)) {
                activity.startActivity(intent);
            } else {
                JumpPermissionManagement.gotoAppSettingsPage(activity);
            }
        }
    }

    public static void Smartisan(Context activity, String applicationId) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", applicationId);
        ComponentName comp = new ComponentName("com.smartisanos.security", "com.smartisanos.security.PhotoActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * 针对Oppo R9 colosos3.0.0 android-5.1
     *
     * @param context
     * @return
     */
    public static void getIntentForOppoR9(Activity context, @NonNull String appName) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pkg_name", context.getPackageName());
        intent.putExtra("app_name", appName /*context.getString(R.string.app_name)*/);
        intent.putExtra("class_name", context.getClass().getName()); // 跳转界面的全类名
        ComponentName comp = new ComponentName("com.coloros.notificationmanager", "com.coloros" +
                ".notificationmanager.AppDetailPreferenceActivity");
        intent.setComponent(comp);
        context.startActivity(intent);
    }


}
