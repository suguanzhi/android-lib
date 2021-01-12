package com.android.sgzcommon.recycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView嵌套RecyclerView，都是垂直方向滑动，解决冲突
 */

public class InnerVerticalRecyclerView extends RecyclerView {

    public InnerVerticalRecyclerView(@NonNull Context context) {
        super(context);
    }

    public InnerVerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_CANCEL == ev.getAction()) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}