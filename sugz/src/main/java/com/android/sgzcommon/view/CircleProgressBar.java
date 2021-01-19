package com.android.sgzcommon.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.android.sgzcommon.utils.UnitUtils;
import com.android.sugz.R;

public class CircleProgressBar extends View {
    private int mCenterX;
    private int mCenterY;
    private int mMinRadius;
    private float mBorderWidth; //圆环的宽度
    private int mProgressColor;
    private int mBackgroundInColor; //最里面圆的颜色
    private int mBackgroundColor;    //圆环背景颜色
    private int mMaxValue;
    private int mCurrentValue;
    private int mProgressTextSize;
    private int mProgressTextColor;
    private RectF mRectF;
    private ValueAnimator valueAnimator;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mBorderWidth = a.getDimension(R.styleable.CircleProgressBar_border_width, UnitUtils.dp2px(10));
        mBackgroundInColor = a.getColor(R.styleable.CircleProgressBar_background_in_color, context.getResources().getColor(R.color.transparent));
        mProgressColor = a.getColor(R.styleable.CircleProgressBar_progress_color, context.getResources().getColor(R.color.amber_500));
        mBackgroundColor = a.getColor(R.styleable.CircleProgressBar_background_color, context.getResources().getColor(R.color.grey_300));
        final int value = a.getInt(R.styleable.CircleProgressBar_value, 0);
        mMaxValue = a.getInt(R.styleable.CircleProgressBar_max_value, 100);
        mProgressTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_text_size, UnitUtils.sp2px(14));
        mProgressTextColor = a.getColor(R.styleable.CircleProgressBar_text_color, mProgressColor);
        a.recycle();
        setValue(value, true);
        //非ViewGroup的子类，实现onDraw方法需要调用此方法。
        this.setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mMinRadius = (int) (Math.min(width, height) / 2 - mBorderWidth);
        mCenterX = width / 2;
        mCenterY = height / 2;
        mRectF = new RectF(mCenterX - mMinRadius - mBorderWidth / 2, mCenterY - mMinRadius - mBorderWidth / 2, mCenterX + mMinRadius + mBorderWidth / 2, mCenterY + mMinRadius + mBorderWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(mBackgroundInColor);
        drawValueText(canvas);
        canvas.drawCircle(mCenterX, mCenterY, mMinRadius, paint);
        drawBackground(canvas, paint);
        drawProgress(canvas, paint);
    }

    /**
     * @param canvas
     */
    private void drawValueText(Canvas canvas) {
        double s = mMinRadius / Math.sqrt(2);
        String valueText = mCurrentValue + "%";
        Rect rect = new Rect((int) (mCenterX - s), (int) (mCenterY - s), (int) (mCenterX + s), (int) (mCenterY + s));
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(mProgressTextColor);
        textPaint.setTextSize(mProgressTextSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);//该方法即为设置基线上那个点到底是left,center,还是right  这里我设置为center
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        canvas.drawText(valueText, rect.centerX(), baseLineY, textPaint);
    }

    /**
     * 画默认圆环
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas, Paint paint) {
        Paint ringNormalPaint = new Paint(paint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(mBorderWidth);
        ringNormalPaint.setColor(mBackgroundColor);
        canvas.drawArc(mRectF, 360, 360, false, ringNormalPaint);
    }

    /**
     * 画彩色圆环
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas, Paint paint) {
        Paint ringColorPaint = new Paint(paint);
        ringColorPaint.setStyle(Paint.Style.STROKE);
        ringColorPaint.setStrokeCap(Paint.Cap.ROUND);
        ringColorPaint.setStrokeWidth(mBorderWidth);
        ringColorPaint.setColor(mProgressColor);
        ringColorPaint.setShader(new SweepGradient(mCenterX, mCenterX, mProgressColor, mProgressColor));
        //逆时针旋转90度
        canvas.rotate(-90, mCenterX, mCenterY);
        int sweep = (int) (360 * (mCurrentValue / 100f));
        canvas.drawArc(mRectF, 360, sweep, false, ringColorPaint);
        ringColorPaint.setShader(null);
    }

    /**
     * @return
     */
    public int getMaxValue() {
        return mMaxValue;
    }

    /**
     * @return
     */
    public int getValue() {
        return mCurrentValue;
    }


    /**
     * 设置当前值，并开启动画
     *
     * @param value
     * @param reset 是否重置，从0开始
     */
    public void setValue(int value, boolean reset) {
        if (reset) {
            mCurrentValue = 0;
        }
        startAnimator(value);
    }

    /**
     *
     */
    private void startAnimator(int value) {
        if (value > mMaxValue) {
            value = mMaxValue;
        }
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofInt(mCurrentValue, value);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentValue = Integer.parseInt(String.valueOf(animation.getAnimatedValue()));
                invalidate();
            }
        });
        valueAnimator.start();
    }
}