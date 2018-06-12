package com.victory.qingteng.qingtenggaoxiao.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.ArrayFragmentMessage;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.entity.FirstLevel;
import com.victory.qingteng.qingtenggaoxiao.model.entity.SecondLevel;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.listener.OnToTopListener;
import com.victory.qingteng.qingtenggaoxiao.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MainRVAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_FIRST_LEVEL = 0;
    public static final int TYPE_SECOND_LEVEL = 1;

    private Context context;

    @BindString(R.string.total_collect_nums)
    String schoolFormat;

    @BindString(R.string.total_major_nums)
    String majorFormat;

    String[] provinces;

    private int type;

    private int lastPos = 1;

    private OnToTopListener toTopListener;

    private boolean hasTop = false;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MainRVAdapter(List<MultiItemEntity> data, Context context, int type, String[] provinces) {
        super(data);
        ButterKnife.bind(this, (Activity) context);
        addItemType(TYPE_FIRST_LEVEL, R.layout.multi_item_first_level);
        addItemType(TYPE_SECOND_LEVEL, R.layout.multi_item_second_level);
        this.context = context;
        this.type = type;
        this.provinces = provinces;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()){
            case TYPE_FIRST_LEVEL:
                final FirstLevel firstLevel = (FirstLevel) item;
                DBHelper.getInstance()
                        .queryValueSum(type, new String[]{}, type == DBHelper.TYPE_SCHOOL ? "province=?" : "level=?", new String[]{firstLevel.getName()})
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                firstLevel.setTotalSchoolNums(integer);
                                helper.setText(R.id.main_province_school_count, String.format(type == DBHelper.TYPE_SCHOOL ? schoolFormat : majorFormat, integer));
                            }
                        });
                helper.setText(R.id.main_first_level_name, firstLevel.getName())
                        .setImageResource(R.id.main_first_level_img, firstLevel.isExpanded() ? R.drawable.up_arrow:
                                (hasTop && helper.getAdapterPosition() == 1) ? R.drawable.down_arrow_top : R.drawable.down_arrow);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        int collapseCount = 0;
                        if(lastPos < pos){
                            collapseCount = collapse(lastPos);
                        } else if(lastPos > pos){
                            collapse(lastPos);
                        }
                        pos = pos - collapseCount;
                        if(firstLevel.isExpanded()){
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                        lastPos = pos;
                    }
                });
                helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(type == DBHelper.TYPE_MAJOR) return true;
                        collapse(lastPos);
                        toTopListener.showDialog(true, helper.getAdapterPosition());
                        return true;
                    }
                });
                break;
            case TYPE_SECOND_LEVEL:
                final SecondLevel secondLevel = (SecondLevel) item;
                helper.setText(R.id.main_second_level_name, secondLevel.getName());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onClick(View v) {
                        final String queryName = secondLevel.getName().equals("全部") ? provinces[lastPos-1] : secondLevel.getName();
                        DBHelper.getInstance()
                                .queryValueByArgs(type, new String[]{"name"}, type == DBHelper.TYPE_SCHOOL ?
                                                (secondLevel.getName().equals("全部") ?"province=?" : "city=?") : "category=?", new String[]{queryName})
                                .subscribe(new Consumer<List<String>>() {
                                    @Override
                                    public void accept(List<String> list) throws Exception {
                                        if(list.size() == 0){
                                            ToastUtils.showToast(R.string.collect_no_data);
                                            return;
                                        }
                                        EventBus.getDefault().postSticky(new ArrayFragmentMessage(type == DBHelper.TYPE_SCHOOL ? Constants.CITY_SCHOOL : Constants.MAJOR,
                                                String.format(type == DBHelper.TYPE_SCHOOL ? context.getResources().getString(R.string.total_collect_nums)
                                                : context.getResources().getString(R.string.total_major_nums), list.size()), queryName, list));
                                    }
                                });
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+getHeaderLayoutCount();
    }

    public void setToTopListener(OnToTopListener toTopListener) {
        this.toTopListener = toTopListener;
    }

    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }
}
