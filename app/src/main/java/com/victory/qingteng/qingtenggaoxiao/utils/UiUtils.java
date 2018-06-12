package com.victory.qingteng.qingtenggaoxiao.utils;

import android.util.Log;

import com.victory.qingteng.qingtenggaoxiao.MyApplication;

public class UiUtils {

    public static int getStatusBarHeight(){
        int result = 0;
        int resourceId = MyApplication.getAppContext().getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0){
            result = MyApplication.getAppContext().getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("uiutils", result+"");
        return result;
    }
}
