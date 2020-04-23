package com.android.sgzcommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by sgz on 2016/11/21.
 */

public class MyListView extends ListView {

    private static final String TAG = "MyListView";

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //    @Override
    //    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //        int expandSpec = MeasureSpec.makeMeasureSpec(  //这个makeMeasureSpec方法表示就是针对明确的测量模式，来得到相应的尺寸
    //                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); //第一个参数就是传入的尺寸size，表示Integer表示最大空间值进行2位移位运算，第二参数指定为AT_MOST表示是"warp_content"的测量模式
    //        super.onMeasure(widthMeasureSpec, expandSpec);//指定了我们明确的测量尺寸
    //    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onTouchEvent=" + ev.getAction());
        return super.onTouchEvent(ev);
    }
}
