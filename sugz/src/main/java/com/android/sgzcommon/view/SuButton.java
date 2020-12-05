package com.android.sgzcommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;

import com.android.sugz.R;

import androidx.appcompat.widget.AppCompatButton;

/**
 * @author sgz
 * @date 2020/12/5
 */
public class SuButton extends AppCompatButton {
    public SuButton(Context context) {
        this(context, null);
    }

    public SuButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.buttonStyle);
    }

    public SuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SuButton);
        int raduis = array.getDimensionPixelSize(R.styleable.SuButton_radius, 0);
        Drawable back = getBackground();
        if (back instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) back;
            int color = cd.getColor();
            ShapeDrawable shapeDrawable = new ShapeDrawable();
            shapeDrawable.getPaint().setColor(color);
            float[] rs = new float[]{raduis, raduis, raduis, raduis, raduis, raduis, raduis, raduis};
            RoundRectShape roundRectShape = new RoundRectShape(rs, null, null);
            shapeDrawable.setShape(roundRectShape);
            setBackground(shapeDrawable);
        }
        boolean borderless = array.getBoolean(R.styleable.SuButton_borderless, false);
        if (borderless) {
            setStateListAnimator(null);
        }
        array.recycle();
    }

}
