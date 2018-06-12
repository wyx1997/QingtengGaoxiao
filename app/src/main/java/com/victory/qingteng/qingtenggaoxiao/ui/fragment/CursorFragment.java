package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.StringListMessage;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.presenter.SearchPresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.DetailsActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.SimpleRVAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.functions.Consumer;

public class CursorFragment extends BaseFragment {

    private static final String TAG = "CursorFragment";
    private static final String TYPE = "type";

    private RecyclerView recyclerView;

    private SimpleRVAdapter adapter;

    private int type;

    private SearchPresenter presenter;

    public static CursorFragment getInstance(int type){
        CursorFragment fragment = new CursorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(fatherActivity);
        initView(savedInstanceState, container);
        return recyclerView;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView(Bundle savedInstanceState, ViewGroup container) {
        initRV();
    }

    @Override
    protected void handleArguments(Bundle arguments) {
        type = arguments.getInt(TYPE);
    }

    private void initRV(){
        presenter = new SearchPresenter(this);
        adapter = new SimpleRVAdapter(null);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.queryDetails(fatherActivity, type, (String) adapter.getItem(position));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(fatherActivity, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(Object data) {
        adapter.setNewData((List<String>) data);
    }
}
