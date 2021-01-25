package com.android.sgzcommon.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
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

    private static final int STYLE_PRIMARY = 1;
    private static final int STYLE_WARN = 2;
    private static final int STYLE_DANGER = 3;
    private static final int STYLE_CONFIRM = 4;
    private static final int STYLE_DISABLE = 5;

    private static final int RADIUS_STYLE_DEFAULT = 0;
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
        ColorStateList textColor = array.getColorStateList(R.styleable.SuButton_android_textColor);
        int radius = array.getDimensionPixelSize(R.styleable.SuButton_radius, getResources().getDimensionPixelSize(R.dimen.btn_radius));
        int radiusStyle = array.getInt(R.styleable.SuButton_radius_style, RADIUS_STYLE_DEFAULT);
        if (RADIUS_STYLE_CIRCLE == radiusStyle) {
            radius = Integer.MAX_VALUE;
        }
        int borderWidth = array.getDimensionPixelSize(R.styleable.SuButton_border_width, getResources().getDimensionPixelSize(R.dimen.btn_border_width));
        boolean stroke = array.getBoolean(R.styleable.SuButton_stroke, false);
        boolean stateAnimator = array.getBoolean(R.styleable.SuButton_state_animator, true);
        int style = array.getInt(R.styleable.SuButton_style, 0);

        int styleColor;
        int styleBackgroundColor;
        if (STYLE_PRIMARY == style) {
            styleColor = getResources().getColor(R.color.colorPrimary);
        } else if (STYLE_WARN == style) {
            styleColor = getResources().getColor(R.color.btn_warn);
        } else if (STYLE_DANGER == style) {
            styleColor = getResources().getColor(R.color.btn_danger);
        } else if (STYLE_CONFIRM == style) {
            styleColor = getResources().getColor(R.color.btn_confirm);
        } else if (STYLE_DISABLE == style) {
            stateAnimator = false;
            styleColor = getResources().getColor(R.color.btn_disable);
        } else {
            stroke = true;
            styleColor = getResources().getColor(R.color.btn_normal);
        }
        int styleTextColor;
        if (stroke) {
            styleTextColor = styleColor;
            styleBackgroundColor = Color.WHITE;
        } else {
            styleTextColor = Color.WHITE;
            styleBackgroundColor = styleColor;
        }
        int borderColor = array.getColor(R.styleable.SuButton_border_color, styleColor);
        int backgroundColor = array.getColor(R.styleable.SuButton_background_color, styleBackgroundColor);

        if (textColor == null) {
            textColor = ColorStateList.valueOf(styleTextColor);
        }
        if (STYLE_DISABLE == style) {
            setEnabled(false);
            backgroundColor = getResources().getColor(R.color.btn_disable);
            textColor = ColorStateList.valueOf(getResources().getColor(R.color.grey_500));
        } else {
            setEnabled(true);
        }
        setTextColor(textColor);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.btn_text_size));

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(backgroundColor);
        gradientDrawable.setCornerRadius(radius);
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
