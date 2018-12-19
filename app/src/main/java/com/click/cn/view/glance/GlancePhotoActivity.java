package com.click.cn.view.glance;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.click.cn.R;
import com.click.cn.base.BaseActivity;
import com.click.cn.widget.MultiTouchViewPager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import me.relex.circleindicator.CircleIndicator;
import me.relex.photodraweeview.PhotoDraweeView;

public class GlancePhotoActivity extends BaseActivity {
    private MultiTouchViewPager viewPager;
    private  CircleIndicator indicator;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, GlancePhotoActivity.class));
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_glance;
    }

    @Override
    public void findViews() {
         indicator = findViewById(R.id.indicator);
        viewPager = findViewById(R.id.view_pager);
    }

    @Override
    public void initHeader() {
      mToolBar.setVisibility(View.GONE);
    }

    @Override
    public void initContent() {
        viewPager.setAdapter(new DraweePagerAdapter());
        indicator.setViewPager(viewPager);
    }

    @Override
    public void setListener() {
//        mToolBar.setEventListener(new HeaderLayout.ClickEvent() {
//            @Override
//            public void backClick(View v) {
//                finish();
//            }
//
//            @Override
//            public void titleClick(View v) {
//
//            }
//
//            @Override
//            public void moreClick(View v) {
//
//            }
//        });
    }

    public class DraweePagerAdapter extends PagerAdapter {

        private int[] mDrawables = new int[] {
                R.drawable.viewpager_1, R.drawable.viewpager_2, R.drawable.viewpager_3,
                R.drawable.viewpager_1, R.drawable.viewpager_2, R.drawable.viewpager_3
        };

        @Override public int getCount() {
            return mDrawables.length;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public Object instantiateItem(ViewGroup viewGroup, int position) {
            final PhotoDraweeView photoDraweeView = new PhotoDraweeView(viewGroup.getContext());
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(Uri.parse("res:///" + mDrawables[position]));
            controller.setOldController(photoDraweeView.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null) {
                        return;
                    }
                    photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            photoDraweeView.setController(controller.build());

            try {
                viewGroup.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return photoDraweeView;
        }
    }
}
