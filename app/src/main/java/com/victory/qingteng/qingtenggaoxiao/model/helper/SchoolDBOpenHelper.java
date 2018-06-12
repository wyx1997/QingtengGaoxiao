package com.victory.qingteng.qingtenggaoxiao.model.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchoolDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "school.db";

    public SchoolDBOpenHelper(Context context,int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table school(" +
                "name varchar(16) primary key," +
                "province char(3)," +
                "city char(5)," +
                "img char(4)," +
                "zhuguan char(8)," +
                "category char(3)," +
                "project char(1)," +
                "level char(2)," +
                "banxue char(1)," +
                "yiliu char(1)," +
                "yiliunum char(2)," +
                "guanwang char(20)," +
                "zhaosheng char(30)," +
                "weibo char(30)," +
                "number char(4)," +
                "collect char(1)" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
