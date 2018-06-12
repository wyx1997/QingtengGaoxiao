package com.victory.qingteng.qingtenggaoxiao.presenter;

import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.model.ArticleModel;
import com.victory.qingteng.qingtenggaoxiao.model.model.WikiListModel;
import com.victory.qingteng.qingtenggaoxiao.utils.RxCache;

import java.util.List;

public class BaikeDetailsPresenter extends BasePresenter {

    private WikiListModel model;

    public BaikeDetailsPresenter(Contracts.IView view) {
        super(view);
        model = new WikiListModel();
    }

    public void getData(final String id){
        final String ids = "wikiList_"+id;
        if(!isAttachView()){
            return;
        }
        view.showLoading();
        model.getFromLocal(ids, new Contracts.ICallback<List<List<String>>>() {
            @Override
            public void onSuccess(List<List<String>> data) {
                view.hideLoading();
                view.showData(data);
            }

            @Override
            public void onError(String errorMsg) {
                //view.showError(errorMsg);
                model.getFromNetwork(id, new Contracts.ICallback<List<List<String>>>() {
                    @Override
                    public void onSuccess(List<List<String>> data) {
                        view.hideLoading();
                        view.showData(data);
                        put2Cache(ids, data);
                    }

                    @Override
                    public void onError(String errorMsg) {

                    }

                    @Override
                    public void onFailure(String failMsg) {
                        view.hideLoading();
                        view.showError(failMsg);
                    }
                });
            }

            @Override
            public void onFailure(String failMsg) {

            }
        });
    }

    private void put2Cache(String key, List<List<String>> data){
        RxCache.get().putStringLists(key, data).subscribe();
    }
}
