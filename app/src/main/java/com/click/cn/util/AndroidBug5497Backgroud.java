package com.click.cn.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by vito-xa49 on 2017/12/27.
 * 解决Android系统级bug 5497 (从Android 1.X时代就存在这个问题，官方一直没有给出解决方案)
 * 这个类用于解决全屏模式、或者透明状态栏、沉浸式状态栏导致的adjustResize、adjustSpan失效问题
 * <p>
 * 详情见：
 *
 * @see <a href="https://issuetracker.google.com/issues/36911528">google issue tracker</a>
 * @see <a href="https://stackoverflow.com/questions/7417123/android-how-to-adjust-layout-in-full-screen-mode-when-softkeyboard-is-visible/19494006#19494006">
 * https://stackoverflow.com/questions/7417123/android-how-to-adjust-layout-in-full-screen-mode-when-softkeyboard-is-visible/19494006#19494006
 * </a>
 */

public class AndroidBug5497Backgroud {
    private static final String TAG = AndroidBug5497Backgroud.class.getSimpleName();

    private Activity activity;
    private View navgView;
    private int fixedVirtualNavigationBarHeight;
    private int virtualNavigationBarHeight;
    private int virtualNavigationBarHeightPrevious;
    private int statusBarHeight;
    private View mChildOfContent; // setContentView()的root view
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private int screenOrientationPrevious = Configuration.ORIENTATION_UNDEFINED;
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    private int compensationValue; // 虚拟状态栏补偿值
    private View decorView;
    private int screenHeight;

    /**
     * @param activity
     */
    public static void assistActivity(@NonNull Activity activity) {
        new AndroidBug5497Backgroud(activity);
    }

    private AndroidBug5497Backgroud(final Activity activity) {
        this.activity = activity;
        decorView = AndroidBug5497Backgroud.this.activity.getWindow().getDecorView();
//        printTreeView(decorView);
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
//                int screenHeight = DensityUtil.getScreenHeight();
//                Log.i(TAG, "screenHeight:" + screenHeight + "   screenHeight:" + AppUtil.getScreenHeight(activity.getApplicationContext())
//                        + "   decorViewHeight:" + decorView.getHeight() + "   mChildOfContent:" + mChildOfContent.getHeight());
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
        fixedVirtualNavigationBarHeight = (int) (AppUtil.getVirtualNavigationBarHeight(activity) + 0.5);
        statusBarHeight = (int) (AppUtil.getStatusBarHeight(activity) + 0.5);
//        Log.i(TAG, "navgHeight" + fixedVirtualNavigationBarHeight + "   statusBarHeight:" + statusBarHeight);
//        Log.i(TAG, "screenHeight:" + screenHeight + "   decorViewHeight:" + decorView.getHeight() + "   mChildOfContent:" + mChildOfContent.getHeight());
        screenHeight = AppUtil.getScreenHeight(activity.getApplicationContext());
    }

    private void possiblyResizeChildOfContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 需要多次获取，存在为null的情况
            navgView = this.activity.getWindow().getDecorView().findViewById(android.R.id.navigationBarBackground);
            compensationValue = (navgView == null ? DensityUtils.dppx(BaseAppContextRef.getAppContext(), 28F) : 0);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (screenHeight == decorView.getHeight()) { // 无虚拟状态栏
                compensationValue = DensityUtils.dppx(BaseAppContextRef.getAppContext(), 28F);
            } else {
                compensationValue = 0;
            }
        } else {
            compensationValue = 0;
        }
        virtualNavigationBarHeight = (navgView == null ? 0 : navgView.getHeight());
        int usableHeightNow = computeUsableHeight();
        boolean needChangeLayoutParams = false;
        if (activity.getApplication().getResources().getConfiguration().orientation != screenOrientationPrevious) {
            needChangeLayoutParams = true;
        } else if (usableHeightNow != usableHeightPrevious) {
            needChangeLayoutParams = true;
        } else if (virtualNavigationBarHeight != virtualNavigationBarHeightPrevious) {
            needChangeLayoutParams = true;
        } else {
            needChangeLayoutParams = false;
        }

        if (needChangeLayoutParams) {
            int usableHeightSansKeyboard = decorView/*mChildOfContent*/.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow; // 被软键盘遮挡的高度
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
                        + fixedVirtualNavigationBarHeight
                        + compensationValue;

                if (compensationValue == 0) {
                    frameLayoutParams.height -= ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? statusBarHeight : 0);
                } else {

                }

            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ?
                        (int) (usableHeightSansKeyboard - virtualNavigationBarHeight + 0.5)
                        : usableHeightSansKeyboard - statusBarHeight;
            }

            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
            virtualNavigationBarHeightPrevious = virtualNavigationBarHeight;
        } else {
            // do nothing
        }
    }

    /**
     * 获取可视区域
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        decorView.getWindowVisibleDisplayFrame(r);
        return r.height();
    }

    public static final void release() {

    }

    private void printTreeView(View decorView) {
        if (decorView == null || !ViewGroup.class.isInstance(decorView)) {
            return;
        }

        for (int index = 0; index < ((ViewGroup) decorView).getChildCount(); index++) {
            View childView = ((ViewGroup) decorView).getChildAt(index);
            if (ViewGroup.class.isInstance(childView)) {
                Log.i(TAG, "ViewGroup:" + childView.toString());
                printTreeView(childView);
            } else {
                Log.i(TAG, "View:" + childView.toString());
            }
        }
    }
}
