package com.android.sgzcommon.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.android.sugz.R;

/**
 * Created by Administrator on 2015/11/9.
 */
@SuppressLint("NewApi")
public class LoadingView extends View {
    private Paint paint1;
    private Paint paint2;
    private int color1 ;
    private int color2;

    private boolean init = false;
    private ValueAnimator valueAnimator;
    private float numb = 0;

    private boolean stop = false;

    private int r = 0;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.loadingview);
        color1 = array.getColor(R.styleable.loadingview_color1,Color.parseColor("#448AFF"));
        color2 = array.getColor(R.styleable.loadingview_color2,color2= Color.parseColor("#FFFFFF"));
        paint1.setColor(color1);
        paint2.setColor(color2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!init) {
            init = true;
            r = getWidth() / 8;
            start();
        }
        numb = (Float) valueAnimator.getAnimatedValue();
        if (numb < 0) {
            canvas.drawCircle((getWidth() - 2 * r) * (1 - Math.abs(numb)) + r, getHeight() / 2, r - 5, paint2);
            canvas.drawCircle((getWidth() - 2 * r) * Math.abs(numb) + r, getHeight() / 2, r - 5 * (float) Math.abs(Math.abs(numb) - 0.5), paint1);

        } else {
            canvas.drawCircle((getWidth() - 2 * r) * (1 - Math.abs(numb - 1)) + r, getHeight() / 2, r - 5, paint1);
            canvas.drawCircle((getWidth() - 2 * r) * Math.abs(numb - 1) + r, getHeight() / 2, r - 5 * (float) Math.abs(Math.abs(numb) - 0.5), paint2);
        }
        if (valueAnimator.isRunning()) {
            invalidate();
        }

    }

    public void start() {
        valueAnimator = getValueAnimator();
        if (stop == false) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                    invalidate();
                }
            }, valueAnimator.getDuration());
        }
    }

    public void stop() {
        this.stop = true;
    }

    private ValueAnimator getValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-1f, 1f);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        return valueAnimator;
    }

}