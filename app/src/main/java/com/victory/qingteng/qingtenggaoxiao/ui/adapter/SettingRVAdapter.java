package com.victory.qingteng.qingtenggaoxiao.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.ArrayFragmentMessage;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.CollectActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.ConnectUsActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class SettingRVAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int[] imgId = new int[]{
            R.drawable.setting_collect, R.drawable.exam_enter, R.drawable.qun,
            R.drawable.gaoxiao_baike, R.drawable.setting_about
    };

    private Context context;

    private Sheet examYuanSheet;

    public SettingRVAdapter(@Nullable List<String> data, Context context) {
        super(R.layout.item_setting_rv, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        final int pos = helper.getAdapterPosition() - getHeaderLayoutCount();
        helper.setText(R.id.setting_item_text, item)
                .setImageResource(R.id.setting_item_image, imgId[pos])
                .setImageResource(R.id.setting_item_arrow, R.drawable.right_arrow);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pos){
                    case 0: CollectActivity.open(context); break;
                    case 1: postExamYuanData(); break;
                    case 2: {
                        EventBus.getDefault().postSticky(new ArrayFragmentMessage(Constants.BAOKAO_GP, "", context.getResources().getString(R.string.province_qqgp)
                                , Arrays.asList(context.getResources().getStringArray(R.array.province))));
                        break;
                    }
                    case 3: {
                        EventBus.getDefault().postSticky(new ArrayFragmentMessage(Constants.BAIKE, "", item,
                                Arrays.asList(context.getResources().getStringArray(R.array.gaoxiao_baike))));
                        break;
                    }
                    case 4: ConnectUsActivity.open(context);break;
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void postExamYuanData() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                if(null == examYuanSheet){
                    InputStream inputStream = context.getAssets().open("kaoshiyuan.xls");
                    Workbook workbook = Workbook.getWorkbook(inputStream);
                    examYuanSheet = workbook.getSheet(0);
                }
                List<String> data = new ArrayList<>();
                for(Cell cell : examYuanSheet.getColumn(0)){
                    data.add(cell.getContents());
                }
                emitter.onNext(data);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        EventBus.getDefault().postSticky(new ArrayFragmentMessage(Constants.TIANBAO_ENTRY, "", context.getResources().getString(R.string.exam_yuan), list));
                    }
                });
    }
}
