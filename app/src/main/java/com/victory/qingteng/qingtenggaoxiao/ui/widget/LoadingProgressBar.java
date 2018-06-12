package com.victory.qingteng.qingtenggaoxiao.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.victory.qingteng.qingtenggaoxiao.R;

public class LoadingProgressBar extends View{

    private Paint pointPaint;

    private int jumpHeight;

    private Point centerPoint;

    private int spacing;

    private int pointRadius;

    private int pointHeight = 0;

    private boolean isUp = true;

    private int pos2Move = 0;

    public LoadingProgressBar(Context context) {
        this(context, null);
    }

    public LoadingProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(dm);
            centerPoint = new Point(dm.widthPixels / 2, dm.heightPixels / 2);
            jumpHeight = context.getResources().getDimensionPixelSize(R.dimen.jump_height);
            spacing = context.getResources().getDimensionPixelSize(R.dimen.point_spacing);
            pointRadius = context.getResources().getDimensionPixelSize(R.dimen.point_radius);
            pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            pointPaint.setColor(context.getResources().getColor(R.color.colorPrimary));
            pointPaint.setStyle(Paint.Style.FILL);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isUp){
            if(pointHeight < jumpHeight){
                pointHeight += 8;
            } else {
                isUp = false;
            }
        } else {
            if(pointHeight > 0){
                pointHeight -= 8;
            } else {
                isUp = true;
                pos2Move = (pos2Move+1) % 3;
            }
        }

        switch (pos2Move){
            case 0: {
                canvas.drawCircle(centerPoint.x - spacing, centerPoint.y - pointHeight, pointRadius, pointPaint);
                canvas.drawCircle(centerPoint.x, centerPoint.y, pointRadius, pointPaint);
                canvas.drawCircle(centerPoint.x + spacing, centerPoint.y, pointRadius, pointPaint);
                break;
            }
            case 1: {
                canvas.drawCircle(centerPoint.x - spacing, centerPoint.y, pointRadius, pointPaint);
                canvas.drawCircle(centerPoint.x, centerPoint.y - pointHeight, pointRadius, pointPaint);
                canvas.drawCircle(centerPoint.x + spacing, centerPoint.y, pointRadius, pointPaint);
                break;
            }
            case 2: {
                canvas.drawCircle(centerPoint.x - spacing, centerPoint.y, pointRadius, pointPaint);
                canvas.drawCircle(centerPoint.x, centerPoint.y, pointRadius, pointPaint);
                canvas.drawCircle(centerPoint.x + spacing, centerPoint.y - pointHeight, pointRadius, pointPaint);
                break;
            }
        }
        postInvalidateDelayed(5);
    }
}
