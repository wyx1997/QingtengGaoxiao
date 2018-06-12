package com.victory.qingteng.qingtenggaoxiao.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.victory.qingteng.qingtenggaoxiao.R;

import java.util.List;

public class SimpleRVAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SimpleRVAdapter(@Nullable List<String> data) {
        super(R.layout.item_simple_rv_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_simple_rv_tv, item);
    }
}
