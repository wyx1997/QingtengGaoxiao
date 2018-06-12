package com.victory.qingteng.qingtenggaoxiao.model.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MajorDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "major.db";

    public MajorDBOpenHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table major(" +
                "name varchar(15)," +
                "level char(2)," +
                "category varchar(9)," +
                "collect varchar(1)," +
                "primary key(name, level)" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
