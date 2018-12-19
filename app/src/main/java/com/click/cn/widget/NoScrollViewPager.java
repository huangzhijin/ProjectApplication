package com.click.cn.widget;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可禁止滚动的ViewPager
 */
public class NoScrollViewPager extends ViewPager {

    private boolean isCanScroll = false;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 是否禁止滚动
     *
     * @param isCanScroll false 表示禁止滚动
     */
    public void setScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);
    }


    // 伪代码
//    public boolean dispatchTouchEvent(MotionEvent ev)｛
//    boolean consume = false;
//    if(onInterceptTouchEvent(ev)){
//        consume = onTouchEvent(ev);
//    }else{
//        consume = child.dispatchTouchEvent(ev);
//    }
//
//    return consume;
//｝


    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

    }
}
