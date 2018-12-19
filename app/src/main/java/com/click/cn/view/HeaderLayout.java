package com.click.cn.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.click.cn.R;

public class HeaderLayout extends LinearLayout {
    private Context mContext;
    private ImageView backImageView;
    private ImageView moreImageView;
    private TextView title;

    private ClickEvent mClickEvent;
    private TextView leftTextMenu;
    private TextView rightTextMenu;
    private ImageView titleRightImageView;
    private ImageView titleRight02ImageView;

    private View left_wrapper, right_wrapper;

    public HeaderLayout(Context context) {
        this(context, null);
    }

    public HeaderLayout(Context context, @Nullable AttributeSet attrs) {
        // fixme: 0改为 当前主题主题样式
        this(context, attrs, 0);
    }

    public HeaderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        initView();
        addListener();
    }

    private void initView() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.common_view_head, this, true);
        backImageView = contentView.findViewById(R.id.left_back);
        moreImageView = contentView.findViewById(R.id.img_right_more);
        title = contentView.findViewById(R.id.header_title);
        leftTextMenu = contentView.findViewById(R.id.tv_left);
        rightTextMenu = contentView.findViewById(R.id.tv_right);
        titleRightImageView = contentView.findViewById(R.id.img_right_01);
        titleRight02ImageView = contentView.findViewById(R.id.img_right_02);

        titleRightImageView.setVisibility(View.GONE);
        titleRight02ImageView.setVisibility(View.GONE);

        this.left_wrapper = contentView.findViewById(R.id.left_wrapper);
        this.right_wrapper = contentView.findViewById(R.id.right_wrapper);
    }

    private void addListener() {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null == mClickEvent) {
                    return;
                }

                if (v.getId() == R.id.left_back || v.getId() == R.id.tv_left || v.getId() == R.id.left_wrapper) {
                    mClickEvent.backClick(v);
                } else if (v.getId() == R.id.header_title) {
                    mClickEvent.titleClick(v);
                } else if (v.getId() == R.id.img_right_more || v.getId() == R.id.tv_right || v.getId() == R.id.right_wrapper) {
                    mClickEvent.moreClick(v);
                }
            }
        };
        this.left_wrapper.setOnClickListener(onClickListener);
//        backImageView.setOnClickListener(onClickListener);
//        moreImageView.setOnClickListener(onClickListener);
//        leftTextMenu.setOnClickListener(onClickListener);
//        rightTextMenu.setOnClickListener(onClickListener);
        title.setOnClickListener(onClickListener);

        this.right_wrapper.setOnClickListener(onClickListener);
    }

    public void setTitle(@StringRes int strId) {
        this.title.setText(strId);
    }

    public void setTitle(String str) {
        this.title.setText(str);
    }

    public void setTitle(String str, @ColorInt int colorInt) {
        this.title.setText(str);
        this.title.setTextColor(colorInt);
    }

    public void setTitleColor(@ColorInt int colorInt) {
        this.title.setTextColor(colorInt);
    }


    public void setEventListener(ClickEvent clickEvent) {
        if (null == clickEvent) {
            return;
        }

        this.mClickEvent = clickEvent;
    }

    public void release() {
        this.mClickEvent = null;
        backImageView.setOnClickListener(null);
        moreImageView.setOnClickListener(null);
        title.setOnClickListener(null);

        this.left_wrapper.setOnClickListener(null);
        this.right_wrapper.setOnClickListener(null);
    }

    public void showBackView() {
        backImageView.setVisibility(View.VISIBLE);
    }

    public void hideBackView() {
        backImageView.setVisibility(View.GONE);
    }

    public void showMoreView() {
        moreImageView.setVisibility(View.VISIBLE);
    }

    public void hideMoreview() {
        moreImageView.setVisibility(View.GONE);
    }


    public void setRightMenuIcon(@DrawableRes int drawableId) {
        moreImageView.setImageResource(drawableId);
        moreImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        moreImageView.setVisibility(View.VISIBLE);
    }

    public void performRightLick() {
        right_wrapper.performClick();
    }

    public interface ClickEvent {
        void backClick(View v);

        void titleClick(View v);

        void moreClick(View v);
    }

    public void setBackImg(boolean darkMode) {
        backImageView.setImageResource(darkMode ? R.drawable.action_back_white : R.drawable.action_back);
    }

    public void setBackTextImage(@StringRes int leftTextId) {
        this.showBackView();
        this.leftTextMenu.setVisibility(View.VISIBLE);
        this.leftTextMenu.setText(leftTextId);
    }

    public void setBackTextImage(@android.support.annotation.NonNull String leftText) {
        this.showBackView();
        this.leftTextMenu.setVisibility(View.VISIBLE);
        this.leftTextMenu.setText(leftText);
    }


    /**
     * 设置文本类型菜单
     *
     * @param leftText
     * @param rightText
     */
    public void setTextMenus(@android.support.annotation.NonNull String leftText, @android.support.annotation.NonNull String rightText) {
        hideBackView();
        hideMoreview();
        leftTextMenu.setVisibility(View.VISIBLE);
        rightTextMenu.setVisibility(View.VISIBLE);
        leftTextMenu.setText(leftText);
        rightTextMenu.setText(rightText);
    }

    /**
     * 设置文本类型菜单
     *
     * @param leftTextId
     * @param rightTextId
     */
    public void setTextMenus(@StringRes int leftTextId, @StringRes int rightTextId) {
        hideBackView();
        hideMoreview();
        leftTextMenu.setVisibility(View.VISIBLE);
        rightTextMenu.setVisibility(View.VISIBLE);
        leftTextMenu.setText(leftTextId);
        rightTextMenu.setText(rightTextId);
    }


    public void setRightTextMenu(@NonNull String rightText) {
        hideMoreview();
        rightTextMenu.setVisibility(View.VISIBLE);
        rightTextMenu.setText(rightText);
    }

    public void setTitleRightImg(@DrawableRes int drawableResId) {
        titleRightImageView.setVisibility(View.VISIBLE);
        titleRightImageView.setImageResource(drawableResId);
    }

    public void setTitleRight02Img(@DrawableRes int drawableResId) {
        titleRight02ImageView.setVisibility(View.VISIBLE);
        titleRight02ImageView.setImageResource(drawableResId);
    }

    public void setTitleRightImageVisible(int visibleInt) {
        titleRightImageView.setVisibility(visibleInt);
    }

    public void setTitleRight02ImageVisible(int visibleInt) {
        titleRight02ImageView.setVisibility(visibleInt);
    }
}
