package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.presenter.ExamYuanPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;

public class ExamYuanDetailsActivity extends BaseActivity {

    private static final String INTENT_POS = "intent_pos";

    @BindView(R.id.exam_details_rv)
    RecyclerView recyclerView;

    @BindView(R.id.exam_details_toolbar)
    Toolbar toolbar;

    @BindString(R.string.exam_yuan_web)
    String detailsWeb;

    @BindString(R.string.exam_yuan_weibo)
    String weibo;

    private ExamYuanPresenter presenter;

    private int pos;

    public static void open(Context context, int pos){
        Intent intent = new Intent(context, ExamYuanDetailsActivity.class);
        intent.putExtra(INTENT_POS, pos);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exam_yuan_details;
    }

    @Override
    protected void init() {
        presenter = new ExamYuanPresenter(this);
        initToolbar(toolbar);
        presenter.getData(this, pos);
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
    protected void handleIntent(Intent intent) {
        pos = intent.getIntExtra(INTENT_POS, 0);
    }

    @Override
    public void showData(Object data) {
        List<String> list = (List<String>) data;
        actionBar.setTitle(list.get(0));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RvAdapter(list.subList(1, list.size())));
    }

    private class RvAdapter extends BaseQuickAdapter<String, BaseViewHolder>{

        RvAdapter(@Nullable List<String> data) {
            super(R.layout.item_details, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final String item) {
            switch (helper.getAdapterPosition()){
                case 0:
                    helper.setImageResource(R.id.details_image, R.drawable.guanwang)
                            .setText(R.id.details_text, detailsWeb);
                    break;
                case 1:
                    helper.setImageResource(R.id.details_image, R.drawable.weibo)
                            .setText(R.id.details_text, weibo);
                    break;
            }
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.open(ExamYuanDetailsActivity.this, item);
                }
            });
        }
    }
}
