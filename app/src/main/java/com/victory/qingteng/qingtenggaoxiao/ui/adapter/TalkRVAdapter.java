package com.victory.qingteng.qingtenggaoxiao.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.ArticleActivity;

import java.util.List;

public class TalkRVAdapter extends BaseQuickAdapter<List<String>, BaseViewHolder>{

    private Context context;

    public TalkRVAdapter(@Nullable List<List<String>> data, Context context) {
        super(R.layout.item_talk_rv, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final List<String> item) {
        int id = 0;
        switch (item.get(0)){
            case "1.0": id = R.drawable.vine; break;
            case "14.0": id = R.drawable.fourteen; break;
            case "15.0": id = R.drawable.fifteen; break;
            case "16.0": id = R.drawable.sixteen; break;
            case "17.0": id = R.drawable.seventeen; break;
            case "18.0": id = R.drawable.eighteen;
        }
        helper.setImageResource(R.id.talk_item_civ, id)
                .setText(R.id.talk_item_people_details, item.get(1))
                .setText(R.id.talk_item_title, item.get(2));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleActivity.open(context, Constants.TALK_ARTICLE, item.get(3), item.get(2));
            }
        });
    }
}
