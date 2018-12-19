package com.click.cn.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.click.cn.R;
import com.click.cn.util.DensityUtils;
import com.click.cn.view.HeaderLayout;

public abstract class BaseFragment extends Fragment implements ICommonAction {
    protected String logTag = "";
    @LayoutRes
    public static final Integer NO_LAYOUT_ID = 0;
    protected Context mContext;
    protected HeaderLayout mToolbar;
    protected ViewGroup mContainerView;
    private ProgressBar mProgressBar;

    @Nullable
    protected View mContentView;

    @Override
    public void onAttach(Context context) {
        logTag = this.getClass().getSimpleName();
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        MyApplication.getAppContext().getResources().getIdentifier()
        View rootView = layoutInflater.inflate(R.layout.layout_fragment_base, container, false);
        mToolbar = rootView.findViewById(R.id.tool_bar);
        mContainerView = rootView.findViewById(R.id.fg_view_container);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        initHeader();
        if (!NO_LAYOUT_ID.equals(getLayoutId())) {
            mContentView = layoutInflater.inflate(getLayoutId(), null, false);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mContainerView.addView(mContentView, 0, layoutParams);
        } else {
            Log.i(logTag, "未设置布局资源");
        }
        int height = DensityUtils.dppx(mContext, 41F);
        mToolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bg_status_bar_default));
        ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
        if (null == layoutParams) {
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        } else {
            layoutParams.height = height;
        }
        mToolbar.setLayoutParams(layoutParams);
        mToolbar.setBackImg(true);
        findViews();
        setListener();
        initContent();
        return rootView;
    }

    @Override
    public int getLayoutId() {
        return NO_LAYOUT_ID;
    }

    @Override
    public void showProgess() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }


    protected void closeSoftInput() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (getActivity() != null) {
                if (getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    public void setHeaderBackground(@ColorRes int colorRes) {
        final int colorInt = ContextCompat.getColor(mContext, colorRes);
        mToolbar.setBackgroundColor(colorInt);
    }

    public void setHeaderBackgroundInt(@ColorInt int colorInt) {
        mToolbar.setBackgroundColor(colorInt);
    }

    public View findViewById(@IdRes int id) {
        return this.mContainerView.findViewById(id);
    }
}
