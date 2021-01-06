package com.android.sgzcommon.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.android.sugz.R;

public class CircleProgressBar extends View {
    private int mCenterX;
    private int mCenterY;
    private int mMinRadius;
    private float mRingWidth; //圆环的宽度
    private int mRingColor;
    private int mCircleInColor; //最里面圆的颜色
    private int mRingBackgroundColor;    //圆环背景颜色
    private int mValue;
    private int mMaxValue;
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
        mRingWidth = a.getDimension(R.styleable.CircleProgressBar_ring_width, 40f);
        mCircleInColor = a.getColor(R.styleable.CircleProgressBar_circle_in_color, context.getResources().getColor(R.color.transparent));
        mRingColor = a.getColor(R.styleable.CircleProgressBar_ring_color, context.getResources().getColor(R.color.amber_500));
        mRingBackgroundColor = a.getColor(R.styleable.CircleProgressBar_ring_background_color, context.getResources().getColor(R.color.grey_300));
        int value = a.getInt(R.styleable.CircleProgressBar_value, 0);
        mMaxValue = a.getInt(R.styleable.CircleProgressBar_max_value, 100);
        mProgressTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_text_size, mMinRadius / 2);
        mProgressTextColor = a.getColor(R.styleable.CircleProgressBar_text_color, mRingColor);
        a.recycle();
        //需要重写onDraw就得调用此
        this.setWillNotDraw(false);
        setValue(value);
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
        mMinRadius = (int) (Math.min(width, height) / 2 - mRingWidth);
        mCenterX = width / 2;
        mCenterY = height / 2;
        mRectF = new RectF(mCenterX - mMinRadius - mRingWidth / 2, mCenterY - mMinRadius - mRingWidth / 2, mCenterX + mMinRadius + mRingWidth / 2, mCenterY + mMinRadius + mRingWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(mCircleInColor);
        drawValueText(canvas);
        canvas.drawCircle(mCenterX, mCenterY, mMinRadius, paint);
        //画默认圆环
        drawNormalRing(canvas, paint);
        //画彩色圆环
        drawColorRing(canvas, paint);
    }

    /**
     * @param canvas
     */
    private void drawValueText(Canvas canvas) {
        double s = mMinRadius / Math.sqrt(2);
        Rect rect = new Rect((int) (mCenterX - s), (int) (mCenterY - s), (int) (mCenterX + s), (int) (mCenterY + s));
        Paint textPaint = new Paint();
        textPaint.setColor(mProgressTextColor);
        textPaint.setTextSize(mProgressTextSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);//该方法即为设置基线上那个点到底是left,center,还是right  这里我设置为center
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        canvas.drawText(mValue + "%", rect.centerX(), baseLineY, textPaint);
    }

    /**
     * 画默认圆环
     *
     * @param canvas
     */
    private void drawNormalRing(Canvas canvas, Paint paint) {
        Paint ringNormalPaint = new Paint(paint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(mRingWidth);
        ringNormalPaint.setColor(mRingBackgroundColor);
        canvas.drawArc(mRectF, 360, 360, false, ringNormalPaint);
    }

    /**
     * 画彩色圆环
     *
     * @param canvas
     */
    private void drawColorRing(Canvas canvas, Paint paint) {
        Paint ringColorPaint = new Paint(paint);
        ringColorPaint.setStyle(Paint.Style.STROKE);
        ringColorPaint.setStrokeCap(Paint.Cap.ROUND);
        ringColorPaint.setStrokeWidth(mRingWidth);
        ringColorPaint.setColor(mRingColor);
        ringColorPaint.setShader(new SweepGradient(mCenterX, mCenterX, mRingColor, mRingColor));
        //逆时针旋转90度
        canvas.rotate(-90, mCenterX, mCenterY);
        int sweep = (int) (360 * (mValue / 100f));
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
        return mValue;
    }

    /**
     * 设置当前值
     *
     * @param value
     */
    public void setValue(int value) {
        if (value > mMaxValue) {
            value = mMaxValue;
        }
        mValue = value;
        startAnimator();
    }

    /**
     *
     */
    private void startAnimator() {
        valueAnimator = ValueAnimator.ofInt(0, mValue);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = Integer.parseInt(String.valueOf(animation.getAnimatedValue()));
                invalidate();
            }
        });
        valueAnimator.start();
    }
}