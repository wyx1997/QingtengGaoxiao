package com.victory.qingteng.qingtenggaoxiao.model.model;

import android.annotation.SuppressLint;

import com.google.gson.reflect.TypeToken;
import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.entity.BaseResult;
import com.victory.qingteng.qingtenggaoxiao.model.entity.QqgpData;
import com.victory.qingteng.qingtenggaoxiao.utils.RxCache;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QqgpModel extends BaseModel implements Contracts.IModel<List<List<String>>> {

    public static final int SCHOOL = 0;
    public static final int EXAM = 1;

    private int type;

    @SuppressLint("CheckResult")
    @Override
    public void getFromLocal(String key, final Contracts.ICallback<List<List<String>>> callback) {
        RxCache.get().getStringLists(key)
                .subscribe(new BaseCacheObserver<List<List<String>>>(callback));
    }

    @Override
    public void getFromNetwork(String id, final Contracts.ICallback<List<List<String>>> callback) {
        if(type == SCHOOL){
            getApi().getExamQqgp(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseNetworkObserver(callback));
        } else {
            getApi().getExamQqgp(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseNetworkObserver(callback));
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    void handleNetworkOnNext(String string, Contracts.ICallback callback) {
        try {
            BaseResult<List<QqgpData>> baseResult = getBaseResult(string, type==SCHOOL?"school_qqgp":"exam_qqgp", new TypeToken<BaseResult<List<QqgpData>>>(){}.getType());
            if(baseResult.isSuccess()){
                List<List<String>> lists = new ArrayList<>();
                List<QqgpData> data = baseResult.getData();
                for(int i=0; i<data.size(); i++){
                    List<String> list = new ArrayList<>();
                    list.add(data.get(i).getGpName());
                    list.add(String.valueOf(data.get(i).getGpNum()));
                    list.add(data.get(i).getDesc());
                    lists.add(list);
                }
                callback.onSuccess(lists);
            } else {
                showFailure(callback);
            }
        } catch (JSONException e) {
            showFailure(callback);
        }
    }
}
