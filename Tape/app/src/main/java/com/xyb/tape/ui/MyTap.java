package com.xyb.tape.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
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
    private float startScale;//最小刻度数
    private float endScale;//最大刻度数
    private float internalScale;//每一小格代表的刻度
    private float currentScale;//设置当前的刻度

    private int scaleGap = 40;//每个刻度间的间距


    private int sortLineWidth = 5;//短刻度宽度
    private int sortLineHeight = 50;//短刻度高度
    private int sortScaleColor = Color.GRAY;//短刻度颜色
    private Paint sortScalePaint;//短刻度画笔

    private int longLineWidth = 8;//长刻度宽度
    private int longLineHeight = 100;//长刻度高度
    private int longScaleColor = Color.GRAY;//长 刻度颜色;
    private Paint longScalePaint;//刻度画笔

    private int indicatorLineHeight = 150;//指示器高度
    private int indicatorLineWidth = 10;//指示器宽度
    private int indicatorLineColor = Color.GREEN;//知识器颜色
    private Paint indicatorPaint;//指示器画笔


    private int topHorizontalLineHeight;
    private int topHorizontalLineColor;
    private Paint topHorizontalLinePaint;


    private List<Float> scaleList = new ArrayList<>();
    private Paint kgPaint;//文字画笔
    private int textSize;
    private int textColor;
    private int textMarginTop;


    private OuterInterface mOuterInterface;

    public MyTap(Context context) {
        super(context);
    }

    public MyTap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTap, 0, 0);
        startScale = typedArray.getFloat(R.styleable.MyTap_startScale, 20f);
        endScale = typedArray.getFloat(R.styleable.MyTap_endScale, 30f);
        internalScale = typedArray.getFloat(R.styleable.MyTap_internalScale, 0.1f);
        currentScale = typedArray.getFloat(R.styleable.MyTap_currentScale, startScale);

        scaleGap = typedArray.getDimensionPixelOffset(R.styleable.MyTap_scaleGap, 20);

        sortLineWidth = typedArray.getDimensionPixelOffset(R.styleable.MyTap_sortLineWidth, 5);//短刻度宽度
        sortLineHeight = typedArray.getDimensionPixelOffset(R.styleable.MyTap_sortLineHeight, 50);//短刻度高度
        sortScaleColor = typedArray.getColor(R.styleable.MyTap_sortScaleColor, Color.GRAY);//短刻度颜色

        longLineWidth = typedArray.getDimensionPixelOffset(R.styleable.MyTap_longLineWidth, 5);//短刻度宽度
        longLineHeight = typedArray.getDimensionPixelOffset(R.styleable.MyTap_longLineHeight, 50);//短刻度高度
        longScaleColor = typedArray.getColor(R.styleable.MyTap_longScaleColor, Color.GRAY);//短刻度颜色

        indicatorLineWidth = typedArray.getDimensionPixelOffset(R.styleable.MyTap_indicatorLineWidth, 10);//短刻度宽度
        indicatorLineHeight = typedArray.getDimensionPixelOffset(R.styleable.MyTap_indicatorLineHeight, 150);//短刻度高度
        indicatorLineColor = typedArray.getColor(R.styleable.MyTap_indicatorScaleColor, Color.GREEN);//短刻度颜色

        textSize = typedArray.getDimensionPixelSize(R.styleable.MyTap_textSize, 38);
        textColor = typedArray.getColor(R.styleable.MyTap_textColor, Color.GRAY);
        textMarginTop = typedArray.getDimensionPixelOffset(R.styleable.MyTap_textMarginTop, 20);

        topHorizontalLineHeight = typedArray.getDimensionPixelOffset(R.styleable.MyTap_topHorizontalLineHeight, 2);
        topHorizontalLineColor = typedArray.getColor(R.styleable.MyTap_topHorizontalLineColor, Color.GRAY);

        typedArray.recycle();
        indicatorPaint = new Paint();
        kgPaint = new Paint();
        sortScalePaint = new Paint();
        longScalePaint = new Paint();
        topHorizontalLinePaint = new Paint();

        indicatorPaint.setColor(indicatorLineColor);
        indicatorPaint.setStrokeWidth(indicatorLineWidth);
        indicatorPaint.setStrokeCap(Paint.Cap.ROUND);

        topHorizontalLinePaint.setColor(topHorizontalLineColor);
        topHorizontalLinePaint.setStrokeWidth(topHorizontalLineHeight);

        sortScalePaint.setStrokeCap(Paint.Cap.ROUND);
        sortScalePaint.setColor(longScaleColor);
        sortScalePaint.setStrokeWidth(longLineWidth);

        longScalePaint.setStrokeCap(Paint.Cap.ROUND);
        longScalePaint.setColor(sortScaleColor);
        longScalePaint.setStrokeWidth(sortLineWidth);

        kgPaint.setAntiAlias(true);
        kgPaint.setTextSize(textSize);
        kgPaint.setColor(textColor);

        float needKg = startScale;
        while (MathUtil.compareTwoNum(needKg, endScale) <= 0) {
            scaleList.add(needKg);
            needKg = needKg + internalScale;
        }

        setOneStep(scaleGap);

        setLeftMaxScorll(0);
        setRightMaxScroll((scaleList.size() - 1) * scaleGap);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollTo((int) (scaleGap * ((currentScale - startScale) / internalScale)), getScrollY());
            }
        }, 200);

    }

    public MyTap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i(TAG, "onDraw: start");
        int startX = getWidth() / 2;//从屏幕中间开始绘制刻度
        int startY = 0;
        int endY = 0;

        //绘制刻度
        for (int i = 0; i < scaleList.size(); i++) {
            if (startX > getScrollX() && startX < getScrollX() + getWidth()) {
                float nowKg = scaleList.get(i);
                Paint linePaint;
                if (isLongLine(nowKg)) {

                    linePaint = sortScalePaint;
                    endY = startY + longLineHeight;

                    String showKg = MathUtil.zeroDecimal(nowKg);
                    Rect rect = new Rect();
                    kgPaint.getTextBounds(showKg, 0, showKg.length(), rect);
                    canvas.drawText(showKg, startX - rect.width() / 2, endY + rect.height() + textMarginTop, kgPaint);
                } else {
                    linePaint = longScalePaint;
                    endY = startY + sortLineHeight;
                }
                canvas.drawLine(startX, 0, startX, endY, linePaint);
            }
            startX += scaleGap;
        }


        Log.i(TAG, "onDraw: end");
    }

    /**
     * dispatchDraw是绘制子View的方法，onDraw之后调用，这里用来绘制刻度指示器和顶部横线
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //绘制中间的指示器
        canvas.drawLine(getScrollX() + getWidth() / 2, 0, getScrollX() + getWidth() / 2, indicatorLineHeight, indicatorPaint);

        //顶部横线
        canvas.drawLine(0, 0, getScrollX() + getWidth(), 0, topHorizontalLinePaint);


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

            int index = getScrollX() / scaleGap;
            float nowScale = scaleList.get(index);
            if (mOuterInterface != null) {
                mOuterInterface.nowScale(MathUtil.oneDecimal(nowScale));
            }
            Log.i(TAG, "now nowScale is " + nowScale);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "超出数组长度");
        }
    }

    public interface OuterInterface {
        void nowScale(String nowScale);
    }

    public void setOuterInterface(OuterInterface outerInterface) {
        this.mOuterInterface = outerInterface;
    }

    public void setStartScale(float startScale) {
        this.startScale = startScale;
        invalidate();
    }

    public void setEndScale(float endScale) {
        this.endScale = endScale;
        invalidate();
    }


}
