package com.victory.qingteng.qingtenggaoxiao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SharedPfUtils {

    private static final String APP = "app";
    private static final String MAIN_LIST = "main_list";
    private static final String MAIN_ID = "main_id";

    public static void save2SPF(Context context, String key, boolean value){
        SharedPreferences preferences = context.getSharedPreferences(APP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void save2SPF(Context context, String key, long value){
        SharedPreferences preferences = context.getSharedPreferences(APP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void remove(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static Object getFromSPF(Context context, String key, long defValue){
        SharedPreferences preferences = context.getSharedPreferences(APP, Context.MODE_PRIVATE);
        return preferences.getLong(key, defValue);
    }

    public static Object getFromSPF(Context context, String key, boolean defValue){
        SharedPreferences preferences = context.getSharedPreferences(APP, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defValue);
    }

    public static Observable saveMainData2SPF(final Context context, final String itemKey, final String[] items, final String idKey, final int[] ids){
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                SharedPreferences preferences = context.getSharedPreferences(MAIN_LIST, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                SharedPreferences preferences1 = context.getSharedPreferences(MAIN_ID, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                for(int i=0; i<items.length; i++){
                    editor.putString(itemKey+"_"+i, items[i]);
                    editor1.putInt(idKey+"_"+i, ids[i]);
                }
                editor.apply();
                editor1.apply();
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static String[] getMainListFromSPF(final Context context, final String key, final int size){
        SharedPreferences preferences = context.getSharedPreferences(MAIN_LIST, Context.MODE_PRIVATE);
        String[] data = new String[size];
        for(int i=0; i<size; i++){
            data[i] = (preferences.getString(key+"_"+i, null));
        }
        return data;
    }

    public static int[] getMainArrayFromSPF(final Context context, final String key, final int size){
        SharedPreferences preferences = context.getSharedPreferences(MAIN_ID, Context.MODE_PRIVATE);
        int[] ids = new int[size];
        for(int i=0; i<size; i++){
            ids[i] = preferences.getInt(key+"_"+i, -1);
        }
        return ids;
    }
}
