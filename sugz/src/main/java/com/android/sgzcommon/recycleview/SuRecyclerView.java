package com.android.sgzcommon.recycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView嵌套RecyclerView，都是垂直方向滑动，禁止内部RecyclerView滑动，解决冲突。
 */

public class SuRecyclerView extends RecyclerView {

    public SuRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
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