package com.android.sgzcommon.view.provider;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RoundViewOutlineProvider extends ViewOutlineProvider {

    private float mRadius;//圆角弧度

    public RoundViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        int vWidth = view.getWidth();
        int vHeight = view.getHeight();
        int left = 0;
        int top = 0;
        int width = vWidth;
        int height = vHeight;
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                int iWidth = drawable.getIntrinsicWidth();
                int iHeight = drawable.getIntrinsicHeight();
                float wr = iWidth * 1f / vWidth;
                float hr = iHeight * 1f / vHeight;
                float ir = iWidth * 1f / iHeight;
                ImageView.ScaleType scaleType = imageView.getScaleType();
                if (ImageView.ScaleType.FIT_START == scaleType || ImageView.ScaleType.FIT_CENTER == scaleType || ImageView.ScaleType.FIT_END == scaleType || ImageView.ScaleType.FIT_XY == scaleType || ImageView.ScaleType.CENTER_CROP == scaleType) {
                    if (wr > hr) {
                        height = (int) (width / ir);
                    } else if (hr > wr) {
                        width = (int) (height * ir);
                    }
                } else {
                    if (wr > hr) {
                        width = Math.min(width, vWidth);
                        height = (int) (width / ir);
                    } else if (hr > wr) {
                        height = Math.min(height, vHeight);
                        width = (int) (height * ir);
                    }
                }
                if (ImageView.ScaleType.CENTER == scaleType || ImageView.ScaleType.CENTER_INSIDE == scaleType || ImageView.ScaleType.FIT_CENTER == scaleType) {
                    left = (vWidth - width) / 2;
                    top = (vHeight - height) / 2;
                } else if (ImageView.ScaleType.FIT_END == scaleType) {
                    top = Math.max(0, vHeight - height);
                    left = Math.max(0, vWidth - width);
                } else {
                    top = 0;
                    left = 0;
                }
            }
        }
        Rect selfRect = new Rect(left, top, left + width, top + height);// 绘制区域
        outline.setRoundRect(selfRect, mRadius);
    }
}