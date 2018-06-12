package com.victory.qingteng.qingtenggaoxiao.model.model;

import android.annotation.SuppressLint;

import com.google.gson.reflect.TypeToken;
import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.entity.BaseResult;
import com.victory.qingteng.qingtenggaoxiao.utils.RxCache;

import org.json.JSONException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ArticleModel extends BaseModel implements Contracts.IModel<String> {

    private int type;

    public ArticleModel(int type) {
        this.type = type;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getFromLocal(String key, final Contracts.ICallback<String> callback) {
        RxCache.get().getString(key)
                .subscribe(new BaseCacheObserver<String>(callback));
    }

    @SuppressLint("CheckResult")
    @Override
    public void getFromNetwork(String id, final Contracts.ICallback<String> callback) {
        if(type == Constants.TALK_ARTICLE){
            getApi().getArticle(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseNetworkObserver(callback));
        } else if (type == Constants.BAIKE){
            getApi().getWikiContent(id)
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
            String name = null;
            switch (type){
                case Constants.TALK_ARTICLE: name = "article"; break;
                case Constants.BAIKE: name = "exam_wiki_content"; break;
            }
            BaseResult<String> baseResult = getBaseResult(string, name, new TypeToken<BaseResult<String>>(){}.getType());
            if(baseResult.isSuccess()){
                callback.onSuccess(baseResult.getData());
            } else {
                showFailure(callback);
            }
        } catch (JSONException e) {
            showFailure(callback);
        }
    }
}
