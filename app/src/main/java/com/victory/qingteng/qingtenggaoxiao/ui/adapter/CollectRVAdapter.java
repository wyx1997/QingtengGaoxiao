package com.victory.qingteng.qingtenggaoxiao.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.StringListMessage;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.DetailsActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class CollectRVAdapter extends BaseQuickAdapter<String, BaseViewHolder>
        implements BaseQuickAdapter.OnItemChildClickListener{

    private static final String TAG = "CollectRVAdapter";

    private Context context;

    private int type = DBHelper.TYPE_SCHOOL;

    private List<String> toDeleteItems;

    public CollectRVAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_collect, data);
        this.context = context;
        toDeleteItems = new ArrayList<>();
        setOnItemChildClickListener(this);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setText(R.id.collect_text, item)
                .addOnClickListener(R.id.collect_text)
                .setOnCheckedChangeListener(R.id.collect_check_box, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked && !toDeleteItems.contains(item)){
                            toDeleteItems.add(item);
                        } else if(!isChecked && toDeleteItems.contains(item)){
                            toDeleteItems.remove(item);
                        }
                    }
                });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.collect_text:
                String name = (String) adapter.getItem(position);
                DBHelper.getInstance()
                        .queryValueByArgs(type, null, "name=?", new String[]{name})
                        .subscribe(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> list) throws Exception {
                                EventBus.getDefault().postSticky(new StringListMessage(list));
                                DetailsActivity.open(context, type);
                            }
                        });
                break;
        }
    }

    public List<String> getToDeleteItems() {
        return toDeleteItems;
    }

    public void setType(int type) {
        this.type = type;
    }
}
