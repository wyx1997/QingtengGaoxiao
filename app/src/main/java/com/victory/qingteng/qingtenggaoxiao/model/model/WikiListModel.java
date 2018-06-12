package com.victory.qingteng.qingtenggaoxiao.model.model;

import android.annotation.SuppressLint;

import com.google.gson.reflect.TypeToken;
import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.entity.BaseResult;
import com.victory.qingteng.qingtenggaoxiao.model.entity.ExamWikiData;
import com.victory.qingteng.qingtenggaoxiao.utils.RxCache;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WikiListModel extends BaseModel implements Contracts.IModel<List<List<String>>> {

    @SuppressLint("CheckResult")
    @Override
    public void getFromLocal(String key, final Contracts.ICallback<List<List<String>>> callback) {
        RxCache.get().getStringLists(key)
                .subscribe(new BaseCacheObserver<List<List<String>>>(callback));
    }

    @Override
    public void getFromNetwork(String id, final Contracts.ICallback<List<List<String>>> callback) {
        getApi().getWikiList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetworkObserver(callback));
    }

    @Override
    void handleNetworkOnNext(String string, Contracts.ICallback callback) {
        try {
            BaseResult<List<ExamWikiData>> baseResult = getBaseResult(string, "exam_wiki_list", new TypeToken<BaseResult<List<ExamWikiData>>>(){}.getType());
            if(baseResult.isSuccess()){
                List<List<String>> lists = new ArrayList<>();
                for(int i=0; i<baseResult.getData().size(); i++){
                    ExamWikiData wikiData = baseResult.getData().get(i);
                    List<String> list = new ArrayList<>();
                    list.add(wikiData.getArticleName());
                    list.add(wikiData.getArticleNum());
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
