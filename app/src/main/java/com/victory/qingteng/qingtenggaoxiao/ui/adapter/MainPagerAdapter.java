package com.victory.qingteng.qingtenggaoxiao.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.victory.qingteng.qingtenggaoxiao.ui.fragment.TabFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;

    private TabFragment proFragment;

    private TabFragment majorFragment;

    public MainPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                if(null == proFragment){
                    proFragment = TabFragment.getInstance(TabFragment.FRAGMENT_PROVINCE);
                }
                return proFragment;
            }
            default:{
                if(null == majorFragment){
                    majorFragment = TabFragment.getInstance(TabFragment.FRAGMENT_LEVEL);
                }
                return majorFragment;
            }
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
