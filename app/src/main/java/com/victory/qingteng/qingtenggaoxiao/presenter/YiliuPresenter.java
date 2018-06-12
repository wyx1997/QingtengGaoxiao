package com.victory.qingteng.qingtenggaoxiao.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.WebViewActivity;

import java.util.List;

import io.reactivex.functions.Consumer;

public class YiliuPresenter extends BasePresenter {

    public YiliuPresenter(Contracts.IView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    public void getData(String title){
        DBHelper.getInstance()
                .queryValueByArgs(DBHelper.TYPE_YILIU, new String[]{"major"}, "schoolname=?", new String[]{title})
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        view.showData(list);
                    }
                });
    }

    public void break2Web(String item){
        String url = "https://baike.baidu.com/item/"+item+"专业";
        WebViewActivity.open((Context) view, url);
    }
}
