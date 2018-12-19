package com.click.cn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.click.cn.MainActivity;
import com.click.cn.main.ListFragment;
import com.click.cn.main.MainFragment;
import com.click.cn.main.ItemListDialogFragment;


public class MainPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private MainFragment mainFragment = null;
    private ListFragment listFragment = null;
    private ItemListDialogFragment mMoreFragment = null;


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        mainFragment = new MainFragment();
        listFragment = new ListFragment();
        mMoreFragment =  ItemListDialogFragment.newInstance(9);
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = mainFragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = listFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = mMoreFragment;
                break;
        }
        return fragment;
    }


}

