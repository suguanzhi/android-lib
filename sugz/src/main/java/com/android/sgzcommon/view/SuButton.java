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
        int borderWidth = array.getDimensionPixelSize(R.styleable.SuButton_border_width, 0);
        int borderColor = array.getColor(R.styleable.SuButton_border_color, Color.TRANSPARENT);
        boolean stateAnimator = array.getBoolean(R.styleable.SuButton_state_animator, true);

        GradientDrawable gradientDrawable = new GradientDrawable();
        Drawable oldBackground = getBackground();
        if (oldBackground instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) oldBackground;
            gradientDrawable.setColor(cd.getColor());
        }
        if (0 == raduis) {
            raduis = getResources().getDimensionPixelSize(R.dimen.btn_radius);
        }
        int style = array.getInt(R.styleable.SuButton_style, 1);
        int backColor;
        int textColor = Color.WHITE;
        if (2 == style) {
            backColor = getResources().getColor(R.color.btn_warn);
        } else if (3 == style) {
            backColor = getResources().getColor(R.color.btn_danger);
        } else if (4 == style) {
            backColor = getResources().getColor(R.color.btn_confirm);
        } else if (5 == style) {
            backColor = getResources().getColor(R.color.btn_disable);
            textColor = getResources().getColor(R.color.grey_500);
            stateAnimator = false;
        } else if (6 == style) {
            backColor = getResources().getColor(R.color.colorPrimary);
        } else {
            backColor = Color.WHITE;
            if (0 == borderWidth) {
                borderWidth = getResources().getDimensionPixelSize(R.dimen.btn_border_width);
            }
            if (1 == style) {
                borderColor = getResources().getColor(R.color.btn_normal_stroke);
                textColor = getResources().getColor(R.color.btn_normal_stroke);
            } else if (12 == style) {
                borderColor = getResources().getColor(R.color.btn_warn);
                textColor = getResources().getColor(R.color.btn_warn);
            } else if (13 == style) {
                borderColor = getResources().getColor(R.color.btn_danger);
                textColor = getResources().getColor(R.color.btn_danger);
            } else if (14 == style) {
                borderColor = getResources().getColor(R.color.btn_confirm);
                textColor = getResources().getColor(R.color.btn_confirm);
            } else if (16 == style) {
                borderColor = getResources().getColor(R.color.colorPrimary);
                textColor = getResources().getColor(R.color.colorPrimary);
            }
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
