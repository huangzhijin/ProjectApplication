package com.click.cn.base;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.click.cn.R;
import com.click.cn.util.AppUtil;
import com.click.cn.util.DensityUtils;
import com.click.cn.util.TransluteStatuBarUtil;
import com.click.cn.view.HeaderLayout;

public abstract class BaseActivity extends AppCompatActivity implements ICommonAction{


    protected String logTag;
    private View mStatusBarPlaceHolder;
    protected HeaderLayout mToolBar;
    private ViewGroup mProgressWrapper;
    private ProgressBar mProgressBar;
    protected View mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        logTag = this.getClass().getSimpleName();
        super.onCreate(savedInstanceState);
        setWindowBackground(R.color.bg_window_default);
        setContentView(R.layout.layout_activity_base);
        TransluteStatuBarUtil.transparentStatusBar(this, false);
        mStatusBarPlaceHolder = findViewById(R.id.view_status_bar_holder);
        mStatusBarPlaceHolder.getLayoutParams().height = (int) (AppUtil.getStatusBarHeight(this) + 0.5F);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarPlaceHolder.setVisibility(View.VISIBLE);
        } else {
            mStatusBarPlaceHolder.setVisibility(View.GONE);
        }
        mToolBar = findViewById(R.id.tool_bar);
        int height = DensityUtils.dppx(this, 41F);
        setPlaceStatusBarViewColor(R.color.bg_status_bar_default);
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_status_bar_default));
        ViewGroup.LayoutParams layoutParams = mToolBar.getLayoutParams();
        if (null == layoutParams) {
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        } else {
            layoutParams.height = height;
        }
        mToolBar.setLayoutParams(layoutParams);
        mToolBar.setBackImg(true);
        mProgressWrapper = findViewById(R.id.ll_progress);
        mProgressBar = findViewById(R.id.progress_bar);
        RelativeLayout containerView = findViewById(R.id.activity_container);
        initHeader();
//        ViewGroup contentWrapper = (ViewGroup) ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
        if (!BaseFragment.NO_LAYOUT_ID.equals(getLayoutId())) {
            mContentView = LayoutInflater.from(this).inflate(getLayoutId(), null);
            RelativeLayout.LayoutParams contentViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            containerView.addView(mContentView, 0, contentViewLayoutParams);
        }
        findViews();
        setListener();
        initContent();
    }

    @Override
    public void showProgess() {
        if (null != mProgressWrapper) {
            mProgressWrapper.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (null != mProgressWrapper) {
            mProgressWrapper.setVisibility(View.GONE);
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void setPlaceStatusBarViewColor(@ColorRes int colorResId) {
        mStatusBarPlaceHolder.setBackground(new ColorDrawable(ContextCompat.getColor(this, colorResId)));
    }

    public void setPlaceStatusBarViewColorInt(@ColorInt int colorInt) {
        mStatusBarPlaceHolder.setBackground(new ColorDrawable(colorInt));
    }

    public void setStatusToolBarColor(@ColorRes int colorResId) {
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(this, colorResId));
        this.mStatusBarPlaceHolder.setBackground(colorDrawable);
        this.mToolBar.setBackground(colorDrawable);
    }

    public void setStatusToolBarColorInt(@ColorInt int colorInt) {
        ColorDrawable colorDrawable = new ColorDrawable(colorInt);
        this.mStatusBarPlaceHolder.setBackground(colorDrawable);
        this.mToolBar.setBackground(colorDrawable);
    }

    public void forceSetPlaceStatusBarViewVisible(boolean isShow) {
        mStatusBarPlaceHolder.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置window为白色
     */
    public void setWindowBackgroundWhite() {
        final int white = ContextCompat.getColor(this, android.R.color.white);
        getWindow().setBackgroundDrawable(new ColorDrawable(white));
    }

    /**
     * 设置widow 灰色背景
     */
    public void setWindowBackgroundGray() {
        final int gray = ContextCompat.getColor(this, R.color.common_bg_color);
        getWindow().setBackgroundDrawable(new ColorDrawable(gray));
    }

    /**
     * 设置window背景为指定颜色
     *
     * @param colorRes
     */
    public void setWindowBackground(@ColorRes int colorRes) {
        final int bgColor = ContextCompat.getColor(this, colorRes);
        getWindow().setBackgroundDrawable(new ColorDrawable(bgColor));
    }

    public void setWindowBackgroundInt(@ColorInt final int colorInt) {
        getWindow().setBackgroundDrawable(new ColorDrawable(colorInt));
    }

    /**
     * 去除window背景颜色
     */
    public void dropWindowBackground() {
        getWindow().setBackgroundDrawable(new ColorDrawable());
    }

    public void setHeaderBackground(@ColorRes int colorRes) {
        final int colorInt = ContextCompat.getColor(this, colorRes);
        mToolBar.setBackgroundColor(colorInt);
        mStatusBarPlaceHolder.setBackgroundColor(colorInt);
    }

    public void setHeaderBackgroundInt(@ColorInt int colorInt) {
        mToolBar.setBackgroundColor(colorInt);
        mStatusBarPlaceHolder.setBackgroundColor(colorInt);
    }

    protected void fullScreen() {
        mToolBar.setVisibility(View.GONE);
        forceSetPlaceStatusBarViewVisible(false);
    }



}
