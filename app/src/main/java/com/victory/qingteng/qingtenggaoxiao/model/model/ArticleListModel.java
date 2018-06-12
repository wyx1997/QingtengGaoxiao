package com.victory.qingteng.qingtenggaoxiao.model.model;

import android.annotation.SuppressLint;

import com.google.gson.reflect.TypeToken;
import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.entity.ArticleDetails;
import com.victory.qingteng.qingtenggaoxiao.model.entity.BaseResult;
import com.victory.qingteng.qingtenggaoxiao.utils.RxCache;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ArticleListModel extends BaseModel implements Contracts.IModel<List<List<String>>> {

    @SuppressLint("CheckResult")
    @Override
    public void getFromLocal(String key, final Contracts.ICallback<List<List<String>>> callback) {
        RxCache.get().getStringLists(key)
                .subscribe(new BaseCacheObserver<List<List<String>>>(callback));
    }

    @SuppressLint("CheckResult")
    @Override
    public void getFromNetwork(String id, final Contracts.ICallback<List<List<String>>> callback) {
        getApi().getArticleList("article_list")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetworkObserver(callback));
    }

    @Override
    void handleNetworkOnNext(String string, Contracts.ICallback callback) {
        try {
            BaseResult<List<ArticleDetails>> baseResult = getBaseResult(string, "article_list",
                    new TypeToken<BaseResult<List<ArticleDetails>>>(){}.getType());
            if(baseResult.isSuccess()){
                List<List<String>> lists = new ArrayList<>();
                for (int i=0; i<baseResult.getData().size(); i++){
                    ArticleDetails articleList = baseResult.getData().get(i);
                    List<String> list = new ArrayList<>();
                    list.add(String.valueOf(articleList.getGrade()));
                    list.add("学校:"+articleList.getSchool()+" 专业:"
                            +articleList.getMajor());
                    list.add(articleList.getTitle());
                    list.add(String.valueOf(articleList.getArticleNum()));
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
