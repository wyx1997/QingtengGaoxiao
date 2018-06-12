package com.victory.qingteng.qingtenggaoxiao.presenter;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.model.ArticleListModel;
import com.victory.qingteng.qingtenggaoxiao.utils.RxCache;

import java.util.ArrayList;
import java.util.List;

public class TalkPresenter extends BasePresenter {

    private ArticleListModel model;

    public TalkPresenter(Contracts.IView view) {
        super(view);
        model = new ArticleListModel();
    }

    public void getData(final String name){
        if(!isAttachView()){
            return;
        }
        view.showLoading();
        model.getFromLocal(name, new Contracts.ICallback<List<List<String>>>() {
            @Override
            public void onSuccess(List<List<String>> data) {
                view.hideLoading();
                view.showData(data);
            }

            @Override
            public void onError(String errorMsg) {
                //view.showError(errorMsg);
                model.getFromNetwork("", new Contracts.ICallback<List<List<String>>>() {
                    @Override
                    public void onSuccess(List<List<String>> data) {
                        view.hideLoading();
                        view.showData(data);
                        put2Cache(name, data);
                    }

                    @Override
                    public void onError(String errorMsg) {

                    }

                    @Override
                    public void onFailure(String failMsg) {
                        view.hideLoading();
                        view.showFailure(failMsg);
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
