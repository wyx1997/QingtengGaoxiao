package com.victory.qingteng.qingtenggaoxiao.model.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class YiliuMajorOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "yiliu_major_db";

    public YiliuMajorOpenHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table yiliumajor(" +
                //"_id integer primary key autoincrement," +
                "schoolname char(8)," +
                "major varchar(12)," +
                "primary key(schoolname, major)" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
