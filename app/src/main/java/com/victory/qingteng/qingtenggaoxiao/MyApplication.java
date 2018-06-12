package com.victory.qingteng.qingtenggaoxiao;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;
import com.victory.qingteng.qingtenggaoxiao.utils.ExcelUtils;
import com.victory.qingteng.qingtenggaoxiao.utils.SharedPfUtils;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jxl.read.biff.BiffException;

public class MyApplication extends Application{

    public static final String KEY_INIT = "key_init";

    public static final String INTERRUPT = "interrupt";

    private static Context appContext;

    private RefWatcher watcher;

    public static Context getAppContext(){
        return appContext;
    }

    public static MyApplication get(){
        return AppHolder.application;
    }

    public RefWatcher getRefWatcher(Context context){
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.watcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        watcher = LeakCanary.install(this);
        QbSdk.initX5Environment(appContext, null);
        initDB(appContext);
    }

    @SuppressLint("CheckResult")
    private void initDB(final Context context){
        if((boolean) SharedPfUtils.getFromSPF(this, KEY_INIT, false)) return;
        initData(context);
    }

    private void initData(final Context context){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                ExcelUtils.readExcel2DB(context);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        SharedPfUtils.save2SPF(MyApplication.this, KEY_INIT, true);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(throwable instanceof IOException){
                            throw new RuntimeException(getString(R.string.init_db_io_error));
                        } else if (throwable instanceof BiffException) {
                            throw new RuntimeException(getString(R.string.init_db_read_error));
                        } else {
                            throwable.printStackTrace();
                        }
                    }
                })
                .subscribe();
    }

    private static class AppHolder{
        static MyApplication application = new MyApplication();
    }
}
