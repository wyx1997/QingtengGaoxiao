package com.victory.qingteng.qingtenggaoxiao.presenter;

import android.annotation.SuppressLint;
import android.content.ContentValues;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;

import java.util.Iterator;
import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class CollectPresenter extends BasePresenter{

    private List<String> data;

    public CollectPresenter(Contracts.IView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    public void query4Collect(int type){
        DBHelper.getInstance()
                .queryValueByArgs(type, new String[]{"name"}, "collect=1", null)
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        data = list;
                        view.hideLoading();
                        view.showData(list);
                    }
                });
    }

    public void updateCollect(List<String> toDeleteItems, int type){
        if(data == null) return;
        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            if (toDeleteItems.contains(iterator.next())) {
                iterator.remove();
            }
        }
        ContentValues values = new ContentValues(1);
        values.put("collect", "0");
        DBHelper.getInstance()
                .updateValueByArgs(type, values, "name=?", toDeleteItems)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.showData(data);
                    }
                })
                .subscribe();
    }
}
