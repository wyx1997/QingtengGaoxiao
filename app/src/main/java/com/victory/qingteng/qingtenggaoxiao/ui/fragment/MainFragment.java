package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.MainPagerAdapter;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class MainFragment extends BaseFragment {

    @BindView(R.id.main_container_view_pager)
    ViewPager viewPager;

    @BindView(R.id.main_fragment_tab)
    TabLayout tabLayout;

    @BindArray(R.array.main_tab_title)
    String[] tabTitles;

    public static MainFragment getInstance(){
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState, ViewGroup container) {
        initViewPager();
    }

    private void initViewPager(){
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[1]));
        viewPager.setAdapter(new MainPagerAdapter(getChildFragmentManager(), tabTitles));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showData(Object data) {

    }
}
