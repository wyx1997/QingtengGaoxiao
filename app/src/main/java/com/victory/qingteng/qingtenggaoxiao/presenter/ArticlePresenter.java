package com.victory.qingteng.qingtenggaoxiao.presenter;

import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.model.ArticleModel;
import com.victory.qingteng.qingtenggaoxiao.utils.RxCache;

public class ArticlePresenter extends BasePresenter {

    private ArticleModel model;

    public ArticlePresenter(Contracts.IView view, int type) {
        super(view);
        model = new ArticleModel(type);
    }

    public void getData(final String id){
        final String ids = "article_"+id;
        if(!isAttachView()){
            return;
        }
        view.showLoading();
        model.getFromLocal(ids, new Contracts.ICallback<String>() {
            @Override
            public void onSuccess(String data) {
                view.hideLoading();
                view.showData(data);
            }

            @Override
            public void onError(String errorMsg) {
                //view.showError(errorMsg);
                model.getFromNetwork(id, new Contracts.ICallback<String>() {
                    @Override
                    public void onSuccess(String data) {
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

    private void put2Cache(String key, String data){
        RxCache.get().putString(key, data).subscribe();
    }
}
