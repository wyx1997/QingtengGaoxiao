package com.victory.qingteng.qingtenggaoxiao.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class ExcelUtils {

    @SuppressLint("CheckResult")
    public static void readExcel2DB(Context context) throws Exception {
        InputStream inputStream = context.getAssets().open("school_links.xls");
        Workbook workbook = Workbook.getWorkbook(inputStream);
        Sheet sheet = workbook.getSheet(0);
        int rows = sheet.getRows();
        List<ContentValues> insertValue = new ArrayList<>();
        String[] schoolKeys = DBHelper.getInstance().schoolKeys;
        for (int i = 0; i < rows; ++i) {
            ContentValues values = new ContentValues(schoolKeys.length);
            int j = 0;
            for (; j < schoolKeys.length - 1; ++j) {
                values.put(schoolKeys[j], sheet.getCell(j, i).getContents());
            }
            values.put(schoolKeys[j], "0");
            insertValue.add(values);
        }
        DBHelper.getInstance().insertValuesDirect(DBHelper.TYPE_SCHOOL, insertValue);
        workbook.close();

        readMajor(context, "benke_links.xls", "本科");
        readMajor(context, "zhuanke_links.xls", "专科");

        InputStream inputStream1 = context.getAssets().open("yiliu_xueke.xls");
        Workbook workbook1 = Workbook.getWorkbook(inputStream1);
        Sheet sheet1 = workbook1.getSheet(0);
        int rows1 = sheet1.getRows();
        List<ContentValues> list = new ArrayList<>();
        String[] yiliuKeys = DBHelper.getInstance().yiliuMajorKeys;
        for (int i = 0; i < rows1; i++) {
            ContentValues values1 = new ContentValues(yiliuKeys.length);
            values1.put(yiliuKeys[0], sheet1.getCell(0, i).getContents());
            values1.put(yiliuKeys[1], sheet1.getCell(1, i).getContents());
            list.add(values1);
        }
        workbook1.close();
        DBHelper.getInstance().insertValuesDirect(DBHelper.TYPE_YILIU, list);
    }

    private static void readMajor(Context context, String fileName, String level) throws Exception {
        InputStream inputStream = context.getAssets().open(fileName);
        Workbook workbook = Workbook.getWorkbook(inputStream);
        List<ContentValues> list = new ArrayList<>();
        String[] majorKeys = DBHelper.getInstance().majorKeys;
        for (Sheet sheet : workbook.getSheets()) {
            int rows1 = sheet.getRows();
            String name = sheet.getName();
            for (int i = 0; i < rows1; ++i) {
                ContentValues values = new ContentValues(majorKeys.length);
                values.put(majorKeys[0], sheet.getCell(0, i).getContents());
                values.put(majorKeys[1], level);
                values.put(majorKeys[2], name);
                values.put(majorKeys[3], "0");
                list.add(values);
            }
        }
        DBHelper.getInstance().insertValuesDirect(DBHelper.TYPE_MAJOR, list);
        workbook.close();
    }
}
