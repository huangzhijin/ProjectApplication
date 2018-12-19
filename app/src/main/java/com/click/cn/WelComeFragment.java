package com.click.cn;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.click.cn.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;

public class WelComeFragment  extends Fragment {
    public static final String EXTRA_CENTER_DRAWABLE_ID = "center_drawable_id";
    public static final String EXTRA_BOTTOM_DRAWABLE_ID = "bottom_drawable_id";
    public static final String EXTRA_IS_LAST_PAGE = "is_last_page";


    private static final int TYPE_MSG_COUNT = 12;

    private SimpleDraweeView centerDraweeView;
    private SimpleDraweeView bottomDraweeView;
    private TextView countDownTimerView;
    private ViewGroup countDownTimerLayout;
    private Handler mHandler;

    public static WelComeFragment getInstance(int centerDrawableId, int bottomDrawableId, boolean isLastPage) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_CENTER_DRAWABLE_ID, centerDrawableId);
        bundle.putInt(EXTRA_BOTTOM_DRAWABLE_ID, bottomDrawableId);
        bundle.putBoolean(EXTRA_IS_LAST_PAGE, isLastPage);
        WelComeFragment introFragment = new WelComeFragment();
        introFragment.setArguments(bundle);
        return introFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.layout_fragment_welcome, container, false);
        centerDraweeView = contentView.findViewById(R.id.drawee_view_center);
        bottomDraweeView = contentView.findViewById(R.id.drawee_view_bottom);
        countDownTimerLayout = contentView.findViewById(R.id.ll_count_down);
        countDownTimerView = contentView.findViewById(R.id.tv_count_down);

        Bundle arguments = getArguments();
        if (null == arguments) {
            // do nothing
        } else {
            int centerDrawableId = arguments.getInt(EXTRA_CENTER_DRAWABLE_ID);
            int bottomDrawableId = arguments.getInt(EXTRA_BOTTOM_DRAWABLE_ID);
//            Log.i("queryGroupChatSettingsFromNet", "centerDrawableId: " + centerDrawableId + " bottomDrawableId: " + bottomDrawableId);
            boolean isLastPage = arguments.getBoolean(EXTRA_IS_LAST_PAGE, false);
            countDownTimerLayout.setVisibility(isLastPage ? View.VISIBLE : View.GONE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), bottomDrawableId, options);
            int bottomBitmapHeight = options.outHeight;
            ViewGroup.LayoutParams layoutParams = bottomDraweeView.getLayoutParams();
            if (null == layoutParams) {
                layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, bottomBitmapHeight);
            } else {
                layoutParams.height = bottomBitmapHeight;
            }
            bottomDraweeView.setLayoutParams(layoutParams);

            FrescoUtil.loadDrawableId(centerDraweeView, centerDrawableId);
            FrescoUtil.loadDrawableId(bottomDraweeView, bottomDrawableId);


            if (isLastPage) {

                countDownTimerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != getActivity()) {
//                            LoginActivity.start(getActivity());
                            getActivity().finish();
                        }
                    }
                });

                mHandler = new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
//                        super.dispatchMessage(msg);
                        if (msg.what == TYPE_MSG_COUNT) {
                            int countDownTime = (int) msg.obj;
                            // 更新界面
                            String formatStr = String.format(getString(R.string.intro_click_jump), countDownTime);
                            countDownTimerView.setText(formatStr);

                            if (countDownTime < 1) {
                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                getActivity().finish();
                            } else {
                                Message message = Message.obtain();
                                message.what = TYPE_MSG_COUNT;
                                message.obj = countDownTime - 1;
                                mHandler.sendMessageDelayed(message, 1 * 1000);
                            }
                        }
                    }
                };

//                restart();
            }
        }
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    public void restart() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);

            String formatStr = String.format(getString(R.string.intro_click_jump), 5);
            countDownTimerView.setText(formatStr);
            Message message = Message.obtain();
            message.what = TYPE_MSG_COUNT;
            message.obj = 5;
            mHandler.sendMessageDelayed(message, 1 * 1000);
        }
    }

    public void cancel() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}

