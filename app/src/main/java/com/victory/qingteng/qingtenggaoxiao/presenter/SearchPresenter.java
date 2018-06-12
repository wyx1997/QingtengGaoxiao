package com.victory.qingteng.qingtenggaoxiao.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.StringListMessage;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.DetailsActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.functions.Consumer;

public class SearchPresenter extends BasePresenter {

    public SearchPresenter(Contracts.IView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    public void searchName(int type, String text){
        DBHelper.getInstance()
                .queryValueByArgs(type, new String[]{"name"}, "name like ?",
                        new String[]{text+"%"})
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        view.showData(list);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void queryDetails(final Context context, final int type, String item){
        DBHelper.getInstance()
                .queryValueByArgs(type,null, "name=?", new String[]{item})
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        if(list.size() != 0){
                            EventBus.getDefault().postSticky(new StringListMessage(list));
                            DetailsActivity.open(context, type);
                        }
                    }
                });
    }
}
