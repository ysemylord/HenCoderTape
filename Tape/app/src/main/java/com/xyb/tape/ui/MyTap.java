package com.xyb.tape.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.xyb.tape.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2017/10/13.
 *
 * @author xyb
 */

public class MyTap extends HorizontalScroll {
    private static final String TAG = "MyTap";
    private float startNum ;//最小刻度数
    private float endNum ;//最大刻度数
    private float internalNum ;//每一小格代表的刻度

    int scaleGap = 40;//每个刻度间的间距


    int sortLineWidth = 5;//短刻度宽度
    int sortLineHeight = 50;//短刻度高度
    int sortScaleColor = Color.GRAY;//短刻度颜色
    Paint sortScalePaint;//短刻度画笔

    int longLineWidth = 8;//长刻度宽度
    int longLineHeight = 100;//长刻度高度
    int longScaleColor = Color.GRAY;//长 刻度颜色;
    Paint longScalePaint;//刻度画笔

    int indicatorHeight = 150;//指示器高度
    int indicatorWidth = 10;//指示器宽度
    int indicatorColor = Color.GREEN;//知识器颜色
    Paint indicatorPaint;//指示器画笔

    List<Float> kgs = new ArrayList<>();
    private Paint kgPaint;//文字画笔


    private OuterInterface mOuterInterface;

    public MyTap(Context context) {
        super(context);
    }

    public MyTap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init( context,  attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTap, 0, 0);
        startNum=getAttrStringToFlaot(typedArray,R.styleable.MyTap_startNum,20f);
        endNum= getAttrStringToFlaot(typedArray,R.styleable.MyTap_endNum,30f);
        internalNum= getAttrStringToFlaot(typedArray,R.styleable.MyTap_internalNum,0.2f);

        typedArray.recycle();
        indicatorPaint = new Paint();
        kgPaint = new Paint();
        sortScalePaint = new Paint();
        longScalePaint = new Paint();

        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setStrokeWidth(indicatorWidth);
        indicatorPaint.setStrokeCap(Paint.Cap.ROUND);

        kgPaint.setTextSize(48);

        sortScalePaint.setStrokeCap(Paint.Cap.ROUND);
        sortScalePaint.setColor(longScaleColor);
        sortScalePaint.setStrokeWidth(longLineWidth);

        longScalePaint.setStrokeCap(Paint.Cap.ROUND);
        longScalePaint.setColor(sortScaleColor);
        longScalePaint.setStrokeWidth(sortLineWidth);


        float needKg = startNum;
        while (MathUtil.compareTwoNum(needKg, endNum) <= 0) {
            kgs.add(needKg);
            needKg = needKg + internalNum;
        }

        setOneStep(scaleGap);
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

        //绘制刻度
        for (int i = 0; i < kgs.size(); i++) {
            float nowKg = kgs.get(i);
            Paint linePaint;
            if (isLongLine(nowKg)) {

                linePaint = sortScalePaint;
                endY = startY + longLineHeight;

                String showKg = MathUtil.zeroDecimal(nowKg);
                Rect rect = new Rect();
                kgPaint.getTextBounds(showKg, 0, showKg.length(), rect);
                canvas.drawText(showKg, startX - rect.width() / 2, endY + rect.height() + 20, kgPaint);
            } else {
                linePaint = longScalePaint;
                endY = startY + sortLineHeight;
            }
            canvas.drawLine(startX, 0, startX, endY, linePaint);
            startX += scaleGap;


        }

        setLeftMaxScorll(0);
        setRightMaxScroll(startX - getWidth() / 2 - scaleGap);
    }

    /**
     * 决定是否是长线
     * 可以覆盖此方法，决定value为几时，为长线
     *
     * @param value
     * @return
     */
    public boolean isLongLine(float value) {
        String resS = MathUtil.oneDecimal(value);
        String[] splits = resS.split("\\.");
        if (splits[1].equals("0")) {
            return true;
        }
        return false;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        try {

            int num = getScrollX() / scaleGap;
            float kg = kgs.get(num);
            if (mOuterInterface != null) {
                mOuterInterface.nowKg(MathUtil.oneDecimal(kg));
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

    public void setStartNum(float startNum) {
        this.startNum = startNum;
        invalidate();
    }

    public void setEndNum(float endNum) {
        this.endNum = endNum;
        invalidate();
    }

    public float getAttrStringToFlaot(TypedArray typedArray,int atrr,float defaultValue){
       String value= typedArray.getString(atrr);
       if(TextUtils.isEmpty(value)){
           return  defaultValue;
       }
       return Float.parseFloat(value);
    }
}
