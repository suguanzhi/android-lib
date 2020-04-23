package com.android.sgzcommon.view.scrollviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 无限循环ViewPager
 *
 * @author sgz
 */
public class AutoScrollViewPager extends ViewPager {

    private int mSize;
    private int mDuration;
    private int mCurrentPosition;
    /**
     * 与mCurrentPosition不等，mCurrentRealPosition包括了前后过渡的Item
     */
    private int mCurrentRealPosition;
    private float mUpX;
    private float mDownX;
    private float mUpY;
    private float mDownY;
    private float mCurrentXOffset;
    private boolean isFirst;
    private boolean isTouching;
    private Context mContext;
    private MyHandler mHandler;
    private OnViewPageClickListener mPageClickListener;
    private OnViewPageChangeListener mPageChangeListener;
    private CustomDurationScroller mScroller;
    private static final int SCROLL = 1;
    private static final int SCROLL_DURATION = 1000;
    private static final int SHOW_DURATION = 10;

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoScrollViewPager(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        isFirst = true;
        mContext = context;
        mDuration = SHOW_DURATION;
        mHandler = new MyHandler(AutoScrollViewPager.this);
        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!isTouching) {
                    mHandler.obtainMessage(SCROLL).sendToTarget();
                }
            }
        }, 0, mDuration * 1000);
        setViewPagerScroller();
    }

    public void setShowDuration(int duration) {
        mDuration = duration;
    }

    public void setPagerAdapter(AutoScrollViewPagerAdapter adapter) {
        super.setAdapter(adapter);
        setStartItem();
        adapter.notifyDataSetChanged();
    }

    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);
            mScroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
            // 设置两页面之间切换过程时间
            mScroller.setDuration(SCROLL_DURATION);
            scrollerField.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStartItem() {
        try {
            Field currItemField = ViewPager.class.getDeclaredField("mCurItem");
            currItemField.setAccessible(true);
            currItemField.set(this, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onPageScrolled(int arg0, float arg1, int arg2) {
        PagerAdapter adapter = getAdapter();
        mCurrentXOffset = arg1;
        if (adapter != null) {
            mSize = adapter.getCount();
        }
        mCurrentRealPosition = arg0;
        if (arg1 == 0f) {
            if (mSize > 0) {
                mCurrentPosition = mCurrentRealPosition;
                if (mCurrentRealPosition >= mSize - 1 || mCurrentRealPosition == 1) {
                    mCurrentPosition = 0;
                } else if (mCurrentRealPosition == 0) {
                    mCurrentPosition = mSize - 1;
                } else {
                    mCurrentPosition = mCurrentRealPosition - 1;
                }
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageSelected(mCurrentPosition);
                }
            }
            if (mSize >= 2) {
                mScroller.setDuration(SCROLL_DURATION);
                if (mCurrentRealPosition == (mSize - 1)) {
                    mCurrentRealPosition = 1;
                } else if (mCurrentRealPosition == 0) {
                    mCurrentRealPosition = mSize - 2;
                }
                setCurrentItem(mCurrentRealPosition, false);
            }
        }
        super.onPageScrolled(arg0, arg1, arg2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                isTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                isTouching = true;
                break;
            case MotionEvent.ACTION_UP:
                mUpX = event.getX();
                mUpY = event.getY();
                if (isTouching) {
                    if (mDownX == mUpX && mDownY == mUpY && scrollComplete()) {
                        if (mPageClickListener != null) {
                            mPageClickListener.onPageClick(mCurrentPosition);
                        }
                    }
                }
                isTouching = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当page滑动到xoffset == 0时，滑动完成。
     *
     * @return
     */
    private boolean scrollComplete() {
        return mCurrentXOffset == 0f || mCurrentXOffset < 0.1f || mCurrentXOffset > 0.9f;
    }

    private void scroll() {
        if (mSize > 2) {
            setCurrentItem(mCurrentRealPosition + 1, true);
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<AutoScrollViewPager> weakReference;

        public MyHandler(AutoScrollViewPager myViewPager) {
            weakReference = new WeakReference<AutoScrollViewPager>(myViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            AutoScrollViewPager viewPager = weakReference.get();
            removeCallbacksAndMessages(null);
            switch (msg.what) {
                case SCROLL:
                    if (viewPager != null) {
                        viewPager.scroll();
                    }
                    break;
            }
        }
    }

    public void setOnViewPageChangeListener(OnViewPageChangeListener listener) {
        mPageChangeListener = listener;
    }

    public void setOnViewPageClickListener(OnViewPageClickListener listener) {
        mPageClickListener = listener;
    }

    public interface OnViewPageChangeListener {
        void onPageSelected(int position);
    }

    public interface OnViewPageClickListener {
        void onPageClick(int position);
    }

    class CustomDurationScroller extends Scroller {

        private int mDuration;
        private double scrollFactor = 1;

        public CustomDurationScroller(Context context) {
            super(context);
        }

        public CustomDurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        /**
         * Set the factor by which the duration will change
         */
        public void setScrollDurationFactor(double scrollFactor) {
            this.scrollFactor = scrollFactor;
        }

        /**
         * 设置两页面之间的移动切换时间
         *
         * @param duration
         */
        public void setDuration(int duration) {
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (mDuration * scrollFactor));
        }
    }

}
