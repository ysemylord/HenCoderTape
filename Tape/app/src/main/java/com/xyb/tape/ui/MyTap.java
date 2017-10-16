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
    private float startKg = 30f;//最小刻度数
    private float endKg = 35f;//最大刻度数
    int lineGap = 40;//每个刻度间的间距
    int indicatorHeight = 150;//指示器高度
    int smallLineWidth = 5;//端刻度宽度
    int bigLineWidth = 8;//长刻度宽度
    private int indicatorWidth = 10;
    int sortLineHeight = 50;//短刻度高度
    int longLineHeight = 100;//长刻度高度
    int scalesColor = Color.GRAY;//刻度颜色
    int indicatorColor = Color.GREEN;//知识器颜色
    List<Float> kgs = new ArrayList<>();
    private Paint kgPaint;//文字画笔
    private Paint scalesPaint;//刻度画笔
    private Paint indicatorPaint;//姿势器画笔

    private OuterInterface mOuterInterface;

    public MyTap(Context context) {
        super(context);
    }

    public MyTap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        indicatorPaint = new Paint();
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setStrokeWidth(indicatorWidth);
        indicatorPaint.setStrokeCap(Paint.Cap.ROUND);

        kgPaint = new Paint();

        scalesPaint = new Paint();
        scalesPaint.setStrokeCap(Paint.Cap.ROUND);
        scalesPaint.setColor(scalesColor);
        kgPaint.setTextSize(48);


        float needKg = startKg;
        while (needKg <= endKg) {
            kgs.add(needKg);
            needKg = needKg + 0.1f;
        }

        setOneSetp(lineGap);
    }

    public MyTap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制中间的指示器
        canvas.drawLine(getScrollX() + getWidth() / 2, 0, getScrollX() + getWidth() / 2, indicatorHeight, indicatorPaint);


        int startX = getWidth() / 2;//从屏幕中间开始绘制刻度
        int startY = 0;
        int endY = 0;

        for (int i = 0; i < kgs.size(); i++) {
            float nowKg = kgs.get(i);
            if (isLongLine(nowKg)) {
                endY = startY + longLineHeight;
                scalesPaint.setStrokeWidth(bigLineWidth);
                String showKg = twoDecimal(nowKg);

                Rect rect = new Rect();
                kgPaint.getTextBounds(showKg, 0, showKg.length(), rect);
                canvas.drawText(showKg, startX - rect.width() / 2, endY + rect.height() + 20, kgPaint);
            } else {
                endY = startY + sortLineHeight;
                scalesPaint.setStrokeWidth(smallLineWidth);
            }
            canvas.drawLine(startX, 0, startX, endY, scalesPaint);
            startX += lineGap;


        }

        setMinScorll(0);
        setMaxScroll(startX - getWidth() / 2 - lineGap);
    }

    private boolean isLongLine(float value) {
        String resS = oneDecimal(value);
        String[] splits = resS.split("\\.");
        if (splits[1].equals("0")) {
            return true;
        }
        return false;
    }

    /**
     * 保留一位小数
     *
     * @param value
     * @return
     */
    private String oneDecimal(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    }

    private String twoDecimal(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        try {

            int num = getScrollX() / lineGap;
            float kg = kgs.get(num);
            if (mOuterInterface != null) {
                mOuterInterface.nowKg(oneDecimal(kg));
            }
            Log.i(TAG, "now kg is " + kg);
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

    public void setStartKg(float startKg) {
        this.startKg = startKg;
    }

    public void setEndKg(float endKg) {
        this.endKg = endKg;
    }
}
