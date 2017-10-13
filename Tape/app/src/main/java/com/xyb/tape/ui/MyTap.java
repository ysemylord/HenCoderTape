package com.xyb.tape.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2017/10/13.
 *
 * @author xyb
 */

public class MyTap extends View {
    private static final String TAG = "MyTap";
    int lineOffset = 40;
    int smallLineWidth = 5;
    int bigLineWidth = 10;
    int sortLineHeight = 50;
    int longLineHeight = 150;
    List<Float> kgs = new ArrayList<>();

    public MyTap(Context context) {
        super(context);
    }

    public MyTap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Float startKg = 30f;
        Float endKg = 100f;
        float needKg = startKg;
        while (needKg <= endKg) {
            needKg = needKg + 0.1f;
            kgs.add(needKg);
        }
    }

    public MyTap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.GREEN);
        int startX = 0;
        int startY = 0;
        int endY = 0;

        for (int i = 0; i < kgs.size(); i++) {
            float nowKg = kgs.get(i);
            if (isLongLine(nowKg)) {
                endY = startY + longLineHeight;
                paint.setStrokeWidth(bigLineWidth);
            } else {
                endY = startY + sortLineHeight;
                paint.setStrokeWidth(smallLineWidth);
            }
            canvas.drawLine(startX, 0, startX, endY, paint);
            Log.i(TAG, "onDraw: " + nowKg);
            startX += lineOffset;
        }
    }

    private boolean isLongLine(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        String resS = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
        String[] splits = resS.split("\\.");
        if (splits[1].equals("0")) {
            return true;
        }
        return false;
    }
}
