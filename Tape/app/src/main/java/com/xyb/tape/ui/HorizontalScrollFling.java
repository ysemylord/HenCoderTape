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
 *
 */

public class HorizontalScrollFling extends FrameLayout {
    private static final String TAG = "MyScrollView";
    private int mPointActivtyId = -1;
    private int mTouchSlop = -1;//用来判断手势移动的距离是否达到滑动的标准
    private float mLastX = -1;
    private Scroller mFlingScroller;
    private VelocityTracker mVelocityTracker;

    private int leftMaxScroll =-1000;
    private int rightMaxScroll =1000;


    public HorizontalScrollFling(Context context) {
        this(context, null);
    }

    public HorizontalScrollFling(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    private void init() {
        if (mFlingScroller == null) {
            mFlingScroller = new Scroller(getContext());
        }
    }

    public HorizontalScrollFling(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }



        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if(!mFlingScroller.isFinished()){
                    mFlingScroller.abortAnimation();
                }
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
                float scrollDx=-dx;

                int curScrollX=getScrollX();
                int leftDiffX=leftMaxScroll-curScrollX;
                int righDiffX=rightMaxScroll-curScrollX;
                if(scrollDx<leftDiffX){//向右滑动
                   scrollDx=leftDiffX;
                }else if(scrollDx>righDiffX){//向左滑动
                   scrollDx=righDiffX;
                }

                scrollBy((int)scrollDx, 0);

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
                            leftMaxScroll, rightMaxScroll,
                            0, 0);
                }
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


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mFlingScroller.computeScrollOffset()) {
            int currX = mFlingScroller.getCurrX();
            Log.i(TAG, "computeScroll: " + currX);
            scrollTo(currX, mFlingScroller.getCurrY());

            invalidate();
            Log.i(TAG, "computeScroll: 重绘");
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
