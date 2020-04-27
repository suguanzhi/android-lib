package com.android.sgzcommon.view;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.sgzcommon.utils.UnitUtil;

@SuppressLint("NewApi")
public class ProgressTextView extends TextView {

	private int mWidth;
	private int mRadius;
	private int mCurrentX;
	private int mProgress;
	private int mCurrentProgress;
	private int mBackgroundColor;
	private long mLastSetTime;
	private long mCurrentSetTime;
	private boolean isReset;
	private Paint mCleanPaint;
	private Paint mProgressPaint;
	private ObjectAnimator mAnimator;
	private OnCompleteListener mListener;
	/**
	 * 按钮的圆角半径，单位dp
	 */
	private static final int RADIUS = 5;
	/**
	 * 进度条背景颜色
	 */
	private static final String BACKGROUND_COLOR = "#C5C5C5";
	/**
	 * 进度条进度的颜色
	 */
	private static final String PROGRESS_COLOR = "#25E330";

	public ProgressTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ProgressTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attr) {
		mRadius = UnitUtil.dp2px(RADIUS);
		mBackgroundColor = Color.parseColor(BACKGROUND_COLOR);
		int progressColor = Color.parseColor(PROGRESS_COLOR);
		drawBackground();
		mProgressPaint = new Paint();
		mProgressPaint.setAntiAlias(true);
		mProgressPaint.setColor(progressColor);
		mCleanPaint = new Paint();
		mCleanPaint.setAntiAlias(true);
		mCleanPaint.setColor(mBackgroundColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setBackgroundColor(int color) {
		mBackgroundColor = color;
		drawBackground();
	}

	public void resetBackgroundColor() {
		mBackgroundColor = Color.parseColor(BACKGROUND_COLOR);
		drawBackground();
	}

	/**
	 * 并非设置进度的函数，内部使用，设置进度使用{@link #setProgress(int)}
	 * 
	 * @param progress
	 */
	public void setCurrentProgress(int progress) {
		mCurrentProgress = progress;
		invalidate();
	}

	public void setProgressColor(int color) {
		mProgressPaint.setColor(color);
	}

	public void setProgress(int progress) {
		mCurrentSetTime = System.currentTimeMillis();
		if (mLastSetTime == 0) {
			mLastSetTime = mCurrentSetTime - 100;
		}
		long time = mCurrentSetTime - mLastSetTime;
		mLastSetTime = mCurrentSetTime;
		if (progress < mCurrentProgress) {
			resetProgress();
		}
		if (progress <= 100) {
			mProgress = progress;
		}
		final int stepProgress = Math.abs(progress - mCurrentProgress);
		if (mAnimator != null && mAnimator.isRunning()) {
			mAnimator.end();
		}
		if (stepProgress > 0) {
			mAnimator = ObjectAnimator.ofObject(this, "currentProgress", new MyTypeEvaluator(), mCurrentProgress, progress);
			mAnimator.setDuration(time);
			mAnimator.start();
		}
	}

	public void resetProgress() {
		isReset = true;
		mProgress = 0;
		mCurrentX = 0;
		mCurrentProgress = 0;
		invalidate();
	}

	public int getProgress() {
		return mProgress;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if (isReset) {
			isReset = false;
			RectF cleanF = new RectF(0, 0, mWidth, getHeight());
			canvas.drawRoundRect(cleanF, mRadius, mRadius, mCleanPaint);
		}
		mCurrentX = mCurrentProgress * mWidth / 100;
		if (mCurrentProgress > 100) {
			mCurrentProgress = 100;
		}
		RectF progressF = new RectF(0, 0, mCurrentX, getHeight());
		canvas.drawRoundRect(progressF, mRadius, mRadius, mProgressPaint);
		if (mCurrentProgress >= 100 && mListener != null) {
			mLastSetTime = 0;
			mListener.onComplete();
		}
		super.onDraw(canvas);
	}

	@SuppressLint("NewApi")
	private void drawBackground() {
		Shape shape = new Shape() {

			@Override
			public void draw(Canvas canvas, Paint paint) {
				RectF f = new RectF(0, 0, getWidth(), getHeight());
				paint.setColor(mBackgroundColor);
				canvas.drawRoundRect(f, mRadius, mRadius, paint);
			}
		};
		ShapeDrawable drawable = new ShapeDrawable(shape);
		if (Build.VERSION.SDK_INT > 15) {
			setBackground(drawable);
		} else {
			setBackgroundDrawable(drawable);
		}
	}

	public void setOnCompleteListener(OnCompleteListener listener) {
		mListener = listener;
	}

	public interface OnCompleteListener {
		void onComplete();
	}

	private class MyTypeEvaluator implements TypeEvaluator<Integer> {

		@Override
		public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
			return (int) (startValue + (endValue - startValue) * fraction);
		}

	}
}
