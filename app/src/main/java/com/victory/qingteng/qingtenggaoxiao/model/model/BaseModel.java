package com.victory.qingteng.qingtenggaoxiao.model.model;

import com.google.gson.Gson;
import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.MyApplication;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.api.CommonApi;
import com.victory.qingteng.qingtenggaoxiao.model.api.GetApi;
import com.victory.qingteng.qingtenggaoxiao.model.entity.BaseResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class BaseModel {

    protected GetApi getApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonApi.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(GetApi.class);
    }

    class BaseCacheObserver<T> implements Observer<T>{

        Contracts.ICallback callback;

        BaseCacheObserver(Contracts.ICallback callback) {
            this.callback = callback;
        }

        Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(T t) {
            callback.onSuccess(t);
            disposable.dispose();
        }

        @Override
        public void onError(Throwable e) {
            callback.onError(null);
        }

        @Override
        public void onComplete() {

        }
    }

    class BaseNetworkObserver implements Observer<String> {

        Disposable disposable;

        Contracts.ICallback iCallback;

        BaseNetworkObserver(Contracts.ICallback callback) {
            iCallback = callback;
        }

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(String string) {
            handleNetworkOnNext(string, iCallback);
            disposable.dispose();
        }

        @Override
        public void onError(Throwable e) {
            iCallback.onFailure(MyApplication.getAppContext().getResources().getString(R.string.network_request_failure));
            disposable.dispose();
        }

        @Override
        public void onComplete() {

        }
    }

    abstract void handleNetworkOnNext(String string, Contracts.ICallback callback);

    protected void showFailure(Contracts.ICallback callback){
        callback.onFailure(MyApplication.getAppContext().getResources().getString(R.string.network_request_failure));
    }

    protected <T> BaseResult<T> getBaseResult(String jsonStr, String rootName, Type type) throws JSONException {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(jsonStr);
        return gson.fromJson(jsonObject.get(rootName).toString(), type);
    }
}
