package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.content.ClipboardManager;
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
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.presenter.QqgpPresenter;
import com.victory.qingteng.qingtenggaoxiao.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;

public class QqgpActivity extends BaseActivity {

    private static final String TYPE = "type";
    private static final String ID = "id";

    @BindView(R.id.qqgp_toolbar)
    Toolbar toolbar;

    @BindView(R.id.qqgp_rv)
    RecyclerView recyclerView;

    private int type;

    private String id;

    private QqgpAdapter adapter;

    private QqgpPresenter presenter;

    public static void open(Context context, int type, String id){
        Intent intent = new Intent(context, QqgpActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(ID, id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qqgp;
    }

    @Override
    protected void init() {
        presenter = new QqgpPresenter(this);
        initToolbar(toolbar);
        initRV();
        presenter.getData(id, type);
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
        type = intent.getIntExtra(TYPE, 0);
        id = intent.getStringExtra(ID);
    }

    private void initRV(){
        adapter = new QqgpAdapter(null);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(Object data) {
        if(null != adapter){
            adapter.setNewData((List<List<String>>) data);
        }
    }

    private class QqgpAdapter extends BaseQuickAdapter<List<String>, BaseViewHolder>{

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        QqgpAdapter(@Nullable List<List<String>> data) {
            super(R.layout.item_qqgp, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final List<String> item) {
            helper.setText(R.id.item_qqgp_name, item.get(0))
                    .setText(R.id.item_qqgp_number, "群号: "+item.get(1))
                    .setText(R.id.item_qqgp_desc, "简介: "+item.get(2));
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clipboardManager.setText(item.get(1));
                    ToastUtils.showToast(R.string.qqgp_number_to_clipboard);
                }
            });
        }
    }
}
