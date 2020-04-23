package com.android.sgzcommon.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * drawableTop与文本一起垂直居中显示
 */
public class DrawableCenterTextView extends TextView {

    private int width;
    private int height;

    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
            int drawablePadding = getCompoundDrawablePadding();
            float textHeight = fontMetrics.bottom - fontMetrics.top;
            int drawableHeight = 0;
            Drawable drawableTop = drawables[1];
            if (drawableTop != null) {
                Rect drawableTopBounds = drawableTop.getBounds();
                drawableHeight += drawableTopBounds.height();
            }
            float bodyHeight = textHeight + drawableHeight + drawablePadding;
            canvas.translate(0, (getHeight() - bodyHeight - getPaddingBottom() - getPaddingTop()) / 2);
        }
        super.onDraw(canvas);
    }
}