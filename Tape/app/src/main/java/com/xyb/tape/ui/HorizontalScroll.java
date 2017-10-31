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
 * 该控件类似一个水平滑动的ScrollView
 * 但是具有以下两个扩展功能
 * leftMaxScorll，rightMaxScroll指定内容左右最大的偏移量。
 * oneStep 每次内容滑动完成后，自动偏移到oneSetp的整数倍。
 *
 */

public class HorizontalScroll extends FrameLayout {
    private static final String TAG = "MyScrollView";
    private int mPointActivtyId = -1;
    private int mTouchSlop = -1;//用来判断手势移动的距离是否达到滑动的标准
    private float mLastX = -1;
    private Scroller mFlingScroller;
    private VelocityTracker mVelocityTracker;

    protected int leftMaxScorll, rightMaxScroll;
    protected int oneStep;//滑动的最小步长
    private int currentScroll;
    private Scroller mAjustScroll;


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
        mAjustScroll=new Scroller(getContext());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }



        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(!mAjustScroll.isFinished()){
                    mAjustScroll.abortAnimation();
                }
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
                float scrollDx = -dx;

                int curScrollX = getScrollX();
                int leftDiffX = leftMaxScorll - curScrollX;
                int righDiffX = rightMaxScroll - curScrollX;
                if (scrollDx < leftDiffX) {//向右滑动
                    scrollDx = leftDiffX;
                } else if (scrollDx > righDiffX) {//向左滑动
                    scrollDx = righDiffX;
                }

                scrollBy((int) scrollDx, 0);

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
                            leftMaxScorll, rightMaxScroll,
                            0, 0);
                }
                startCheckScroll();
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

    /**
     * 检察Scroll是否停止
     */
    private void startCheckScroll() {
        currentScroll = getScrollX();
        Log.i(TAG, "startCheckScroll: currentScroll:" + currentScroll);
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
        }

        if(mAjustScroll.computeScrollOffset()){
            mFlingScroller.abortAnimation();
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

    /**
     * 停止滑动后对偏移量进行调整，使偏移量为oneStep的整数倍
     */
    Runnable scrollCheckTask = new Runnable() {
        @Override
        public void run() {
            int newScroll = getScrollX();
            Log.i(TAG, "startCheckScroll: newScroll:" + newScroll);
            if (currentScroll == newScroll) {
                int more = currentScroll % oneStep;
                int remain= oneStep * (getNumberSign(currentScroll))-more;
                if (Math.abs(more) > oneStep / 2) {
                    scrollBy(remain, 0);//
                    Log.i(TAG, "run: scroll 调整" + remain);
                } else {
                    scrollBy(-more, 0);
                    Log.i(TAG, "run: scroll 调整" + -more);

                }
                invalidate();
            } else {
                startCheckScroll();
                Log.i(TAG, "run: 滚动未结束");
            }
        }
    };


    private int getNumberSign(int number){
           if(number==0){return  1;}
        return number / Math.abs(number);
    }

}
