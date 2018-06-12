package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.presenter.TalkPresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.TalkRVAdapter;
import com.victory.qingteng.qingtenggaoxiao.ui.widget.LoadingProgressBar;

import java.util.List;

import butterknife.BindView;

public class TalkFragment extends BaseFragment {

    private static final String KEY = "talk";
    private static final String TAG = "TalkFragment";
    private static final int PERMISSION_REQUEST_STORAGE = 1;

    @BindView(R.id.talk_rv)
    RecyclerView recyclerView;

    @BindView(R.id.talk_loading)
    LoadingProgressBar loadingProgressBar;

    private TalkRVAdapter adapter;

    private TalkPresenter talkPresenter;

    public static TalkFragment getInstance(){
        return new TalkFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_talk;
    }

    @Override
    protected void initView(Bundle savedInstanceState, ViewGroup container) {
        talkPresenter = new TalkPresenter(this);
        adapter = new TalkRVAdapter(null, fatherActivity);
        talkPresenter.getData(KEY);
        recyclerView.addItemDecoration(new DividerItemDecoration(fatherActivity, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showData(Object data) {
        adapter.setNewData((List<List<String>>) data);
    }
}
