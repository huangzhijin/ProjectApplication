package com.click.cn;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.click.cn.util.MkVUtils;
import com.click.cn.util.TransluteStatuBarUtil;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class WelComeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<WelComeFragment> fragments;
    private CircleIndicator circleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MyApplication.getMainAppContext(), R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransluteStatuBarUtil.immersive(this, false);
        }
        setContentView(R.layout.layout_activity_welcome);
        mViewPager = findViewById(R.id.view_pager);
        circleIndicator = findViewById(R.id.indicator);
        fragments = initFragment();
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        circleIndicator.setViewPager(mViewPager);
        MkVUtils.getInstance(MyApplication.getMainAppContext()).getUiProcessMMKV().encode(MkVUtils.KEY_VERSION_CODE, BuildConfig.VERSION_CODE);

        addListener();
    }

    private void addListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                WelComeFragment fragment = fragments.get(fragments.size() - 1);
                if (position == fragments.size() - 1) {
                    fragment.restart();
                } else {
                    fragment.cancel();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<WelComeFragment> initFragment() {

        WelComeFragment introFragment01 = WelComeFragment.getInstance(R.drawable.intro_center_01, R.drawable.intro_bottom_01, false);
        WelComeFragment introFragment02 = WelComeFragment.getInstance(R.drawable.intro_center_02, R.drawable.intro_bottom_02, false);
        WelComeFragment introFragment03 = WelComeFragment.getInstance(R.drawable.intro_center_03, R.drawable.intro_bottom_03, true);

        List<WelComeFragment> list = new ArrayList<>();
        list.add(introFragment01);
        list.add(introFragment02);
        list.add(introFragment03);
        return list;
    }

    private static final class MainViewPagerAdapter extends FragmentPagerAdapter {
        private List<WelComeFragment> fragments;

        public MainViewPagerAdapter(FragmentManager fm, List<WelComeFragment> fragmentList) {
            super(fm);
            fragments = fragmentList;
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments == null ? null : fragments.get(position);
        }

        public List<WelComeFragment> getFragments() {
            return fragments;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransluteStatuBarUtil.immersive(this, false);
        }
    }
}
