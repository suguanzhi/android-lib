package com.android.sgzcommon.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import uk.co.senab.photoview.PhotoView;

/**
 * 作者github地址：https://github.com/githubwing/DragPhotoView
 * 存在的背景宽高不适应的bug，su已修改。
 */
public class DragPhotoView extends PhotoView {
    private Paint mPaint;
    private float mDownX;
    private float mDownY;
    private float mTranslateY;
    private float mTranslateX;
    private float mMoveScale;
    private float mMinMoveScale;
    private float mExistMaxY;
    //控件最大宽度
    private int mWidth;
    //控件最大高度
    private int mHeight;
    private int mCurrentWidth;
    private int mCurrentHeight;
    private int mMoveAlpha;
    private static final int MAX_TRANSLATE_Y = 500;
    private static final long DURATION = 300L;
    private boolean isCanFinish;
    private boolean isAnimate;
    private boolean isTouchEvent;
    private DragPhotoView.OnTouchListener mOnTouchListener;

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
        this.mExistMaxY = 500F;
        this.isCanFinish = false;
        this.isAnimate = false;
        this.isTouchEvent = false;
        this.mPaint = new Paint();
        this.mPaint.setColor(-16777216);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //bug修复
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mExistMaxY = mHeight / 3;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.mPaint.setAlpha(this.mMoveAlpha);
        canvas.drawRect(0.0F, 0.0F, (float) mWidth, (float) mHeight, this.mPaint);
        canvas.translate(this.mTranslateX, this.mTranslateY);
        canvas.scale(this.mMoveScale, this.mMoveScale, (float) (this.mCurrentWidth / 2), (float) (this.mCurrentHeight / 2));
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
        if (this.mTranslateY > mExistMaxY) {
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

        float percent = mTranslateY / mExistMaxY;
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
                DragPhotoView.this.isAnimate = true;
            }

            public void onAnimationEnd(Animator animator) {
                DragPhotoView.this.isAnimate = false;
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