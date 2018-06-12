package com.victory.qingteng.qingtenggaoxiao.utils;

import android.annotation.SuppressLint;
import android.support.v4.util.LruCache;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxCache {

    private LruCache<String, List<List<String>>> doubleListCache;
    private LruCache<String, List<String>> listCache;
    private LruCache<String, String> stringCache;

    private RxCache() {
        int memory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = memory / 8;
        doubleListCache = new LruCache<>(cacheSize);
        listCache = new LruCache<>(cacheSize);
        stringCache = new LruCache<>(cacheSize);
    }

    public static RxCache get(){
        return Holder.cache;
    }

    public Observable<Boolean> putStringLists(final String key, final List<List<String>> data){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                doubleListCache.put(key, data);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<List<String>>> getStringLists(final String key){
        return Observable.create(new ObservableOnSubscribe<List<List<String>>>() {
            @Override
            public void subscribe(ObservableEmitter<List<List<String>>> emitter) throws Exception {
                if(doubleListCache.get(key) == null){
                    emitter.onError(new Throwable());
                } else {
                    emitter.onNext(doubleListCache.get(key));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<Boolean> putStringsList(final List<String> key, final List<String> data){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                for(int i=0; i<data.size(); i++){
                    stringCache.put(key.get(i), data.get(i));
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> putString(final String key, final String data){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                stringCache.put(key, data);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> putStringList(final String key, final List<String> data){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                listCache.put(key, data);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<String>> getStringList(final String key){
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                if(listCache.get(key) == null){
                    emitter.onError(new Throwable());
                } else {
                    emitter.onNext(listCache.get(key));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<String> getString(final String key){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if(stringCache.get(key) == null){
                    emitter.onError(new Throwable());
                } else {
                    emitter.onNext(stringCache.get(key));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static class Holder{
        static RxCache cache = new RxCache();
    }
}
