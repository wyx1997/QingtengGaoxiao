package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.presenter.BaikeDetailsPresenter;

import java.util.List;

import butterknife.BindView;

public class BaikeDetailsActivity extends BaseActivity {

    private static final String ID = "id";
    private static final String NAME = "name";

    @BindView(R.id.baike_details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.baike_details_rv)
    RecyclerView recyclerView;

    private String id;

    private String name;

    private BaikeDetailsPresenter presenter;

    public static void open(Context context, String id, String name){
        Intent intent = new Intent(context, BaikeDetailsActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(NAME, name);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_baike_details;
    }

    @Override
    protected void handleIntent(Intent intent) {
        id = intent.getStringExtra(ID);
        name = intent.getStringExtra(NAME);
    }

    @Override
    protected void init() {
        initToolbar(toolbar);
        presenter = new BaikeDetailsPresenter(this);
        presenter.getData(id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showData(Object data) {
        if(!isFinishing()){
            List<List<String>> lists = (List<List<String>>) data;
            actionBar.setTitle(name);
            recyclerView.setAdapter(new BaikeRVAdapter(this, lists));
        }
    }

    private class BaikeRVAdapter extends BaseQuickAdapter<List<String>, BaseViewHolder>{

        private Context context;

        public BaikeRVAdapter(Context context, @Nullable List<List<String>> data) {
            super(R.layout.item_simple_rv_layout, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, final List<String> item) {
            helper.setText(R.id.item_simple_rv_tv, item.get(0));
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleActivity.open(context, Constants.BAIKE, item.get(1), item.get(0));
                }
            });
        }
    }
}
