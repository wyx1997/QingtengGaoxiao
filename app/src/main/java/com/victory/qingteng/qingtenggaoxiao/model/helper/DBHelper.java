package com.victory.qingteng.qingtenggaoxiao.model.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.victory.qingteng.qingtenggaoxiao.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DBHelper {

    private static int progress = 0;

    public static final int TYPE_SCHOOL = 0;
    public static final int TYPE_MAJOR = 1;
    public static final int TYPE_YILIU = 2;

    public final String[] schoolKeys = new String[]{"name", "province", "city", "img", "zhuguan", "category", "project", "level",
            "banxue", "yiliu", "guanwang", "zhaosheng", "weibo", "number", "collect"};

    public final String[] majorKeys = new String[]{"name", "level", "category", "collect"};

    public final String[] yiliuMajorKeys = new String[]{"schoolname", "major"};

    private MajorDBOpenHelper majorHelper = new MajorDBOpenHelper(MyApplication.getAppContext(), 1);

    private SchoolDBOpenHelper schoolHelper = new SchoolDBOpenHelper(MyApplication.getAppContext(), 1);

    private YiliuMajorOpenHelper yiliuHelper = new YiliuMajorOpenHelper(MyApplication.getAppContext(), 1);

    public static DBHelper getInstance(){
        return HelperHolder.helper;
    }

    public synchronized SQLiteOpenHelper getHelper(int type){
        switch (type){
            case DBHelper.TYPE_MAJOR: return majorHelper;
            case DBHelper.TYPE_YILIU: return yiliuHelper;
            default: return schoolHelper;
        }
    }

    public synchronized SQLiteDatabase getWritableDB(int type){
        switch (type){
            case TYPE_SCHOOL:
                return schoolHelper.getWritableDatabase();
            case TYPE_MAJOR:
                return majorHelper.getWritableDatabase();
            case TYPE_YILIU:
                return yiliuHelper.getWritableDatabase();
                default:
                    return null;
        }
    }

    public synchronized SQLiteDatabase getReadableDB(int type){
        switch (type){
            case TYPE_SCHOOL:
                return schoolHelper.getReadableDatabase();
            case TYPE_MAJOR:
                return majorHelper.getReadableDatabase();
            case TYPE_YILIU:
                return yiliuHelper.getReadableDatabase();
                default:
                    return null;
        }
    }

    private List<String> handleValueCursor(Cursor cursor, String columnName){
        List<String> names = new ArrayList<>();
        int index = cursor.getColumnIndex(columnName);
        while (cursor.moveToNext()) {
            names.add(cursor.getString(index));
        }
        return names;
    }

    private List<String> handleAllCursor(Cursor cursor, int type){
        List<String> allValues = new ArrayList<>();
        String[] keys;
        switch (type){
            case TYPE_MAJOR: keys = majorKeys; break;
            case TYPE_YILIU: keys = yiliuMajorKeys; break;
            default: keys = schoolKeys;
        }
        for(String key : keys){
            cursor.moveToFirst();
            allValues.add(cursor.getString(cursor.getColumnIndex(key)));
        }
        return allValues;
    }

    public synchronized void insertValuesDirect(int type, List<ContentValues> values){
        SQLiteDatabase database = null;
        try {
            String tableName = getTable(type);
            database = getWritableDB(type);
            database.beginTransaction();
            for(ContentValues contentValues : values){
                database.replace(tableName, null, contentValues);
                EventBus.getDefault().post(++progress);
            }
            database.setTransactionSuccessful();
        } finally {
            if(null != database){
                database.endTransaction();
                database.close();
            }
        }
    }

    public synchronized Observable<Boolean> updateValueByArgs(final int type, final ContentValues values, final String whereClause, final String[] whereArgs){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                SQLiteDatabase database = getWritableDB(type);
                for(String item : whereArgs){
                    database.update(getTable(type), values, whereClause, new String[]{item});
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public synchronized Observable<Boolean> updateValueByArgs(final int type, final ContentValues values, final String whereClause, final List<String> whereArgs){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                SQLiteDatabase database = getWritableDB(type);
                for(String item : whereArgs){
                    database.update(getTable(type), values, whereClause, new String[]{item});
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public synchronized Observable<List<String>> queryValueByArgs(final int type, final String[] columns, final String selection, final String[] selectionArgs){
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                Cursor cursor = null;
                try {
                     cursor = getReadableDB(type).query(getTable(type), columns, selection,
                            selectionArgs, null, null, null);
                    if(null == cursor || cursor.getCount() == 0){
                        emitter.onNext(new ArrayList<String>());
                        return;
                    }
                    emitter.onNext(columns == null ? handleAllCursor(cursor, type) : handleValueCursor(cursor, columns[0]));
                } finally {
                    if(null != cursor){
                        cursor.close();
                    }
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public synchronized Observable<Integer> queryValueSum(final int type, final String[] columns, final String selection, final String[] whereArgs){
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Cursor cursor = null;
                try {
                    cursor = getReadableDB(type).query(getTable(type), columns, selection, whereArgs,
                            null, null, null);
                    emitter.onNext(cursor.getCount());
                } finally {
                    if(null != cursor){
                        cursor.close();
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public synchronized Observable<Boolean> deleteDB(final int type){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(MyApplication.getAppContext().deleteDatabase(getTable(type)));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public synchronized Observable<Boolean> deleteAllDB(){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                boolean isDelete;
                isDelete = MyApplication.getAppContext().deleteDatabase(getTable(DBHelper.TYPE_SCHOOL));
                if(isDelete){
                    isDelete = MyApplication.getAppContext().deleteDatabase(getTable(DBHelper.TYPE_MAJOR));
                    if(isDelete){
                        isDelete = MyApplication.getAppContext().deleteDatabase(getTable(DBHelper.TYPE_YILIU));
                    }
                }
                emitter.onNext(isDelete);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private String getTable(int type){
        String table;
        switch (type){
            case TYPE_MAJOR: table = "major"; break;
            case TYPE_YILIU: table = "yiliumajor"; break;
            default: table = "school";
        }
        return table;
    }

    private static class HelperHolder{
        public static final DBHelper helper = new DBHelper();
    }
}


