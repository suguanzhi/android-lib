package com.android.sgzcommon.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * ViewPger底下的标签圆点
 */
public class ViewPagerTagView extends LinearLayout {

    private int mNumber;
    private int mMaxWidth;
    private int mMaxHeight;
    private int mSelectedPosition;
    private float mVerticalSpace;
    private float mHorizontalSpace;
    private float mRadius;
    private Paint mSelectedPaint;
    private Paint mUnselectedPaint;
    private Paint mBackgroundPaint;
    private static final int DEFAULT_RADIUS = 5;
    private static final int DEFAULT_H_SPACE = 3;
    private static final int DEFAULT_V_SPACE = 2;
    private static final int DEFAULT_SELECTED_COLOR = Color.WHITE;
    private static final int DEFAULT_UNSELECTED_COLOR = Color.RED;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.GRAY;

    public ViewPagerTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ViewPagerTagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        mRadius = DEFAULT_RADIUS;
        mVerticalSpace = DEFAULT_V_SPACE;
        mHorizontalSpace = DEFAULT_H_SPACE;
        mSelectedPaint = new Paint();
        mSelectedPaint.setColor(DEFAULT_SELECTED_COLOR);
        mSelectedPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mUnselectedPaint = new Paint();
        mUnselectedPaint.setColor(DEFAULT_UNSELECTED_COLOR);
        mUnselectedPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(DEFAULT_BACKGROUND_COLOR);
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mMaxHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (!(heightMode == MeasureSpec.EXACTLY)) {
            mMaxHeight = (int) (2 * (mRadius + mVerticalSpace));
        }
        setMeasuredDimension(mMaxWidth, mMaxHeight);
    }

    public void setNumbers(int numbers) {
        mNumber = numbers;
        mMaxWidth = (int) ((mRadius * 2) * mNumber + mHorizontalSpace * (mNumber + 1));
        measure(0, 0);
        invalidate();
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        RectF rect = new RectF(0f, 0f, (float) mMaxWidth, (float) mMaxHeight);
        canvas.drawRoundRect(rect, (float) mMaxHeight / 2, (float) mMaxHeight / 2, mBackgroundPaint);
        if (mNumber > 0 && mSelectedPosition < mNumber) {
            Point[] unSelectedCenters = new Point[mNumber];
            int stepWidth = (int) (mHorizontalSpace + mRadius);
            for (int i = 0; i < mNumber; i++) {
                unSelectedCenters[i] = new Point();
                unSelectedCenters[i].x = stepWidth * (i + 1) + (int) (i * mRadius);
                unSelectedCenters[i].y = mMaxHeight / 2;
                canvas.drawCircle(unSelectedCenters[i].x, unSelectedCenters[i].y, mRadius, mUnselectedPaint);
            }
            canvas.drawCircle((float) unSelectedCenters[mSelectedPosition].x, (float) unSelectedCenters[mSelectedPosition].y, mRadius, mSelectedPaint);
        }
        super.onDraw(canvas);
    }
}
