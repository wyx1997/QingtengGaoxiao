package com.victory.qingteng.qingtenggaoxiao.model.model;

import com.google.gson.reflect.TypeToken;
import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.entity.BaseResult;
import com.victory.qingteng.qingtenggaoxiao.model.entity.UpdateData;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VersionModel extends BaseModel implements Contracts.IModel<List<String>>{

    @Override
    public void getFromLocal(String key, Contracts.ICallback<List<String>> callback) {

    }

    @Override
    public void getFromNetwork(String id, final Contracts.ICallback<List<String>> callback) {
        getApi().getVersion("version")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetworkObserver(callback));
    }

    @Override
    void handleNetworkOnNext(String string, Contracts.ICallback callback) {
        try {
            BaseResult<List<UpdateData>> baseResult = getBaseResult(string, "version", new TypeToken<BaseResult<List<UpdateData>>>(){}.getType());
            if(baseResult.isSuccess()){
                List<String> data = new ArrayList<>();
                data.add(baseResult.getData().get(0).getVersion());
                data.add(baseResult.getData().get(0).getVersion_desc());
                data.add(baseResult.getData().get(0).getDownload());
                callback.onSuccess(data);
            } else {
                showFailure(callback);
            }
        } catch (JSONException e) {
            showFailure(callback);
        }
    }
}
