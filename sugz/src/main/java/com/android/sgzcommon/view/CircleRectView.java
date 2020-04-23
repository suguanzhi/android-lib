package com.android.sgzcommon.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * Created by sgz on 2017/7/26.
 */

public class CircleRectView extends View {

    private int width;
    private int height;
    private float r;
    private Paint mPaint;
    private static final String TAG = "CircleRectView";

    public CircleRectView(Context context) {
        super(context);
        init(context);
    }

    public CircleRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        r = 10;
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#F0F0F0"));
//        mPaint.setColor(Color.RED);
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
        invalidate();
    }

    public void setColor(@ColorInt int color) {
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: width = " + width);
        Log.d(TAG, "onMeasure: height = " + height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(0f, 0f, (float) width, (float) height, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        Path p = new Path();
        p.moveTo(0, r);
        RectF rectF = new RectF(0f, 0, 2 * r, 2 * r);
        p.arcTo(rectF, 180, 90, false);
        p.lineTo(width - r, 0);
        rectF = new RectF(width - 2 * r, 0, width, 2 * r);
        p.arcTo(rectF, 270, 90, false);
        p.lineTo(width, height - r);
        rectF = new RectF(width - 2 * r, height - 2 * r, width, height);
        p.arcTo(rectF, 0, 90, false);
        p.lineTo(r, height);
        rectF = new RectF(0, height - 2 * r, 2 * r, height);
        p.arcTo(rectF, 90, 90, false);
        p.lineTo(0, r);
        canvas.drawPath(p, mPaint);
        mPaint.setXfermode(null);
    }
}
