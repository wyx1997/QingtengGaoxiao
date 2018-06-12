package com.victory.qingteng.qingtenggaoxiao.presenter;

import android.content.ContentValues;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;

import java.util.List;

public class DetailsPresenter extends BasePresenter {

    private List<String> data;

    private boolean isReady = false;

    public DetailsPresenter(Contracts.IView view) {
        super(view);
    }

    public void getData(){
        if(isReady && null != data){
            view.showLoading();
            view.showData(data);
            view.hideLoading();
        }
    }

    public void updateCollect(int type, boolean isCollect){
        ContentValues values = new ContentValues(1);
        values.put("collect", isCollect ? "1" : "0");
        DBHelper.getInstance()
                .updateValueByArgs(type, values, "name=?", new String[]{data.get(0)})
                .subscribe();
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
