package com.android.sgzcommon.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * 用于通知，如：常见的右上角通知数目
 */
public class CircleTagView extends View {

    private float mTextSize;
    private boolean mVisible;
    private String mText;
    private Rect mBounds;
    private Paint mTextPaint;
    private Paint mBandingPaint;
    private Paint mBackgroundPaint;
    private static final float BANDING_WIDTH = 2.5f;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_BANDING_COLOR = Color.GREEN;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.RED;
    private static final float DEFAULT_TEXT_SIZE = 19;

    public CircleTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTextSize = DEFAULT_TEXT_SIZE;
        mVisible = true;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(DEFAULT_TEXT_COLOR);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStrokeWidth(BANDING_WIDTH * 4);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(DEFAULT_BACKGROUND_COLOR);
        mBackgroundPaint.setAntiAlias(true);
        mBounds = new Rect();
        mBandingPaint = new Paint();
        mBandingPaint.setColor(DEFAULT_BANDING_COLOR);
        mBandingPaint.setStrokeWidth(BANDING_WIDTH);
        mBandingPaint.setStyle(Paint.Style.STROKE);
        mBandingPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            float textWidth = mBounds.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            float textHeight = mBounds.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }
        int maxValue = (int) ((width >= height ? width : height) + mTextSize);
        setMeasuredDimension(maxValue, maxValue);
    }

    /**
     * 设置显示的文本信息
     *
     * @param text 文本信息
     */
    public void setText(String text) {
        mText = text;
        mTextPaint.getTextBounds(mText, 0, mText.length(), mBounds);
        invalidate();
    }

    /**
     * 设置标签是否可见
     *
     * @param visible 是否可见
     */
    public void setVisibility(boolean visible) {
        mVisible = visible;
        invalidate();
    }

    /**
     * 获取标签文本信息
     *
     * @return
     */
    public String getText() {
        return mText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mVisible && !TextUtils.isEmpty(mText) && !"0".equals(mText)) {
            float centerX = getMeasuredWidth() / 2.0f;
            float centerY = getMeasuredHeight() / 2.0f;
            float r = (centerX <= centerY ? centerY : centerX);
            float baseline = centerY - mBounds.exactCenterY();
            canvas.drawCircle(centerX, centerY, r - BANDING_WIDTH, mBackgroundPaint);
            canvas.drawCircle(centerX, centerY, r - BANDING_WIDTH, mBandingPaint);
            canvas.drawText(mText, centerX - mBounds.exactCenterX(), baseline, mTextPaint);
            requestLayout();
        }
    }
}
