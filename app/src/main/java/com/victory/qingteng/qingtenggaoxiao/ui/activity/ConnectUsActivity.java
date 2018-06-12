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
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.ui.fragment.UpdateDialogFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class ConnectUsActivity extends BaseActivity {

    @BindView(R.id.connect_us_rv)
    RecyclerView recyclerView;

    @BindArray(R.array.connect_us)
    String[] titles;

    @BindView(R.id.connect_us_toolbar)
    Toolbar toolbar;

    private UpdateDialogFragment updateDialogFragment;

    private ConnectRVAdapter adapter;

    public static void open(Context context){
        Intent intent = new Intent(context, ConnectUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_us;
    }

    @Override
    protected void init() {
        initToolbar(toolbar);
        adapter = new ConnectRVAdapter(this, Arrays.asList(titles));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        actionBar.setTitle(R.string.connect_us);
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

    }

    private class ConnectRVAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        private Context context;

        public ConnectRVAdapter(Context context, @Nullable List<String> data) {
            super(R.layout.item_connect_us, data);
            this.context = context;
        }

        @Override
        protected void convert(final BaseViewHolder helper, String item) {
            helper.setText(R.id.connect_us_rv_text, item);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (helper.getAdapterPosition()){
                        case 0:{
                            updateDialogFragment = UpdateDialogFragment.getInstance();
                            updateDialogFragment.show(getSupportFragmentManager(), null);
                        }
                        break;
                        case 1: AboutUsActivity.open(context);
                    }
                }
            });
        }
    }
}
