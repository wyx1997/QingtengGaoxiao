package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.SettingRVAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class SettingFragment extends BaseFragment {

    @BindView(R.id.setting_recycler_view)
    RecyclerView recyclerView;

    @BindArray(R.array.main_setting_content)
    String[] settingTitle;

    private List<String> items;

    private SettingRVAdapter adapter;

    public static SettingFragment getInstance(){
        return new SettingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState, ViewGroup container) {
        initRV();
    }

    private void initRV(){
        items = Arrays.asList(settingTitle);
        adapter = new SettingRVAdapter(items, fatherActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(fatherActivity, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(Object data) {

    }
}
