package com.android.sgzcommon.recycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class InnerRecyclerView extends RecyclerView {
    private int mStartX = 0;
    private int mStartY = 0;
    private int mTouchSlop = 0;
    private int mScrollPointerId = -1;

    public InnerRecyclerView(@NonNull Context context) {
        super(context);
    }

    public InnerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        final int actionIndex = ev.getActionIndex();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollPointerId = ev.getPointerId(0);

                mStartX = (int) (ev.getX() + 0.5f);
                mStartY = (int) (ev.getY() + 0.5f);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mScrollPointerId = ev.getPointerId(actionIndex);    //新落下的手指为滑动监听手指
                mStartX = (int) (ev.getX(actionIndex) + 0.5f);
                mStartY = (int) (ev.getY(actionIndex) + 0.5f);
                break;
            case MotionEvent.ACTION_MOVE:
                final int index = ev.findPointerIndex(mScrollPointerId);

                final int x = (int) (ev.getX(index) + 0.5f);
                final int y = (int) (ev.getY(index) + 0.5f);
                float distanceX = Math.abs(x - mStartX);
                float distanceY = Math.abs(y - mStartY);
                if (distanceX > mTouchSlop && distanceX > distanceY) { //当横向滑动距离大于纵向滑动距离则不拦截事件
                    return false;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void onPointerUp(MotionEvent e) {
        final int actionIndex = e.getActionIndex();
        if (e.getPointerId(actionIndex) == mScrollPointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mScrollPointerId = e.getPointerId(newIndex);
            mStartX = (int) (e.getX(newIndex) + 0.5f);
            mStartY = (int) (e.getY(newIndex) + 0.5f);
        }
    }
}
