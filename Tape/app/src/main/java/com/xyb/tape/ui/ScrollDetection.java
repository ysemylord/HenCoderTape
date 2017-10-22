package com.xyb.tape.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2017/10/13.
 *
 * @author xyb
 */

public class ScrollDetection extends HorizontalScroll {
    private static final String TAG = "MyTap";

    private int indicatorWidth = 10;

    int scalesColor = Color.GRAY;//刻度颜色
    int indicatorColor = Color.GREEN;//指示器颜色
    List<Integer> lineList = new ArrayList<>();
    private Paint linePaint;//文字画笔
    private Paint scalesPaint;//刻度画笔
    private Paint indicatorPaint;//姿势器画笔

    private OuterInterface mOuterInterface;

    public ScrollDetection(Context context) {
        super(context);
    }

    public ScrollDetection(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        indicatorPaint = new Paint();
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setStrokeWidth(indicatorWidth);
        indicatorPaint.setStrokeCap(Paint.Cap.ROUND);

        linePaint = new Paint();

        scalesPaint = new Paint();
        scalesPaint.setStrokeCap(Paint.Cap.ROUND);
        scalesPaint.setColor(scalesColor);
        linePaint.setTextSize(48);


        for (int i = -5000; i < 5000; i++) {
            if(i%20==0) {
                lineList.add(i);
            }
        }

    }

    public ScrollDetection(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawLine(getScrollX(),0,getScrollX(),100,indicatorPaint);

        for (int i = 0; i < lineList.size(); i++) {
            Integer lineX = lineList.get(i);
            canvas.drawLine(lineX,0, lineX,100,linePaint);

        }

    }





    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        try {

            int num = getScrollX() ;
            if (mOuterInterface != null) {
                mOuterInterface.nowKg((num+""));
            }
            Log.i(TAG, "now scroll is " + num);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "超出数组长度");
        }
    }

    public interface OuterInterface {
        void nowKg(String nowKG);
    }

    public void setmOuterInterface(OuterInterface mOuterInterface) {
        this.mOuterInterface = mOuterInterface;
    }
}
