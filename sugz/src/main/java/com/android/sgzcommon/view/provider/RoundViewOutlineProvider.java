package com.android.sgzcommon.view.provider;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RoundViewOutlineProvider extends ViewOutlineProvider {

    private float mRadius;//圆角弧度

    public RoundViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        view.measure(0,0);
        Log.d("RoundViewOutlineProvider", "getOutline: width == " + view.getWidth() + "; height == " + view.getHeight());
        Log.d("RoundViewOutlineProvider", "getOutline: width2 == " + view.getMeasuredWidth() + "; height2 == " + view.getMeasuredHeight());
        Log.d("RoundViewOutlineProvider", "getOutline: radius == " + mRadius);
        Rect selfRect = new Rect(0, 0, view.getWidth(), view.getHeight());// 绘制区域
        outline.setRoundRect(selfRect, mRadius);
    }
}