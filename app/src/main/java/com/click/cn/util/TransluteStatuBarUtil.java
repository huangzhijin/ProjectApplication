package com.click.cn.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by vito-xa49 on 2017/12/5.
 * 透明状态栏工具
 * <p>
 * 预备知识
 * 状态栏着色
 * 状态栏模式：亮色模式\暗色模式
 * <p>
 * <p>
 * 使用：
 * onWindowFocusChanged()中调用相关方法
 */
public class TransluteStatuBarUtil {

    private static final String TAG = TransluteStatuBarUtil.class.getSimpleName();
    private static int STATUS_BAR_BG_COLOR = Color.WHITE;

    private TransluteStatuBarUtil() {
    }

    /**
     * 设置状态栏透明化（布局延伸至状态栏底部）,全屏引发Android 5497bug
     *
     * @param activity
     * @param statusTxtBlack
     */
//    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void transparentStatusBar(@NonNull Activity activity, boolean statusTxtBlack) {
        AndroidBug5497Backgroud.assistActivity(activity);
        transparentStatusBar(activity, statusTxtBlack, STATUS_BAR_BG_COLOR);
    }

    @Deprecated
    public static void transparentStatusBar(@NonNull Dialog dialog, boolean statusTxtBlack) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = dialog.getWindow();
        if (null == window) {
            Log.wtf(TAG, "透明化设置过程中window为null");
            return;
        }

        transparent(window, true, false,
                false, statusTxtBlack, false, false);
    }

    private static void transparentStatusBar(@NonNull Activity activity, boolean statusTxtBlack, @ColorInt int statusBarBgColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        Window window = activity.getWindow();
        if (null == window) {
            Log.wtf(TAG, "透明化设置过程中window为null");
            return;
        }
        STATUS_BAR_BG_COLOR = statusBarBgColor;
//        setStatusBarBgColorForN(activity.getWindow(), true, 0);
        transparent(window, true, false,
                false, statusTxtBlack, false, false);
    }

    private static void transparent(@Nullable final Window window, final boolean transparentStatusBar,
                                    final boolean transparentNavigationBar, boolean fitsSystemWindows,
                                    final boolean statusContentBlack, final boolean absolute, final boolean needStatusBarHolder) {

        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)) {
            return;
        }

        // setContentView()设置的内容
        final ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup viewGroup = (ViewGroup) decorView.findViewById(android.R.id.content);
        if (null == viewGroup || viewGroup.getChildCount() <= 0) {
            Log.wtf(TAG, "沉浸化activity未设置布局");
            return;
        }

        final View contentView = viewGroup.getChildAt(0);

        //1.是否保留系统控件（状态栏）的空间(无需设置)
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        contentView.setFitsSystemWindows(fitsSystemWindows);
//        } else {
//            contentView.setFitsSystemWindows(false);
//        }

        // 2.状态栏透明化
        commonTranslute(window, decorView, contentView, transparentStatusBar, transparentNavigationBar);

        // 3.状态栏字体反色处理
        statusBarLightMode(window, statusContentBlack);
    }

    private static void commonTranslute(@NonNull Window window, final View decorView, final View contentView,
                                        boolean transparentStatusBar, boolean transparentNavigationBar) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //            Log.i(TAG, "透明化状态栏之前： FitsSystemWindows = " + contentView.getFitsSystemWindows());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 与fitsSystemWindows 无关
            if (transparentStatusBar) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                int uiOptions = decorView.getSystemUiVisibility();
                decorView.setSystemUiVisibility(uiOptions & ~View.SYSTEM_UI_FLAG_FULLSCREEN);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                int uiOptions = decorView.getSystemUiVisibility();
                decorView.setSystemUiVisibility(uiOptions & ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            // transparentNavigationBar 不处理
        } else {
            if (transparentStatusBar) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (transparentNavigationBar) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }

            if (!transparentNavigationBar && !transparentNavigationBar) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
//            Log.i(TAG, "透明化状态栏之后： FitsSystemWindows = " + contentView.getFitsSystemWindows());

        contentView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                decorView.requestLayout();
                contentView.requestLayout();
            }
        }, (int) (0.005 * 1000));
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0（基于Android 4.4.4）以上
     * fixme: 低版本无效果，与官方描述不符
     *
     * @param activityWindow 需要设置的窗口
     * @param dark           是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setStatusBarLightModeForFlyme(@NonNull Window activityWindow, boolean dark) {

        if (!SysUtl.Flyme.isFlyme()) {
            return false;
        }

        FlymeStatusbarColorUtils.setStatusBarDarkIcon(activityWindow, dark);
        return true;
    }

    /**
     * MIUI 7.7.13之前设置状态栏黑色字体的方式
     *
     * @param activityWindow
     * @param darkContentMode 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setStatusBarLightModeForMIUI(@NonNull Window activityWindow, boolean darkContentMode) {

        if (SysUtl.MIUI.isMIUI()) {
            //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
            if (SysUtl.MIUI.supportTransluteStdApi()) {
                if (darkContentMode) {
                    int flag = activityWindow.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    activityWindow.getDecorView().setSystemUiVisibility(flag);
                } else {
                    int flag = activityWindow.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    activityWindow.getDecorView().setSystemUiVisibility(flag);
                }
            }
            // 7.7.13 之前
            Class<? extends Window> clazz = activityWindow.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activityWindow, darkContentMode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {

            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * 状态栏亮色模式,即设置状态栏黑色文字和图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param window
     * @return 1:MIUUI 2:Flyme 3:原生android6.0
     */
    public static int statusBarLightMode(Window window, boolean darkContentMode) {

        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (setStatusBarLightModeForMIUI(window, darkContentMode)) {
                result = 1;
            } else if (setStatusBarLightModeForFlyme(window, darkContentMode)) {
                result = 2;
            } else if (SysUtl.OPPP_ROM.isOppo()) {
                setLightStatusBarIconForOppo(window, darkContentMode);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 原生api --- android 6.0以上设置状态栏亮色模式
                View decorView = window.getDecorView();
                int flag = decorView.getSystemUiVisibility();
                if (darkContentMode) {
                    flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    flag &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(flag);
                result = 3;
            }
        } else {
            // do nothing
        }

        return result;
    }

    /**
     * 兼容oppo手机的状态栏字体反色设置（todo: 经过测试android 4.4.4上无效，android 5.0有效）
     *
     * @param activityWindow
     * @param darkContentMode
     */
    private static void setLightStatusBarIconForOppo(@NonNull Window activityWindow, boolean darkContentMode) {
        final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010;
        int vis = activityWindow.getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activityWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (darkContentMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (darkContentMode) {
                vis |= SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            } else {
                vis &= ~SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            }
        }
        activityWindow.getDecorView().setSystemUiVisibility(vis);

    }

    /**
     * 视频播放全屏化
     *
     * @param activity
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void immersive(@NonNull Activity activity, boolean showNavigationView) {
        final Window window = activity.getWindow();
        if (null == window) {
            Log.wtf(TAG, "沉浸化化设置过程中window为null");
            return;
        }

        final View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (showNavigationView) {

            } else {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            }
        }
    }
}
