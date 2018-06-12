package com.victory.qingteng.qingtenggaoxiao.utils;

import android.os.SystemClock;
import android.widget.Toast;

import com.victory.qingteng.qingtenggaoxiao.MyApplication;

public class ToastUtils {

    private static Toast mToast;
    private static long lastTime;
    private static String lastText;

    public static void showToast(String text){
        long time = SystemClock.currentThreadTimeMillis();
        if(null != mToast){
            if((time - lastTime) < Toast.LENGTH_SHORT && lastText.equals(text)) return;
        } else {
            mToast = Toast.makeText(MyApplication.getAppContext(), null, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        lastText = text;
        lastTime = time;
        mToast.show();
    }

    public static void showToast(int resId){
        showToast(MyApplication.getAppContext().getResources().getString(resId));
    }
}
