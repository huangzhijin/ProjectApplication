package com.click.cn.base;

import android.support.annotation.LayoutRes;

public interface ICommonAction {

    @LayoutRes
    int getLayoutId();

    void findViews();

    void initHeader();

    void initContent();

    void setListener();

    void showProgess();

    void hideProgress();

}
