package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.victory.qingteng.qingtenggaoxiao.MyApplication;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.presenter.YiliuPresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.SimpleRVAdapter;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;

import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class YiliuDetailsActivity extends BaseActivity {

    private static final String TAG = "YiliuDetailsActivity";

    private static final String INTENT_TITLE = "intent_title";
    private static final String INTENT_ID = "intent_id";

    @BindView(R.id.rv_yiliu_major)
    RecyclerView rvMajor;

    @BindView(R.id.yiliu_details_toolbar)
    Toolbar toolbar;

    private SimpleRVAdapter adapter;

    private String title;

    private int yiliuId;

    private YiliuPresenter yiliuPresenter;

    public static void open(Context context, String title, String yiliuId){
        Intent intent = new Intent(context, YiliuDetailsActivity.class);
        intent.putExtra(INTENT_TITLE, title);
        intent.putExtra(INTENT_ID, yiliuId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yiliu_details;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void init() {
        yiliuPresenter = new YiliuPresenter(this);
        initToolbar(toolbar);
        initRV();
        yiliuPresenter.getData(title);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        title = intent.getStringExtra(INTENT_TITLE);
        yiliuId = Integer.parseInt(intent.getStringExtra(INTENT_ID));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.get().getRefWatcher(this).watch(this);
    }

    private void initRV(){
        adapter = new SimpleRVAdapter(null);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                yiliuPresenter.break2Web((String) adapter.getItem(position));
            }
        });
        rvMajor.setLayoutManager(new LinearLayoutManager(this));
        rvMajor.setAdapter(adapter);
    }

    @Override
    public void showData(Object data) {
        List<String> list = (List<String>) data;
        String text;
        switch (yiliuId){
            case 1: text = String.format(getString(R.string.yiliu_a_daxue), list.size()); break;
            case 2: text = String.format(getString(R.string.yiliu_b_daxue), list.size()); break;
            case 3: text = String.format(getString(R.string.yiliu_xueke), list.size()); break;
            default: text = "";
        }
        actionBar.setSubtitle(text);
        adapter.setNewData(list);
    }
}
