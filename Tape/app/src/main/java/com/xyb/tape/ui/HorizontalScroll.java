package com.xyb.tape.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
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
 * 该控件类似一个水平滑动的ScrollView
 * 但是具有以下两个扩展功能
 * leftMaxScorll，rightMaxScroll指定内容左右最大的偏移量。
 * oneStep 每次内容滑动完成后，自动偏移到oneSetp的整数倍。
 */

public class HorizontalScroll extends FrameLayout {
    private static final String TAG = "MyScrollView";
    private int mPointActivtyId = -1;
    private int mTouchSlop = -1;//可判断手势为移动的距离的最小距离
    private int mMinimumFling;//可判定为滑动的最小速度
    private int mMaximumFling;//建议的最大的滑动速度
    private float mLastX = -1;
    private Scroller mFlingScroller;

    private VelocityTracker mVelocityTracker;
    protected int leftMaxScorll, rightMaxScroll;
    protected int oneStep;//滑动的最小步长
    private int currentScroll;


    public HorizontalScroll(Context context) {
        this(context, null);
    }

    public HorizontalScroll(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScroll(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFlingScroller = new Scroller(getContext());

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMinimumFling = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        mMaximumFling = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (!mFlingScroller.isFinished()) {
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
                float scrollDx = -dx;

                scrollBy((int) scrollDx, 0);

                mLastX = x;

                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFling);
                float xvel = VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                        mPointActivtyId);
                Log.i(TAG, "onTouchEvent: " + xvel);

                if (Math.abs(xvel) > mMinimumFling) {
                    mFlingScroller.fling(
                            getScrollX(), getScrollY(),
                            -(int) xvel, 0,//数据设为计算出的速度的相反值
                            leftMaxScorll, rightMaxScroll,
                            0, 0);
                    invalidate();
                } else {
                    ajustScrollX();
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


    //重写滑动方法，设置到边界的时候不滑。
    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (x < leftMaxScorll)
        {
            x = leftMaxScorll;
        }
        if (x > rightMaxScroll)
        {
            x = rightMaxScroll;
        }
        if (x != getScrollX())
        {
            super.scrollTo(x, y);
        }

    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mFlingScroller.computeScrollOffset()) {
            int currX = mFlingScroller.getCurrX();
            Log.i(TAG, "computeScroll: " + currX);
            scrollTo(currX, mFlingScroller.getCurrY());

            if (!mFlingScroller.computeScrollOffset()) {
                ajustScrollX();
            }
            invalidate();
            Log.i(TAG, "computeScroll: 重绘");
        }


    }

    /**
     * 调整偏移量，是scrollx为oneStep的整数倍。
     * 供滑动完成后调用
     */
    private void ajustScrollX() {
        int more = getScrollX() % oneStep;
        int remain = oneStep * (getNumberSign(currentScroll)) - more;
        if (Math.abs(more) > oneStep / 2) {
            mFlingScroller.startScroll(getScrollX(), getScrollY(), remain, getScrollY());
            Log.i(TAG, "run: scroll 调整" + remain);
        } else {
            mFlingScroller.startScroll(getScrollX(), getScrollY(), -more, getScrollY());
            Log.i(TAG, "run: scroll 调整" + -more);

        }
        invalidate();
    }

    /**
     * 设置最小的偏移量
     *
     * @param leftMaxScorll
     */
    public void setLeftMaxScorll(int leftMaxScorll) {
        this.leftMaxScorll = leftMaxScorll;
    }

    /**
     * 设置最大的偏移量
     *
     * @param rightMaxScroll
     */
    public void setRightMaxScroll(int rightMaxScroll) {
        this.rightMaxScroll = rightMaxScroll;
    }

    /**
     * 设置每次滑动必须为oneStep的整数倍
     *
     * @param oneStep
     */
    public void setOneStep(int oneStep) {
        this.oneStep = oneStep;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


    }




    private int getNumberSign(int number){
        if(number==0){return  1;}
        return number / Math.abs(number);
    }

}
