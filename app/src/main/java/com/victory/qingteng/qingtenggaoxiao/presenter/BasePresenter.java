package com.victory.qingteng.qingtenggaoxiao.presenter;

import com.victory.qingteng.qingtenggaoxiao.Contracts;

public abstract class BasePresenter implements Contracts.IPresenter {

    protected Contracts.IView view;

    public BasePresenter(Contracts.IView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public boolean isAttachView() {
        return this.view != null;
    }

    @Override
    public void gerMoreData() {
        if(!isAttachView()){
            return;
        }
    }
}
