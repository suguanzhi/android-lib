package com.android.sgzcommon.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.android.sgzcommon.utils.UnitUtils;

import uk.co.senab.photoview.PhotoView;

/**
 * 作者github地址：https://github.com/githubwing/DragPhotoView
 * 存在的背景宽高不适应的bug，su已修改。
 */
public class DragPhotoView extends PhotoView {
    private float mDownX;
    private float mDownY;
    private float mTranslateY;
    private float mTranslateX;
    private float mMoveScale;
    private float mMinMoveScale;
    private float mExitMaxY;
    //控件最大宽度
    private int mWidth;
    //控件最大高度
    private int mHeight;
    private int mCurrentWidth;
    private int mCurrentHeight;
    private int mMoveAlpha;
    private boolean isAnimating;
    private boolean isCanFinish;
    private boolean isTouchEvent;
    private Paint mPaint;
    private Paint mTipBkgPaint;
    private TextPaint mTipPaint;
    private DragPhotoView.OnTouchListener mOnTouchListener;
    private static final String TIP = "松开退出";

    public DragPhotoView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DragPhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public DragPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.mMoveScale = 1.0F;
        this.mMinMoveScale = 0.4F;
        this.mMoveAlpha = 255;
        this.mExitMaxY = 500F;
        this.isCanFinish = false;
        this.isAnimating = false;
        this.isTouchEvent = false;
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
        mTipPaint = new TextPaint();
        mTipPaint.setColor(Color.RED);
        mTipPaint.setTextSize(UnitUtils.sp2px(25));
        mTipPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTipPaint.setTextAlign(Paint.Align.CENTER);
        mTipBkgPaint = new Paint();
        mTipBkgPaint.setColor(Color.YELLOW);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //bug修复
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mExitMaxY = mHeight / 3;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAlpha(mMoveAlpha);
        canvas.drawRect(0.0F, 0.0F, (float) mWidth, (float) mHeight, mPaint);
        if (mTranslateY > mExitMaxY) {
            Paint.FontMetrics fm = mTipPaint.getFontMetrics();
            final float tipHeight = fm.bottom - fm.top;
            final Rect tipBkgRect = new Rect(0, 0, mWidth, (int)tipHeight);
            mTipBkgPaint.setAlpha(100);
            canvas.drawRect(0, 0, mWidth, tipHeight, mTipBkgPaint);

            int baseLineY = (int) (tipBkgRect.centerY() - fm.top / 2 - fm.bottom / 2);
            canvas.drawText(TIP, tipBkgRect.centerX(), baseLineY, mTipPaint);
        }
        canvas.translate(mTranslateX, mTranslateY);
        canvas.scale(mMoveScale, mMoveScale, (float) (mCurrentWidth / 2), (float) (mCurrentHeight / 2));
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCurrentWidth = w;
        mCurrentHeight = h;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (this.getScale() == 1.0F) {
            switch (event.getAction()) {
                case 0:
                    onActionDown(event);
                    isCanFinish = !isCanFinish;
                    break;
                case 1:
                    if (event.getPointerCount() == 1) {
                        onActionUp(event);
                        isTouchEvent = false;
                        postDelayed(new Runnable() {
                            public void run() {
                                if (mTranslateX == 0.0F && mTranslateY == 0.0F && isCanFinish && mOnTouchListener != null) {
                                    mOnTouchListener.onTap(DragPhotoView.this);
                                }
                                isCanFinish = false;
                            }
                        }, 300L);
                    }
                    break;
                case 2:
                    if (mTranslateY == 0.0F && mTranslateX != 0.0F && !isTouchEvent) {
                        mMoveScale = 1.0F;
                        return super.dispatchTouchEvent(event);
                    }

                    if (mTranslateY >= 0.0F && event.getPointerCount() == 1) {
                        onActionMove(event);
                        if (mTranslateY != 0.0F) {
                            isTouchEvent = true;
                        }

                        return true;
                    }

                    if (mTranslateY >= 0.0F && (double) mMoveScale < 0.95D) {
                        return true;
                    }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    private void onActionUp(MotionEvent event) {
        if (mTranslateY > mExitMaxY) {
            if (this.mOnTouchListener != null) {
                mOnTouchListener.onExit(this, this.mTranslateX, this.mTranslateY, (float) this.mCurrentWidth, (float) this.mCurrentHeight);
            }
        } else {
            this.performAnimation();
        }

    }

    private void onActionMove(MotionEvent event) {
        float moveY = event.getY();
        float moveX = event.getX();
        mTranslateX = moveX - mDownX;
        mTranslateY = moveY - mDownY;
        if (mTranslateY < 0.0F) {
            mTranslateY = 0.0F;
        }

        float percent = mTranslateY / mExitMaxY;
        if (percent > 1.0F) {
            percent = 1.0F;
        } else if (percent < 0) {
            percent = 0;
        }
        mMoveScale = 1.0F - percent;
        mMoveAlpha = (int) (255.0F * mMoveScale);
        if (mMoveAlpha > 255) {
            mMoveAlpha = 255;
        } else if (mMoveAlpha < 0) {
            mMoveAlpha = 0;
        }
        float scalePercent = mMoveScale;
        if (mMoveScale < mMinMoveScale) {
            scalePercent = 0;
            mMoveScale = mMinMoveScale;
        }
        if (mOnTouchListener != null) {
            mOnTouchListener.onMovePercent(DragPhotoView.this, scalePercent);
        }
        invalidate();
    }

    private void performAnimation() {
        getScaleAnimation().start();
        getTranslateXAnimation().start();
        getTranslateYAnimation().start();
        getAlphaAnimation().start();
    }

    private ValueAnimator getAlphaAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(new int[]{this.mMoveAlpha, 255});
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DragPhotoView.this.mMoveAlpha = (Integer) valueAnimator.getAnimatedValue();
            }
        });
        return animator;
    }

    private ValueAnimator getTranslateYAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{this.mTranslateY, 0.0F});
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DragPhotoView.this.mTranslateY = (Float) valueAnimator.getAnimatedValue();
            }
        });
        return animator;
    }

    private ValueAnimator getTranslateXAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{this.mTranslateX, 0.0F});
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DragPhotoView.this.mTranslateX = (Float) valueAnimator.getAnimatedValue();
            }
        });
        return animator;
    }

    private ValueAnimator getScaleAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{this.mMoveScale, 1.0F});
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DragPhotoView.this.mMoveScale = (Float) valueAnimator.getAnimatedValue();
                if (mOnTouchListener != null) {
                    mOnTouchListener.onMovePercent(DragPhotoView.this, mMoveScale);
                }
                DragPhotoView.this.invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animator) {
                isAnimating = true;
            }

            public void onAnimationEnd(Animator animator) {
                DragPhotoView.this.isAnimating = false;
                animator.removeAllListeners();
            }

            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }
        });
        return animator;
    }

    private void onActionDown(MotionEvent event) {
        this.mDownX = event.getX();
        this.mDownY = event.getY();
    }

    public float getMinMoveScale() {
        return this.mMinMoveScale;
    }

    public void setMinMoveScale(float minScale) {
        this.mMinMoveScale = minScale;
    }

    public void setOnTouchListener(DragPhotoView.OnTouchListener listener) {
        this.mOnTouchListener = listener;
    }

    public void finishAnimationCallBack() {
        this.mTranslateX = (float) (-this.mCurrentWidth / 2) + (float) this.mCurrentWidth * this.mMoveScale / 2.0F;
        this.mTranslateY = (float) (-this.mCurrentHeight / 2) + (float) this.mCurrentHeight * this.mMoveScale / 2.0F;
        this.invalidate();
    }

    public interface OnTouchListener {
        void onTap(DragPhotoView view);

        void onMovePercent(DragPhotoView view, float percent);

        void onExit(DragPhotoView view, float translateX, float translateY, float width, float height);
    }
}