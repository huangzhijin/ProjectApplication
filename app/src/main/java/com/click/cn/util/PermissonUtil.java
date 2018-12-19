//package com.click.cn.util;
//
//import android.Manifest;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.AppOpsManager;
//import android.app.NotificationManager;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Binder;
//import android.os.Build;
//import android.os.PowerManager;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.annotation.RequiresPermission;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.NotificationManagerCompat;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by vito-xa49 on 2017/9/13.
// * 统一权限请求工具
// * <p>
// * #权限请求分为两个阶段：
// * 4.4 --- 5.x
// * 6.0以上
// * #另外还有一些特殊请求的处理
// * 通知、省电白名单、悬浮窗、定位（位置服务）等
// * <p>
// * 注意：
// * 1.被Fragment中嵌套的Fragment中请求权限需要格外注意
// * Fragment中请求权限：fragment.requestPermission()在少数手机上回调至依附的Activity上，和Fragment的生命周期有关系
// */
//
//public class PermissonUtil {
//    private static final String TAG = PermissonUtil.class.getSimpleName();
//
//    /**
//     * Activity中的权限请求
//     *
//     * @param activity
//     * @param permissionArray
//     * @param requestCode
//     * @retrun true表示权限已经授予， false表示部分权限并未授予并进行了额外的请求
//     */
//    public static final boolean requestPermissionForActivity(@NonNull final Activity activity, @NonNull final String[] permissionArray,
//                                                             @NonNull final int requestCode, boolean reasonableTips, final IGoToSettingListener listener) {
//        if (permissionArray == null || permissionArray.length <= 0) {
//            return true;
//        }
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            return true;
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return requestPermissionMAfterForActivity(activity, permissionArray, requestCode, reasonableTips);
//        } else {
//            return requestPermissionMBefore(activity, permissionArray, listener);
//        }
//    }
//
//    @RequiresApi(23)
//    private static boolean requestPermissionMAfterForActivity(@NonNull final Activity activity, @NonNull String[] permissionArray, @NonNull final int requestCode, boolean reasonableTips) {
//        // android M+
//        // 1.过滤未授权权限
//        List<String> tempPermission = new ArrayList<>();
//        for (String perm : permissionArray) {
//            // fixme: 经过测试，部分情况： 如果手机ROM中自带了安全管理软件（或者root + 安全管理软件）, 权限在拒绝状态下也会返回0
//            if (!TextUtils.isEmpty(perm) && ActivityCompat.checkSelfPermission(activity, perm) == PackageManager.PERMISSION_DENIED) { // todo: 需要考虑的特殊情况----第三方安全软件的行为 ---（try-catch）
//                tempPermission.add(perm);
//            }
//        }
//        if (tempPermission.size() <= 0) {
//            return true;
//        }
//
//        final String[] deniedPermissions = new String[tempPermission.size()];
//        tempPermission.toArray(deniedPermissions);
//
//        // 2. 判断是否需要人性化的提示信息
//        boolean shoudShowTips = false;
//        for (String perm : deniedPermissions) {
//            // 第一次请求权限或者用户上次勾选了不再提示，则返回 false
//            // 显示不再提示的选择框有两种情况： 1.用户在应用设置中关闭了权限； 2.用户在系统权限请求对话框中拒绝了权限请求(未勾选不再提示)
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {
//                shoudShowTips = true;
//                break;
//            }
//        }
//
//        // 3. 动态请求权限
//        if (shoudShowTips && reasonableTips) {
//            DialogUtil.buildTipsDialog(activity, activity.getString(R.string.tips_reasonable_permission), new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
//                    ActivityCompat.requestPermissions(activity, deniedPermissions, requestCode);
//                }
//            }).show();
//        } else {
//            ActivityCompat.requestPermissions(activity, deniedPermissions, requestCode);
//        }
//        return false;
//    }
//
//    private static boolean requestPermissionMBefore(@NonNull final Activity activity, @NonNull String[] permissionArray, final IGoToSettingListener listener) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            return true;
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            for (int index = 0; index < permissionArray.length; index++) {
//                Integer opPermission = PERMISSION_CONVERT.get(permissionArray[index]);
//                int result = checkOp(activity, opPermission);
//                if (result == 1) { // 低版本中只要有可判断的一个权限被否定，就进行提示
//                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
//                        setMode(activity, opPermission, true);
//                    } else {
//                        DialogUtil.buildSimpleDialog(activity, "权限提示", "检测到部分权限被禁止，请允许本应用申请的权限，以保证应用的正常运行",
//                                "去设置", "", false, new MaterialDialog.SingleButtonCallback() {
//                                    @Override
//                                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
//                                        PermissonUtil.permissionDinedForMBefore(activity);
//                                        if (listener != null) {
//                                            listener.gotoSetting();
//                                        }
//                                    }
//                                }, null).show();
//                        return false;
//                    }
//                }
//            }
//            return true;
//        }
//        return true;
//    }
//
//    /**
//     * 支持库V4 中Fragment中的权限请求
//     *
//     * @param fragment
//     * @param attachedContext
//     * @param permissions
//     * @param requestCode
//     */
//    public static final boolean requestPermissionForFragmentV4(@NonNull final android.support.v4.app.Fragment fragment,
//                                                               @NonNull Context attachedContext, @NonNull final String[] permissions,
//                                                               boolean reasonableTips,
//                                                               @NonNull final int requestCode, final IGoToSettingListener listener) {
//        if (permissions == null || permissions.length <= 0) {
//            return true;
//        }
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            return true;
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return requestPermissionMAfterForFragmentV4(fragment, attachedContext, permissions, reasonableTips, requestCode);
//        } else {
//            if (attachedContext instanceof Activity) {
//                return requestPermissionMBefore((Activity) attachedContext, permissions, listener);
//            } else {
//                throw new UnsupportedOperationException("Context 必须为Activity");
//            }
//        }
//    }
//
//    @RequiresApi(23)
//    private static boolean requestPermissionMAfterForFragmentV4(@NonNull final Fragment fragment, @NonNull Context attachedContext, @NonNull final String[] permissions,
//                                                                boolean reasonableTips, @NonNull final int requestCode) {
//        // android M+
//        // 1.过滤未授权权限
//        List<String> tempPermission = new ArrayList<>();
//        for (String perm : permissions) {
//            if (!TextUtils.isEmpty(perm) && ActivityCompat.checkSelfPermission(attachedContext, perm) == PackageManager.PERMISSION_DENIED) { //todo: 安全软件的问题！！！
//                tempPermission.add(perm);
//            }
//        }
//        if (tempPermission.size() <= 0) {
//            return true;
//        }
//        // 2.判断是否需要人性化的提示信息
//        boolean shoudShowTips = false;
//        for (String perm : permissions) {
//            // 第一次请求权限或者用户上次勾选了不再提示，则返回 false
//            // 显示不再提示的选择框有两种情况： 1.用户在应用设置中关闭了权限； 2.用户在系统权限请求对话框中拒绝了权限请求
//            if (fragment.shouldShowRequestPermissionRationale(perm)) {
//                shoudShowTips = true;
//                break;
//            }
//        }
//        // 3. 动态权限请求（注意Fragment是否是嵌套的）
//        if (reasonableTips && shoudShowTips) {
//            DialogUtil.buildTipsDialog(attachedContext, attachedContext.getString(R.string.tips_reasonable_permission), new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
//                    Fragment parentFragment = fragment.getParentFragment();
//                    if (parentFragment == null) {
//                        fragment.requestPermissions(permissions, requestCode);
//                    } else {
//                        parentFragment.requestPermissions(permissions, requestCode);
//                    }
//                }
//            });
//        } else {
//            Fragment parentFragment = fragment.getParentFragment();
//            if (parentFragment == null) {
//                fragment.requestPermissions(permissions, requestCode);
//            } else {
//                parentFragment.requestPermissions(permissions, requestCode);
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * 针对appcompat V14的权限请求
//     *
//     * @param fragment
//     * @param attachedContext
//     * @param permissions
//     * @param requestCode
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public static final boolean requestPermissionForFragmentV13(@NonNull final android.app.Fragment fragment, @NonNull Context attachedContext, @NonNull final String[] permissions,
//                                                                @NonNull final int requestCode, boolean reasonableTips, final IGoToSettingListener listener) {
//        if (permissions == null || permissions.length <= 0) {
//            return true;
//        }
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            return true;
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return requestPermissionMAfterForFragmentV13(fragment, attachedContext, permissions, reasonableTips, requestCode);
//        } else {
//            if (attachedContext instanceof Activity) {
//                return requestPermissionMBefore((Activity) attachedContext, permissions, listener);
//            } else {
//                throw new UnsupportedOperationException("Context 必须为Activity");
//            }
//        }
//
//    }
//
//    @RequiresApi(23)
//    private static boolean requestPermissionMAfterForFragmentV13(@NonNull final android.app.Fragment fragment, @NonNull Context attachedContext,
//                                                                 @NonNull final String[] permission, boolean reasonableTips, @NonNull final int requestCode) {
//        // android M+
//        // 1.过滤未授权权限
//        List<String> tempPermission = new ArrayList<>();
//        for (String perm : permission) {
//            if (!TextUtils.isEmpty(perm) && ActivityCompat.checkSelfPermission(attachedContext, perm) == PackageManager.PERMISSION_DENIED) {
//                tempPermission.add(perm);
//            }
//        }
//        if (tempPermission.size() <= 0) {
//            return true;
//        }
//        // 2.动态权限请求（注意Fragment是否是嵌套的）
//        boolean shoudShowTips = false;
//        for (String perm : permission) {
//            // 第一次请求权限或者用户上次勾选了不再提示，则返回 false
//            // 显示不再提示的选择框有两种情况： 1.用户在应用设置中关闭了权限； 2.用户在系统权限请求对话框中拒绝了权限请求
//            if (android.support.v13.app.FragmentCompat.shouldShowRequestPermissionRationale(fragment, perm)) {
//                shoudShowTips = true;
//                break;
//            }
//        }
//        // 3. 动态权限请求
//        if (reasonableTips && shoudShowTips) {
//            DialogUtil.buildTipsDialog(attachedContext, attachedContext.getString(R.string.tips_reasonable_permission), new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
//                    android.app.Fragment parentFragment = fragment.getParentFragment(); // api 17
//                    if (null == parentFragment) {
//                        android.support.v13.app.FragmentCompat.requestPermissions(fragment, permission, requestCode);
//                    } else {
//                        android.support.v13.app.FragmentCompat.requestPermissions(parentFragment, permission, requestCode);
//                    }
//                }
//            });
//        } else {
//            android.app.Fragment parentFragment = fragment.getParentFragment(); // api 17
//            if (null == parentFragment) {
//                android.support.v13.app.FragmentCompat.requestPermissions(fragment, permission, requestCode);
//            } else {
//                android.support.v13.app.FragmentCompat.requestPermissions(parentFragment, permission, requestCode);
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 4.4 - 5.x  {@link AppOpsManager}
//     * 从Android 4.3开始谷歌就就将权限管理功能集成系统里了，Google把他叫App Ops（Application Operations）。
//     * API 21 后需要签名验证的权限android.Manifest.permission.UPDATE_APP_OPS_STATS，第三方应用用不了了。
//     *
//     * @param context
//     * @param op      op 的值是 0 ~ 47，其中0代表粗略定位权限，1代表精确定位权限，24代表悬浮窗权限。
//     * @return 0 就代表有权限，1代表没有权限，-1函数出错啦(todo: 建议使用！=1来判断，部分国内rom对此方法进行了屏蔽，此方法会返回-1)
//     */
//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    private static int checkOp(@NonNull Context context, int op) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            if (null == appOpsManager) {
//                return -1;
//            }
//            Class c = appOpsManager.getClass();
//            try {
//                Class[] cArg = new Class[3];
//                cArg[0] = int.class;
//                cArg[1] = int.class;
//                cArg[2] = String.class;
//                Method method = c.getDeclaredMethod("checkOp", cArg); // or use checkOpNoThrow()
//                return (Integer) method.invoke(appOpsManager, op, Binder.getCallingUid(), context.getPackageName());
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//                Log.e("permission", "permission:: NoSuchMethodException" + e.getMessage());
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//                Log.e("permission", "permission:: IllegalAccessException" + e.getMessage());
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//                Log.e("permission", "permission:: IllegalArgumentException" + e.getMessage());
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//                Log.e("permission", "permission:: InvocationTargetException" + e.getMessage());
//            } catch (Exception e) {
//                Log.e("permission", e.getMessage());
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * 传入OPSTR_*权限名称
//     *
//     * @param context
//     * @param op
//     */
//    @RequiresApi(19)
//    public static void checkOPNoThrow(@NonNull Context context, @NonNull String op) {
//        // 传入权限时要注意版本
//        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//        if (appOpsManager == null) {
//            return;
//        }
//        appOpsManager.checkOpNoThrow(op, Binder.getCallingUid(), context.getPackageName());
//    }
//
//    /**
//     * android 4.4 - android 5.X 检测权限
//     * <p>
//     * #注意：
//     * 部分国内Rom 屏蔽了AppOpsManager，这里内部会捕获抛出的异常并返回true
//     *
//     * @param context
//     * @param op
//     * @return
//     */
//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    public static boolean checkOPCompact(Context context, int op) {
//        return (checkOp(context, op) != 1) ? true : false;
//    }
//
//    /**
//     * 可设置Android 4.3/4.4的授权状态
//     *
//     * @param context
//     * @param op
//     * @param allowed
//     * @return
//     */
//    @TargetApi(19)
//    private static boolean setMode(@NonNull Context context, int op, boolean allowed) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return false;
//        }
//
//        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//        if (manager == null) {
//            return false;
//        }
//        try {
//            // public void setMode(int code, int uid, String packageName, int mode)
//            Method method = AppOpsManager.class.getDeclaredMethod("setMode", int.class, int.class, String.class, int.class);
//            method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName(), allowed ? AppOpsManager.MODE_ALLOWED : AppOpsManager
//                    .MODE_IGNORED);
//            return true;
//        } catch (Exception e) {
//            Log.e(TAG, Log.getStackTraceString(e));
//        }
//        return false;
//    }
//
//    /**
//     * android 4.4 - android 5.x 权限检测失败后的操作
//     *
//     * @param activity
//     */
//    public static final void permissionDinedForMBefore(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            JumpPermissionManagement.GoToSetting(activity);
//        }
//    }
//
//    // ------------------------------------------- 6.0 动态权限声明 (9组) -------------------------------------------
//    // adb shell pm list permissions -d -g  (部分手机存在自定义的危险权限)
//
//    // android.Manifest.permission_group.CALENDAR
//    public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
//    public static final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
//
//    //    android.Manifest.permission_group.CAMERA
//    public static final String CAMERA = Manifest.permission.CAMERA;
//
//    //    android.Manifest.permission.CONTACTS;
//    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
//    public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
//    public static final String GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
//    //     android.Manifest.permission.LOCATION
//    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
//    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//    //    android.Manifest.permission.MICROPHONE
//    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
//    //    android.Manifest.permission.PHONE
//    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
//    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
//    public static final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG; // api 16
//    public static final String WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG; // api 16
//    public static final String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
//    public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
//    //    android.Manifest.permission.SENSORS
//    public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS; // api 20
//    //    android.Manifest.permission.STORAGE
//    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE; // api 16
//    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//
//    //    android.Manifest.permission_group.SMS
//    public static final String SEND_SMS = Manifest.permission.SEND_SMS;
//    public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
//    public static final String READ_SMS = Manifest.permission.READ_SMS;
//    public static final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
//    public static final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
//
//    /*------------------------------------Android4.4 - Android 5.x-------------------------------------------*/
//
//    public static final class PermissionOps {
//        /**
//         * Access to coarse location information.
//         */
//        public static final int OP_COARSE_LOCATION = 0;
//        /**
//         * Access to fine location information.
//         */
//        public static final int OP_FINE_LOCATION = 1;
//        /**
//         * Causing GPS to run.
//         */
//        public static final int OP_GPS = 2;
//        /**  */
//        public static final int OP_VIBRATE = 3;
//        /**  */
//        public static final int OP_READ_CONTACTS = 4;
//        /**  */
//        public static final int OP_WRITE_CONTACTS = 5;
//        /**  */
//        public static final int OP_READ_CALL_LOG = 6;
//        /**  */
//        public static final int OP_WRITE_CALL_LOG = 7;
//        /**  */
//        public static final int OP_READ_CALENDAR = 8;
//        /**  */
//        public static final int OP_WRITE_CALENDAR = 9;
//        /**  */
//        public static final int OP_WIFI_SCAN = 10;
//        /**  */
//        public static final int OP_POST_NOTIFICATION = 11; // todo: 通知栏权限检测
//        /**  */
//        public static final int OP_NEIGHBORING_CELLS = 12;
//        /**  */
//        public static final int OP_CALL_PHONE = 13;
//        /**  */
//        public static final int OP_READ_SMS = 14;
//        /**  */
//        public static final int OP_WRITE_SMS = 15;
//        /**  */
//        public static final int OP_RECEIVE_SMS = 16;
//        /**  */
//        public static final int OP_RECEIVE_EMERGECY_SMS = 17;
//        /**  */
//        public static final int OP_RECEIVE_MMS = 18;
//        /**  */
//        public static final int OP_RECEIVE_WAP_PUSH = 19;
//        /**  */
//        public static final int OP_SEND_SMS = 20;
//        /**  */
//        public static final int OP_READ_ICC_SMS = 21;
//        /**  */
//        public static final int OP_WRITE_ICC_SMS = 22;
//        /**  */
//        public static final int OP_WRITE_SETTINGS = 23;
//        /**  */
//        public static final int OP_SYSTEM_ALERT_WINDOW = 24;
//        /**  */
//        public static final int OP_ACCESS_NOTIFICATIONS = 25; // todo: ----
//        /**  */
//        public static final int OP_CAMERA = 26;
//        /**  */
//        public static final int OP_RECORD_AUDIO = 27;
//        /**  */
//        public static final int OP_PLAY_AUDIO = 28;
//        /**  */
//        public static final int OP_READ_CLIPBOARD = 29;
//        /**  */
//        public static final int OP_WRITE_CLIPBOARD = 30;
//        /**  */
//        public static final int OP_TAKE_MEDIA_BUTTONS = 31;
//        /**  */
//        public static final int OP_TAKE_AUDIO_FOCUS = 32;
//        /**  */
//        public static final int OP_AUDIO_MASTER_VOLUME = 33;
//        /**  */
//        public static final int OP_AUDIO_VOICE_VOLUME = 34;
//        /**  */
//        public static final int OP_AUDIO_RING_VOLUME = 35;
//        /**  */
//        public static final int OP_AUDIO_MEDIA_VOLUME = 36;
//        /**  */
//        public static final int OP_AUDIO_ALARM_VOLUME = 37;
//        /**  */
//        public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
//        /**  */
//        public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
//        /**  */
//        public static final int OP_WAKE_LOCK = 40;
//        /**
//         * Continually monitoring location data.
//         */
//        public static final int OP_MONITOR_LOCATION = 41;
//        /**
//         * Continually monitoring location data with a relatively high power request.
//         */
//        public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
//        /**
//         * Retrieve current usage stats via
//         *///{@link UsageStatsManager}.
//        public static final int OP_GET_USAGE_STATS = 43;
//        /**  */
//        public static final int OP_MUTE_MICROPHONE = 44;
//        /**  */
//        public static final int OP_TOAST_WINDOW = 45;
//        /**
//         * Capture the device's display contents and/or audio
//         */
//        public static final int OP_PROJECT_MEDIA = 46;
//        /**
//         * Activate a VPN connection without user intervention.
//         */
//        public static final int OP_ACTIVATE_VPN = 47;
//        /**
//         * Access the WallpaperManagerAPI to write wallpapers.
//         */
//        public static final int OP_WRITE_WALLPAPER = 48;
//        /**
//         * Received the assist structure from an app.
//         */
//        public static final int OP_ASSIST_STRUCTURE = 49;
//        /**
//         * Received a screenshot from assist.
//         */
//        public static final int OP_ASSIST_SCREENSHOT = 50;
//        /**
//         * Read the phone state.
//         */
//        public static final int OP_READ_PHONE_STATE = 51;
//        /**
//         * Add voicemail messages to the voicemail content provider.
//         */
//        public static final int OP_ADD_VOICEMAIL = 52;
//        /**
//         * Access APIs for SIP calling over VOIP or WiFi.
//         */
//        public static final int OP_USE_SIP = 53;
//        /**
//         * Intercept outgoing calls.
//         */
//        public static final int OP_PROCESS_OUTGOING_CALLS = 54;
//        /**
//         * User the fingerprint API.
//         */
//        public static final int OP_USE_FINGERPRINT = 55;
//        /**
//         * Access to body sensors such as heart rate, etc.
//         */
//        public static final int OP_BODY_SENSORS = 56;
//        /**
//         * Read previously received cell broadcast messages.
//         */
//        public static final int OP_READ_CELL_BROADCASTS = 57;
//        /**
//         * Inject mock location into the system.
//         */
//        public static final int OP_MOCK_LOCATION = 58;
//        /**
//         * Read external storage.
//         */
//        public static final int OP_READ_EXTERNAL_STORAGE = 59;
//        /**
//         * Write external storage.
//         */
//        public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
//        /**
//         * Turned on the screen.
//         */
//        public static final int OP_TURN_SCREEN_ON = 61;
//        /**
//         * Get device accounts.
//         */
//        public static final int OP_GET_ACCOUNTS = 62;
//        /* Control whether an application is allowed to run in the background. */
//        public static final int OP_RUN_IN_BACKGROUND = 63;
//
//
//        public static final int[] RUNTIME_PERMISSIONS_OPS = {
//                // Contacts
//                OP_READ_CONTACTS,
//                OP_WRITE_CONTACTS,
//                OP_GET_ACCOUNTS,
//                // Calendar
//                OP_READ_CALENDAR,
//                OP_WRITE_CALENDAR,
//                // SMS
//                OP_SEND_SMS,
//                OP_RECEIVE_SMS,
//                OP_READ_SMS,
//                OP_RECEIVE_WAP_PUSH,
//                OP_RECEIVE_MMS,
//                OP_READ_CELL_BROADCASTS,
//                // Storage
//                OP_READ_EXTERNAL_STORAGE,
//                OP_WRITE_EXTERNAL_STORAGE,
//                // Location
//                OP_COARSE_LOCATION,
//                OP_FINE_LOCATION,
//                // Phone
//                OP_READ_PHONE_STATE,
//                OP_CALL_PHONE,
//                OP_READ_CALL_LOG,
//                OP_WRITE_CALL_LOG,
//                OP_ADD_VOICEMAIL,
//                OP_USE_SIP,
//                OP_PROCESS_OUTGOING_CALLS,
//                // Microphone
//                OP_RECORD_AUDIO,
//                // Camera
//                OP_CAMERA,
//                // Body sensors
//                OP_BODY_SENSORS
//        };
//
//        // todo:  sOpToSwitch[]
//    }
//
//    private static final android.support.v4.util.ArrayMap<String, Integer> PERMISSION_CONVERT = new android.support.v4.util.ArrayMap<>();
//
//    static {
//        PERMISSION_CONVERT.put(READ_CALENDAR, PermissionOps.OP_READ_CALENDAR);
//        PERMISSION_CONVERT.put(WRITE_CALENDAR, PermissionOps.OP_WRITE_CALENDAR);
//
//        PERMISSION_CONVERT.put(CAMERA, PermissionOps.OP_CAMERA);
//
//        PERMISSION_CONVERT.put(READ_CONTACTS, PermissionOps.OP_READ_CONTACTS);
//        PERMISSION_CONVERT.put(WRITE_CONTACTS, PermissionOps.OP_WRITE_CONTACTS);
//        PERMISSION_CONVERT.put(GET_ACCOUNTS, PermissionOps.OP_GET_ACCOUNTS);
//
//        PERMISSION_CONVERT.put(RECORD_AUDIO, PermissionOps.OP_RECORD_AUDIO);
//
//        PERMISSION_CONVERT.put(ACCESS_COARSE_LOCATION, PermissionOps.OP_COARSE_LOCATION);
//        PERMISSION_CONVERT.put(ACCESS_FINE_LOCATION, PermissionOps.OP_FINE_LOCATION);
//
//        PERMISSION_CONVERT.put(READ_PHONE_STATE, PermissionOps.OP_READ_PHONE_STATE);
//        PERMISSION_CONVERT.put(CALL_PHONE, PermissionOps.OP_CALL_PHONE);
//        PERMISSION_CONVERT.put(READ_CALL_LOG, PermissionOps.OP_READ_CALL_LOG);
//        PERMISSION_CONVERT.put(WRITE_CALL_LOG, PermissionOps.OP_WRITE_CALL_LOG);
//        PERMISSION_CONVERT.put(ADD_VOICEMAIL, PermissionOps.OP_ADD_VOICEMAIL);
//        PERMISSION_CONVERT.put(PROCESS_OUTGOING_CALLS, PermissionOps.OP_PROCESS_OUTGOING_CALLS);
//
//        PERMISSION_CONVERT.put(BODY_SENSORS, PermissionOps.OP_BODY_SENSORS);
//
//        PERMISSION_CONVERT.put(READ_EXTERNAL_STORAGE, PermissionOps.OP_READ_EXTERNAL_STORAGE);
//        PERMISSION_CONVERT.put(WRITE_EXTERNAL_STORAGE, PermissionOps.OP_WRITE_EXTERNAL_STORAGE);
//
//        PERMISSION_CONVERT.put(SEND_SMS, PermissionOps.OP_SEND_SMS);
//        PERMISSION_CONVERT.put(READ_SMS, PermissionOps.OP_READ_SMS);
//        PERMISSION_CONVERT.put(RECEIVE_SMS, PermissionOps.OP_RECEIVE_SMS);
//        PERMISSION_CONVERT.put(RECEIVE_WAP_PUSH, PermissionOps.OP_RECEIVE_WAP_PUSH);
//        PERMISSION_CONVERT.put(RECEIVE_MMS, PermissionOps.OP_RECEIVE_MMS);
//    }
//
//    //---------------------------------------------------------定位-------------------------------------------------------------
////    LocationManager：定位管理器
////    LocationProvider：定位提供者
////    Location：对位置信息的封装类
//    //  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//    //  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//    //  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
//    //  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//    //  <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
//    //  <uses-permission android:name="android.permission.INTERNET" />
//
//    /**
//     * 判断GPS或者AGPS是否打开, 如果没有相关的硬件则返回true
//     *
//     * @param context
//     * @return
//     */
//    public static final boolean isOpenGPS(final Context context) {
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager == null) {
//            return true;
//        }
//        List<String> allProviders = locationManager.getAllProviders();
//        if (allProviders == null || allProviders.size() <= 0) {
//            return true;
//        }
//        boolean hasGpsDevice = allProviders.contains(LocationManager.GPS_PROVIDER);
//        if (!hasGpsDevice) {
//            return true;
//        }
//
//        // 有相关硬件
//
//        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
//        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if (gps || network) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * 请求用户打卡GPS
//     *
//     * @param context
//     * @return true表示 进行了请求，false表示没有相关硬件
//     */
//    public static final boolean openGPS(Context context, int requestCode) {
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager == null) {
//            return false;
//        }
//        List<String> allProviders = locationManager.getAllProviders();
//        if (allProviders == null || allProviders.size() <= 0) {
////            ToastShow.toastShort("没有GPS相关的硬件");
//            return false;
//        }
//        boolean hasGpsDevice = allProviders.contains(LocationManager.GPS_PROVIDER);
//        if (!hasGpsDevice) {
////            ToastShow.toastShort("没有GPS相关的硬件");
//            return false;
//        }
//
////        Intent GPSIntent = new Intent();
////        GPSIntent.setClassName("com.android.settings",
////                "com.android.settings.widget.SettingsAppWidgetProvider");
////        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
////        GPSIntent.setShowTypeData(Uri.parse("custom:3"));
////        try {
////            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
////        } catch (PendingIntent.CanceledException e) {
////            e.printStackTrace();
////        }
//        gotoGPSSetting((Activity) context, requestCode);
//        return true;
//    }
//
//    // todo: 监听GPS开关状态
//    // getContentResolver()
////            .registerContentObserver(
////            Settings.Secure
////                    .getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
////                    false, mGpsMonitor);
//
//
//    /**
//     * 转到手机设置界面，用户设置GPS
//     */
//    public static final void gotoGPSSetting(Activity activity, int requestCode) {
//        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        activity.startActivityForResult(intent, requestCode); // 设置完成后返回到原来的界面
//    }
//
//
//    // --------------------------------------------------------通知栏权限------------------------------------------------------
//
//    // 通知栏权限 todo:  https://www.zhihu.com/question/35880013
//
//    /**
//     * android 4.4以下直接返回true
//     *
//     * @param ctx
//     * @return
//     */
//    public static final boolean notificationEnabled(Context ctx) {
//        return NotificationManagerCompat.from(ctx).areNotificationsEnabled();
//        // 或者反射解决 http://blog.csdn.net/zcllige/article/details/52444258
//    }
//
//    public static final void gotoNotificaitonSettings(Context context) {
////        Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS; // for android
//
//
////        Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
//        // 访问勿扰模式
//        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//
//
////        NotificationManagerCompat.getEnabledListenerPackages()
//
//        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            boolean notificationPolicyAccessGranted = nm.isNotificationPolicyAccessGranted();
//
//        }
//    }
//
//
//    // 通知使用权--- 监听通知栏
////     api 18 : Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
//    // AppNotificationSettingsActivity    http://www.importnew.com/7663.html
//
//    public static void requestAccessNotifacaiton(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
//            if (AppUtil.isIntentAvailable(intent, activity)) {
//                activity.startActivity(intent);
//            } else {
////                ToastShow.toastShort("您的手机不支持此操作");
//            }
//        }
//
//    }
//
//    /**
//     * 是否被用户允许监听通知栏
//     * {@see https://stackoverflow.com/questions/22663359/redirect-to-notification-access-settings}
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isNotificationServiceRunning(Context context) {
//        ContentResolver contentResolver = context.getContentResolver();
//        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
//        String packageName = context.getPackageName();
//        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
//    }
//
//
//    // 通知显示等级： Manifest.permission.ACCESS_NOTIFICATION_POLICY
//
//    // -------------------------------------------------------------------- battery saver ------------------------------------------
//
//    // 电池优化设置
////        Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;
////
////        // todo: 请求用户忽略电池优化
////        Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;
//
//    // 省电设置
////        Settings.ACTION_BATTERY_SAVER_SETTINGS;
//
//    /**
//     * 判断应用是否被添加至省电白名单
//     *
//     * @param context
//     * @return
//     */
//    @RequiresApi(23)
//    public static final boolean isIgnoreBatterySaver(@NonNull Context context) {
//        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        // android 5.0
//        powerManager.isPowerSaveMode();
//        // android 6.0
//        return powerManager.isIgnoringBatteryOptimizations(context.getApplicationContext().getPackageName());
//    }
//
//    /**
//     * 请求忽略电池优化
//     *
//     * @param context
//     */
//    @TargetApi(23)
//    public static final void requestIgnoreBatterySaver(@NonNull Context context) {
//        final String packagename = context.getApplicationContext().getPackageName();
//        // 显示电池优化白名单列表
//        // todo: ???
////        Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
//        // 显示系统弹框允许用户添加应用至电池优化白名单
//        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//        intent.setData(Uri.parse("package:" + packagename));
//        if (AppUtil.isIntentAvailable(intent, context)) {
//            context.startActivity(intent);
//        } else {
////            ToastShow.toastShort("您的手机不支持此操作！");
//        }
//    }
//
//    // ---------------------------------------------------------------------- data saver -----------------------------------------
//    // 后台数据限制
////        Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS;
//
//    /**
//     * 请求忽略后台数据优化
//     *
//     * @param context
//     */
//    public static final void requesBackgroundtDataSaver(@NonNull Context context) {
//        final String packagename = context.getApplicationContext().getPackageName();
//        // 显示电池优化白名单列表
////        Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
//        // 显示系统弹框允许用户添加应用至电池优化白名单
//        Intent intent = new Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS);
//        intent.setData(Uri.parse("package:" + packagename));
//        if (AppUtil.isIntentAvailable(intent, context)) {
//            context.startActivity(intent);
//        } else {
////            ToastShow.toastShort("您的手机不支持此操作！");
//        }
//    }
//    // ------------------------------------------------------------ DO NOT DISTURB -------------------------------------------------
//    // Android 6.0设置模块免打扰功能浅析
//    // http://blog.csdn.net/ffforfun09/article/details/50447857
//    // Android M Zen mode（勿扰）分析
//    // http://blog.csdn.net/guduxiake1106/article/details/51065633
////    Android7.1勿扰功能简析
//    // http://blog.csdn.net/tubby_ting/article/details/54924392
//    // RingerMode设置和勿扰模式的关系
////    http://blog.csdn.net/niexu19900104/article/details/52370773
//
//
//    public static void test(Context context) {
//        NotificationManagerCompat.from(context).cancelAll();
//
//
//    }
//
//
//    // --------------------------------------修改手机设置------------------------------------------
//
//    /**
//     * 请求用户设置应用修改系统设置的权限
//     *
//     * @param context
//     */
//    @RequiresApi(23)
//    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
//    public static void requestWriteSettings(@NonNull Activity context, int requestCode) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//            intent.setData(Uri.parse("package:" + context.getPackageName()));
//            context.startActivityForResult(intent, requestCode);
//        }
//    }
//
//    /**
//     * 判断是否具有修改手机设置的权限
//     *
//     * @param context
//     * @return
//     */
//    @RequiresApi(19)
//    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
//    private static boolean canWriteSysSettings(Activity context, boolean needRequest, int requestCode) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.System.canWrite(context)) {
//                return true;
//            } else {
//                if (needRequest) {
//                    requestWriteSettings(context, requestCode);
//                }
//                return false;
//            }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            return 1 != checkOp(context, PermissionOps.OP_WRITE_SETTINGS);
//        } else {
//            return true;
//        }
//    }
//
//    // ----------------------------------------悬浮窗权限(在其他应用之上显示)-----------------------------------------------
////    http://blog.csdn.net/self_study/article/details/52859790
//
//    // 建议使用无权限悬浮窗
//    public static boolean canOverlayDraw(@NonNull Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return Settings.canDrawOverlays(context);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            return checkOPCompact(context, PermissionOps.OP_SYSTEM_ALERT_WINDOW);
//        } else {
//            return true;
//        }
//    }
//
//    @RequiresApi(23)
//    @RequiresPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
//    private static void requestOverlayDraw(@NonNull Activity context, int requestCode) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//            intent.setData(Uri.parse("package:" + context.getPackageName()));
//            context.startActivityForResult(intent, requestCode);
//        }
//    }
//
//    /**
//     * 查看所有要求顶层绘制的app
//     *
//     * @param context
//     */
//    public static void requestOverlayDraw(Context context) {
//        Intent intent = new Intent();
//        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (AppUtil.isIntentAvailable(intent, context)) {
//            context.startActivity(intent);
//        }
//    }
//
//    /**
//     * 判断悬浮窗权限
//     * <p>
//     * todo: 未测试
//     *
//     * @param context
//     * @return
//     */
//    @Deprecated
//    public static boolean isFloatWindowOpAllowed(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            return checkOp(context, PermissionOps.OP_SYSTEM_ALERT_WINDOW) != 1;  // AppOpsManager.OP_SYSTEM_ALERT_WINDOW
//        } else {
//            if ((context.getApplicationInfo().flags & 1 << 27) == 1 << 27) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
//
//    /**
//     * 请求悬浮窗权限
//     *
//     * @param context
//     * @param requestCode
//     * @return true表示api23跳转界面
//     */
//    @RequiresPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
//    public static final boolean requestOverlayPermission(Activity context, int requestCode) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            Log.i(TAG, "无需请求悬浮窗权限，清单文件中声明即可");
//            return true;
//        }
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            JumpPermissionManagement.GoToSetting(context);
//            return false;
//        } else {
//            requestOverlayDraw(context, requestCode);
//            return true;
//        }
//    }
//
//// -----------------------------------------------------------------------------------------------------------
//
//    public interface IGoToSettingListener {
//        void gotoSetting();
//    }
//
//}
