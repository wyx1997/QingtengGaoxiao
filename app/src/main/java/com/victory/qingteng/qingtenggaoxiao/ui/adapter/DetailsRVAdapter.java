package com.victory.qingteng.qingtenggaoxiao.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.model.model.QqgpModel;
import com.victory.qingteng.qingtenggaoxiao.presenter.QqgpPresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.QqgpActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.WebViewActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.YiliuDetailsActivity;

import java.util.List;

public class DetailsRVAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public static final String TAG = "DetailsRVAdapter";

    private static String[] schoolItems = new String[]{"双一流建设", "院校官网", "本科招生",
            "百度百科", "百度贴吧", "官方微博", "新生交流群"};

    private static String[] majorItems = new String[]{"百度百科", "百度贴吧"};

    private static int[] schoolImgs = new int[]{
            R.drawable.honor, R.drawable.guanwang, R.drawable.zhaosheng, R.drawable.baike,
            R.drawable.tieba, R.drawable.weibo, R.drawable.qun
    };

    private static int[] majorImgs = new int[]{
            R.drawable.baike, R.drawable.tieba
    };

    private Context context;

    private List<String> originData;

    private int[] originPos;

    private int noDataLen = 0;

    private int type;

    private String detailsTitle;

    public DetailsRVAdapter(Context context, @Nullable List<String> data, String title, int type) {
        super(R.layout.item_details, data);
        this.context = context;
        originData = data;
        originPos = new int[type == DBHelper.TYPE_SCHOOL ? schoolItems.length : majorItems.length];
        for(int i=0; i<originPos.length; ++i){
            originPos[i] = i;
        }
        detailsTitle = title;
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {//每一个item对应一个helper
        final int position = originPos[helper.getAdapterPosition()];
        helper.setText(R.id.details_text, type == DBHelper.TYPE_SCHOOL ? schoolItems[position] : majorItems[position])
                .setImageResource(R.id.details_image, type == DBHelper.TYPE_SCHOOL ? schoolImgs[position] : majorImgs[position]);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0 && item.length() == 1){
                    YiliuDetailsActivity.open(context, detailsTitle, item);
                } else if (item.length() == 5) {
                    QqgpActivity.open(context, QqgpModel.SCHOOL, item);
                } else {
                    WebViewActivity.open(context, item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = super.getItemCount();
        for(int i=0; i<originData.size(); ++i){
            if(originData.get(i) == null || originData.get(i).equals("")){
                size -= 1;
                ++noDataLen;
                originData.remove(i);
            }
            originPos[i] += noDataLen;
        }
        noDataLen = 0;
        return size;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return originData.get(position);
    }
}
