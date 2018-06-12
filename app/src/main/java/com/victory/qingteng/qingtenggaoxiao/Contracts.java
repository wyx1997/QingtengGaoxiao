package com.victory.qingteng.qingtenggaoxiao;

import java.util.List;

public interface Contracts {

    interface IModel<T>{

        void getFromLocal(String key, ICallback<T> callback);

        void getFromNetwork(String id, ICallback<T> callback);
    }

    interface IView<T>{

        /**
         * hide loading progress when the data has loaded already, no matter successfully or failed
         */
        void hideLoading();

        /**
         * show loading progress when the data has started to load
         */
        void showLoading();

        /**
         * show the data Presenter layer give
         *
         * @param data data from Model layer
         */
        void showData(T data);

        /**
         * show error message
         *
         * @param msg error message
         */
        void showError(String msg);


        /**
         * show failure message
         *
         * @param msg failure message
         */
        void showFailure(String msg);
    }

    interface IPresenter{

        void detachView();

        boolean isAttachView();

        void gerMoreData();
    }

    interface ICallback<T>{

        /**
         * call when Model layer has loaded the data successfully
         *
         * @param data data the Model layer return
         */
        void onSuccess(T data);


        /**
         * call when Model layer cannot load the data, mainly because network is not working and so on
         *
         * @param errorMsg message about the error
         */
        void onError(String errorMsg);


        /**
         * call when Model layer has failed to load the data, mainly because the server has not got the data
         * or the server is not working
         *
         * @param failMsg message about the failure
         */
        void onFailure(String failMsg);
    }
}
