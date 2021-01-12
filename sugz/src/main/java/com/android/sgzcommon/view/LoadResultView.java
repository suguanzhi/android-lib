package com.android.sgzcommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.sugz.R;

import androidx.annotation.Nullable;

/**
 * Created by sgz on 2020/1/7.
 */
public class LoadResultView extends LinearLayout {

    private ImageView mIvIcon;
    private TextView mTvTip;
    private TextView mTvButton;

    public LoadResultView(Context context) {
        this(context,null);
    }

    public LoadResultView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context,R.layout.layout_sgz_load_result, this);
        mIvIcon = findViewById(R.id.iv_icon);
        mTvTip = findViewById(R.id.tv_tip);
        mTvButton = findViewById(R.id.tv_button);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadResultView);
            Drawable background = array.getDrawable(R.styleable.LoadResultView_background);
            if (background != null) {
                setBackground(background);
            }
            Drawable icon = array.getDrawable(R.styleable.LoadResultView_icon);
            if (icon != null) {
                mIvIcon.setImageDrawable(icon);
            }

            String text = array.getString(R.styleable.LoadResultView_text);
            int textColor = array.getColor(R.styleable.LoadResultView_text_color, context.getResources().getColor(R.color.text));
            int textSize = array.getDimensionPixelSize(R.styleable.LoadResultView_text_size, 14);
            mTvTip.setTextSize(textSize);
            mTvTip.setTextColor(textColor);
            setTipText(text);

            String btnText = array.getString(R.styleable.LoadResultView_btn_text);
            int btnTextSize = array.getDimensionPixelSize(R.styleable.LoadResultView_btn_text_size, 14);
            int btnTextColor = array.getColor(R.styleable.LoadResultView_btn_text_color, context.getResources().getColor(R.color.grey_500));
            Drawable btnBackground = array.getDrawable(R.styleable.LoadResultView_btn_background);
            if (btnBackground != null) {
                mTvButton.setBackground(btnBackground);
            }
            mTvButton.setTextColor(btnTextColor);
            mTvButton.setTextSize(btnTextSize);
            setBtnText(btnText);
            array.recycle();
        }
        gone();
    }

    public void empty() {
        empty("暂无数据");
    }

    public void empty(String tip) {
        empty(tip, null);
    }

    public void empty(String tip, String btnText) {
        empty(tip, btnText, R.drawable.ic_sgz_empty);
    }

    public void empty(String tip, String btnText, int resId) {
        mIvIcon.setImageResource(resId);
        setTipText(tip);
        setBtnText(btnText);
        setVisibility(VISIBLE);
    }

    public void error() {
        error("数据异常");
    }

    public void error(String tip) {
        error(tip, null);
    }

    public void error(String tip, String btnText) {
        error(tip, btnText, R.drawable.ic_sgz_load_fail);
    }

    public void error(String tip, String btnText, int resId) {
        mIvIcon.setImageResource(resId);
        setTipText(tip);
        setBtnText(btnText);
        setVisibility(VISIBLE);
    }

    public void setBtnText(String btnText) {
        mTvButton.setText(btnText);
        if (TextUtils.isEmpty(btnText)) {
            mTvButton.setVisibility(GONE);
        } else {
            mTvButton.setVisibility(VISIBLE);
        }
    }

    public void setTipText(String tipText) {
        mTvTip.setText(tipText);
        if (TextUtils.isEmpty(tipText)) {
            mTvTip.setVisibility(GONE);
        } else {
            mTvTip.setVisibility(VISIBLE);
        }
    }

    public void gone() {
        setVisibility(GONE);
    }
}
