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
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    protected int minScorll, maxScroll;
    protected int oneSetp;//滑动的最小步长
    private int currentScroll;


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
        mScroller = new Scroller(getContext());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchSlop < 0) {
            //getScaledTouchSlop()，一个距离常量，用来判断用户的行为是否是滑动。手势滑动的距离大于这个值
            //就认为是滑动。
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }


        if (mScroller == null) {
            mScroller = new Scroller(getContext());
        }
        //int action = MotionEventCompat.getActionMasked(event);
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
                scrollBy((int) -dx,0 );
                mLastX = x;

                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float xvel = VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                        mPointActivtyId);
                Log.i(TAG, "onTouchEvent: " + xvel);
                mScroller.fling(
                        getScrollX(), getScrollY(),
                        -(int) xvel, 0,//数据设为计算出的速度的相反值
                        minScorll, maxScroll,
                        0, 0);



                mPointActivtyId = -1;
                mLastX = -1;

                currentScroll=getScrollY();
                postDelayed(scrollCheckTask, 300);
                break;
            case MotionEvent.ACTION_CANCEL:
                mPointActivtyId = -1;
                mLastX = -1;
                mVelocityTracker.recycle();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            Log.i(TAG, "computeScroll: " + currX);
            scrollTo(currX, mScroller.getCurrY());

            currentScroll=getScrollY();
            postDelayed(scrollCheckTask, 300);
            invalidate();
            Log.i(TAG, "computeScroll: 重绘");
        }
    }

    public void setMinScorll(int minScorll) {
        this.minScorll = minScorll;
    }

    public void setMaxScroll(int maxScroll) {
        this.maxScroll = maxScroll;
    }

    public void setOneSetp(int oneSetp) {
        this.oneSetp = oneSetp;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


    }

    Runnable scrollCheckTask = new Runnable() {
        @Override
        public void run() {
            int newScroll = getScrollY();
            if(currentScroll==newScroll){
                int more=getScrollX()%oneSetp;
                if(more<oneSetp/2) {
                    scrollBy(-more, 0);
                }else{
                    scrollBy(oneSetp-more, 0);
                }
                Log.i(TAG, "run: 滚动结束"+more);
            }
        }
    };


}
