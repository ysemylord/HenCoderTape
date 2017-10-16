package com.xyb.tape.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by xuyabo on 2017/9/10.
 * Scroller.fling  实现惯性滑动
 */

public class HorizontalScrollFling extends FrameLayout {
    private static final String TAG = "MyScrollView";
    private int mPointActivtyId = -1;
    private int mTouchSlop = -1;//用来判断手势移动的距离是否达到滑动的标准
    private float mLastX = -1;
    private Scroller mFlingScroller;
    private VelocityTracker mVelocityTracker;

    protected int minScorll, maxScroll;
    protected int oneSetp;//滑动的最小步长
    private int currentScroll;
    private Scroller mAjustScroll;


    public HorizontalScrollFling(Context context) {
        this(context, null);
    }

    public HorizontalScrollFling(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollFling(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFlingScroller = new Scroller(getContext());
        mAjustScroll=new Scroller(getContext());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }


        if (mFlingScroller == null) {
            mFlingScroller = new Scroller(getContext());
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = event.getActionIndex();
                mPointActivtyId = event.getPointerId(index);
                mLastX = event.getX();
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                int pointIndex = event.findPointerIndex(mPointActivtyId);
                if (pointIndex == -1) {
                    Log.i(TAG, "onTouchEvent: error index");
                    break;
                }
                float x = event.getX(pointIndex);
                float dx = x - mLastX;
                int diffMin=getScrollX()-minScorll;
                int diffMax=maxScroll-getScrollX();
                if(dx>diffMin&&dx>0){
                    dx=diffMin;
                }else if(Math.abs(dx)>diffMax&&dx<0){
                    dx=diffMax;
                }
                scrollBy((int) -dx, 0);

                mLastX = x;

                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float xvel = VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                        mPointActivtyId);
                Log.i(TAG, "onTouchEvent: " + xvel);

                if(Math.abs(xvel)>ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity()) {
                    mFlingScroller.fling(
                            getScrollX(), getScrollY(),
                            -(int) xvel, 0,//数据设为计算出的速度的相反值
                            minScorll, maxScroll,
                            0, 0);
                }
                startScroll();
                mPointActivtyId = -1;
                mLastX = -1;


                break;
            case MotionEvent.ACTION_CANCEL:
                mPointActivtyId = -1;
                mLastX = -1;
                mVelocityTracker.recycle();
                break;
        }
        return true;
    }

    private void startScroll() {
        currentScroll = getScrollX();
        Log.i(TAG, "startScroll: currentScroll:" + currentScroll);
        postDelayed(scrollCheckTask, 30);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mFlingScroller.computeScrollOffset()) {
            int currX = mFlingScroller.getCurrX();
            Log.i(TAG, "computeScroll: " + currX);
            scrollTo(currX, mFlingScroller.getCurrY());

            invalidate();
            Log.i(TAG, "computeScroll: 重绘");
        }else if(mAjustScroll.computeScrollOffset()){
            int currX = mAjustScroll.getCurrX();
            int currY = mAjustScroll.getCurrY();
            scrollTo(currX, currY);
            invalidate();
            Log.i(TAG, "mAjustScroll computeScroll currX :"+currX);
        }
    }

    /**
     * 设置最小的偏移量
     *
     * @param minScorll
     */
    public void setMinScorll(int minScorll) {
        this.minScorll = minScorll;
    }

    /**
     * 设置最大的偏移量
     *
     * @param maxScroll
     */
    public void setMaxScroll(int maxScroll) {
        this.maxScroll = maxScroll;
    }

    /**
     * 设置每次滑动必须为oneStep的整数倍
     *
     * @param oneSetp
     */
    public void setOneSetp(int oneSetp) {
        this.oneSetp = oneSetp;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


    }

    /**
     * 停止滑动后对偏移量进行调整，使偏移量为oneStep的整数倍
     */
    Runnable scrollCheckTask = new Runnable() {
        @Override
        public void run() {
            int newScroll = getScrollX();
            Log.i(TAG, "startScroll: newScroll:" + newScroll);
            if (currentScroll == newScroll) {
                int more = getScrollX() % oneSetp;
                if (more < oneSetp / 2) {
                    mAjustScroll.startScroll(getScrollX(),getScrollY(),-more,0);
                    //scrollBy(-more, 0);
                } else {
                   // scrollBy(oneSetp - more, 0);
                    mAjustScroll.startScroll(getScrollX(),getScrollY(),oneSetp - more,0);
                }
                invalidate();
                Log.i(TAG, "run: 滚动结束" + more);
            } else {
                startScroll();
                Log.i(TAG, "run: 滚动未结束");
            }
        }
    };


}
