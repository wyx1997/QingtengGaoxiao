package com.victory.qingteng.qingtenggaoxiao.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.victory.qingteng.qingtenggaoxiao.Contracts;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ExamYuanPresenter extends BasePresenter {

    public ExamYuanPresenter(Contracts.IView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    public void getData(final Context context, final int position){
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                InputStream inputStream = context.getAssets().open("kaoshiyuan.xls");
                Workbook workbook = Workbook.getWorkbook(inputStream);
                Sheet sheet = workbook.getSheet(0);
                List<String> list = new ArrayList<>();
                for(Cell cell : sheet.getRow(position)){
                    list.add(cell.getContents());
                }
                emitter.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        view.showData(strings);
                    }
                });
    }
}
