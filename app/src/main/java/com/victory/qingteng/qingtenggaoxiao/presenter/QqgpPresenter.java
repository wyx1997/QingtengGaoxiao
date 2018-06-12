package com.victory.qingteng.qingtenggaoxiao.presenter;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.model.QqgpModel;
import com.victory.qingteng.qingtenggaoxiao.utils.RxCache;

import java.util.List;

public class QqgpPresenter extends BasePresenter {

    private QqgpModel model;

    public QqgpPresenter(Contracts.IView view) {
        super(view);
        model = new QqgpModel();
    }

    public void getData(final String id, int type){
        model.setType(type);
        final String key = type+"_"+id;
        if(!isAttachView()){
            return;
        }
        view.showLoading();
        model.getFromLocal(key, new Contracts.ICallback<List<List<String>>>() {
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
                        put2Cache(key, data);
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
