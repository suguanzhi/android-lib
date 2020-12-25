package com.android.sgzcommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.android.sugz.R;

import androidx.appcompat.widget.AppCompatButton;

/**
 * @author sgz
 * @date 2020/12/5
 */
public class SuButton extends AppCompatButton {

    private static final int STYLE_NORMAL = 1;
    private static final int STYLE_WARN = 2;
    private static final int STYLE_DANGER = 3;
    private static final int STYLE_CONFIRM = 4;
    private static final int STYLE_DISABLE = 5;

    private static final int RADIUS_STYLE_WRAP = 0;
    private static final int RADIUS_STYLE_CIRCLE = 1;

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
        int borderWidth = array.getDimensionPixelSize(R.styleable.SuButton_border_width, getResources().getDimensionPixelSize(R.dimen.btn_border_width));
        int color = array.getColor(R.styleable.SuButton_style_color, getResources().getColor(R.color.colorPrimary));
        boolean stroke = array.getBoolean(R.styleable.SuButton_stroke, false);
        boolean stateAnimator = array.getBoolean(R.styleable.SuButton_state_animator, true);
        int style = array.getInt(R.styleable.SuButton_style, 0);

        GradientDrawable gradientDrawable = new GradientDrawable();
        Drawable oldBackground = getBackground();
        if (oldBackground instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) oldBackground;
            gradientDrawable.setColor(cd.getColor());
        }
        if (0 == raduis) {
            raduis = getResources().getDimensionPixelSize(R.dimen.btn_radius);
        }
        int textColor;
        int backColor;
        int borderColor;
        if (STYLE_NORMAL == style) {
            stroke = true;
            color = getResources().getColor(R.color.btn_normal);
        } else if (STYLE_WARN == style) {
            color = getResources().getColor(R.color.btn_warn);
        } else if (STYLE_DANGER == style) {
            color = getResources().getColor(R.color.btn_danger);
        } else if (STYLE_CONFIRM == style) {
            color = getResources().getColor(R.color.btn_confirm);
        } else if (STYLE_DISABLE == style) {
            color = getResources().getColor(R.color.btn_disable);
            stateAnimator = false;
        }
        if (stroke) {
            textColor = color;
            backColor = Color.WHITE;
        } else {
            textColor = Color.WHITE;
            backColor = color;
        }
        borderColor = color;
        if (STYLE_DISABLE == style) {
            textColor = getResources().getColor(R.color.grey_500);
        }

        int radiusStyle = array.getInt(R.styleable.SuButton_radius_style, RADIUS_STYLE_WRAP);
        if (RADIUS_STYLE_CIRCLE == radiusStyle) {
            raduis = Integer.MAX_VALUE;
        }
        setTextColor(textColor);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.btn_text_size));
        gradientDrawable.setColor(backColor);
        gradientDrawable.setCornerRadius(raduis);
        gradientDrawable.setStroke(borderWidth, borderColor);
        setBackground(gradientDrawable);
        if (!stateAnimator) {
            setStateListAnimator(null);
        }
        int padding = getResources().getDimensionPixelSize(R.dimen.btn_padding);
        setPadding(padding, 0, padding, 0);
        setGravity(Gravity.CENTER);
        array.recycle();
    }

}
