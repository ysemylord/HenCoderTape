package com.xyb.tape.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2017/10/13.
 *
 * @author xyb
 */

public class MyTap extends HorizontalScrollFling {
    private static final String TAG = "MyTap";
    int lineOffset = 40;
    int indicatorHeight=200;
    int smallLineWidth = 5;
    int bigLineWidth = 10;
    int sortLineHeight = 50;
    int longLineHeight = 150;
    List<Float> kgs = new ArrayList<>();
    Paint kgPaint=new Paint();
    private Paint scalesPaint;
    private Paint indicatorPaint;

    public MyTap(Context context) {
        super(context);
    }

    public MyTap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        indicatorPaint=new Paint();
        indicatorPaint.setColor(Color.GREEN);
        indicatorPaint.setStrokeWidth(20);
        indicatorPaint.setStrokeCap(Paint.Cap.ROUND);

        scalesPaint = new Paint();
        scalesPaint.setStrokeCap(Paint.Cap.ROUND);
        scalesPaint.setColor(Color.GRAY);

        Float startKg = 30f;
        Float endKg = 100f;
        float needKg = startKg;
        while (needKg <= endKg) {
            needKg = needKg + 0.1f;
            kgs.add(needKg);
        }
        kgPaint.setTextSize(48);

    }

    public MyTap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(getScrollX()+getWidth()/2,0,getScrollX()+getWidth()/2,indicatorHeight,indicatorPaint);



        int startX = 0;
        int startY = 0;
        int endY = 0;

        for (int i = 0; i < kgs.size(); i++) {
            float nowKg = kgs.get(i);
            if (isLongLine(nowKg)) {
                endY = startY + longLineHeight;
                scalesPaint.setStrokeWidth(bigLineWidth);
                String showKg=zeroXiaoshu(nowKg);

                Rect rect=new Rect();
                kgPaint.getTextBounds(showKg,0,showKg.length(),rect);
                canvas.drawText(showKg,startX-rect.width()/2,endY+rect.height()+20,kgPaint);
            } else {
                endY = startY + sortLineHeight;
                scalesPaint.setStrokeWidth(smallLineWidth);
            }
            canvas.drawLine(startX, 0, startX, endY, scalesPaint);
            Log.i(TAG, "onDraw: " + nowKg);
            startX += lineOffset;
        }
    }

    private boolean isLongLine(float value) {
        String resS = oneXiaoshu(value);
        String[] splits = resS.split("\\.");
        if (splits[1].equals("0")) {
            return true;
        }
        return false;
    }

    private String oneXiaoshu(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    }

    private String zeroXiaoshu(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }
}
