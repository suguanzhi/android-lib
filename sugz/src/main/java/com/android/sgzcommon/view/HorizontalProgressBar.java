package com.android.sgzcommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.utils.UnitUtils;
import com.android.sugz.R;

import androidx.annotation.Nullable;

/**
 * @author sgz
 * @date 2021-01-14
 */
public class HorizontalProgressBar extends View {

    private int mWidth;
    private int mHeight;
    private int mLeft;
    private int mRight;
    private int mBorderWidth;
    private int mBackgroundColor;
    private int mProgressColor;
    private Paint mProgressTextPaint;
    private Paint mBackgroundTextPaint;

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar);
        mBackgroundColor = array.getColor(R.styleable.HorizontalProgressBar_background_color, getResources().getColor(R.color.grey_300));
        mBorderWidth = array.getDimensionPixelOffset(R.styleable.HorizontalProgressBar_border_width, UnitUtils.dp2px(10));
        int textSize = array.getDimensionPixelOffset(R.styleable.HorizontalProgressBar_text_size, UnitUtils.sp2px(14));
        int backgroundTextSize = array.getDimensionPixelOffset(R.styleable.HorizontalProgressBar_text_size, UnitUtils.sp2px(14));
        mProgressTextPaint.setTextSize(textSize);
        mBackgroundTextPaint.setTextSize(backgroundTextSize);

        array.recycle();
        setWillNotDraw(false);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        double th = getProgressTextHeight() + mBorderWidth + getBackgroundTextHeight() + getPaddingTop() + getPaddingBottom();
        double maxHeight = Math.max(mHeight, th);
        mHeight = (int) Math.ceil(maxHeight);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLeft = left;
        mRight = right;
        Log.d("HorizontalProgressBar", "onLayout: left == " + left + ";top == " + top + "; right == " + right + ";bottom == " + bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        Log.d("HorizontalProgressBar", "onDraw: ");
    }

    private void drawBackground(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mBorderWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(mBackgroundColor);
        float borderRadius = mBorderWidth * 1f / 2;
        Log.d("HorizontalProgressBar", "drawBackground: x == " + getX() + ";width == " + mWidth);
        canvas.drawLine(borderRadius, getProgressTextY(), mWidth - borderRadius, getProgressTextY(), paint);
    }

    private void drawBackgroundText(Canvas canvas) {

    }

    private void drawProgress(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(mProgressColor);

    }

    private void drawProgressText(Canvas canvas) {
        Rect rect = new Rect();
    }

    /**
     *
     * @return
     */
    private float getProgressTextY() {
        double dy = getProgressTextHeight() + getPaddingTop() + mBorderWidth / 2;
        double y = Math.max(dy, mHeight / 2);
        return (float) y;
    }

    /**
     * @return
     */
    private double getProgressTextHeight() {
        Paint.FontMetrics fm = mProgressTextPaint.getFontMetrics();
        return Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * @return
     */
    private double getBackgroundTextHeight() {
        Paint.FontMetrics fm = mBackgroundTextPaint.getFontMetrics();
        return Math.ceil(fm.descent - fm.ascent);
    }
}
