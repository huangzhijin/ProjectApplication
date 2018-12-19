package com.click.cn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.click.cn.adapter.MainPagerAdapter;
import com.click.cn.base.BaseActivity;
import com.click.cn.main.ItemListDialogFragment;
import com.click.cn.main.ListFragment;
import com.click.cn.main.MainFragment;
import com.click.cn.main.dummy.DummyContent;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener,ListFragment.OnListFragmentInteractionListener,ItemListDialogFragment.Listener {

    private BottomNavigationView navigation;
    private  ViewPager  vpager;
    private MainPagerAdapter mAdapter;
    private MainFragment mainFragment;
    private MenuItem   menuItem;


    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    vpager.setCurrentItem(PAGE_ONE);
                    return true;
                case R.id.navigation_dashboard:
                    vpager.setCurrentItem(PAGE_TWO);
                    return true;
                case R.id.navigation_notifications:
                    vpager.setCurrentItem(PAGE_THREE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_main;
    }

    @Override
    public void findViews() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        vpager = (ViewPager) findViewById(R.id.vpager);


    }

    @Override
    public void initHeader() {
        mToolBar.setTitle("首页");
    }

    @Override
    public void initContent() {
        vpager.setAdapter(mAdapter);
        mainFragment = new MainFragment();
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void setListener() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
           @Override
           public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
               super.onFragmentAttached(fm, f, context);
               Log.i(logTag, "onFragmentAttached():" + f.getClass().getSimpleName());
           }


           @Override
           public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
               super.onFragmentCreated(fm, f, savedInstanceState);
               Log.i(logTag, "onFragmentCreated():" + f.getClass().getSimpleName());

           }

           @Override
           public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
               super.onFragmentViewCreated(fm, f, v, savedInstanceState);
               Log.i(logTag, "onFragmentViewCreated():" + f.getClass().getSimpleName());
           }

           @Override
           public void onFragmentResumed(FragmentManager fm, Fragment f) {
               super.onFragmentResumed(fm, f);
               Log.i(logTag, "onFragmentResumed():" + f.getClass().getSimpleName());
           }

           @Override
           public void onFragmentPaused(FragmentManager fm, Fragment f) {
               super.onFragmentPaused(fm, f);
               Log.i(logTag, "onFragmentPaused():" + f.getClass().getSimpleName());
           }

           @Override
           public void onFragmentStopped(FragmentManager fm, Fragment f) {
               super.onFragmentStopped(fm, f);
               Log.i(logTag, "onFragmentStopped():" + f.getClass().getSimpleName());
           }

           @Override
           public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
               super.onFragmentSaveInstanceState(fm, f, outState);
               Log.i(logTag, "onFragmentSaveInstanceState():" + f.getClass().getSimpleName());
           }

           @Override
           public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
               super.onFragmentViewDestroyed(fm, f);
               Log.i(logTag, "onFragmentViewDestroyed():" + f.getClass().getSimpleName());
           }
       }, true);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (menuItem != null) {
            menuItem.setChecked(false);
        } else {
            navigation.getMenu().getItem(0).setChecked(false);
        }
        menuItem = navigation.getMenu().getItem(i);
        menuItem.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
//        if (state == 2) {
//            switch (vpager.getCurrentItem()) {
//                case PAGE_ONE:
//                    rb_terminal.setChecked(true);
//                    break;
//                case PAGE_TWO:
//                    rb_station.setChecked(true);
//                    break;
//                case PAGE_THREE:
//                    rb_more.setChecked(true);
//                    break;
//            }
//        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Toast.makeText(this,""+item.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(this,""+position,Toast.LENGTH_SHORT).show();

    }
    //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });


     /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitClick() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            //System.exit(0);
        }
    }*/


}
