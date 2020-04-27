package com.android.sgzcommon.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.sugz.R;
import com.zhpan.bannerview.annotation.Visibility;

import androidx.annotation.Nullable;

/**
 * Created by sgz on 2019/9/24 0024.
 */
public class TitleBar extends LinearLayout {

    private ImageView mIvLeft;
    private ImageView mIvRight;
    private TextView mTvLeft;
    private TextView mTvRight;
    private TextView mTvTitle;
    private RelativeLayout mRlLeft;
    private RelativeLayout mRlRight;
    private OnLeftAndRightClickListener listener;

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_title, this);
        LinearLayout barLayout = view.findViewById(R.id.ll_content);
        mIvLeft = view.findViewById(R.id.iv_left);
        mIvRight = view.findViewById(R.id.iv_right);
        mTvLeft = view.findViewById(R.id.tv_left);
        mTvRight = view.findViewById(R.id.tv_right);
        mTvTitle = view.findViewById(R.id.tv_title);
        mRlLeft = view.findViewById(R.id.rl_left);
        mRlRight = view.findViewById(R.id.rl_right);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.titlebar);
        Drawable backgroundDrawable = array.getDrawable(R.styleable.titlebar_background);
        if (backgroundDrawable != null) {
            barLayout.setBackground(backgroundDrawable);
        }
        int height = array.getDimensionPixelSize(R.styleable.titlebar_height, 0);
        if (height != 0) {
            ViewGroup.LayoutParams params = barLayout.getLayoutParams();
            params.height = height;
            barLayout.setLayoutParams(params);
        }

        String title = array.getString(R.styleable.titlebar_title);
        mTvTitle.setText(title);

        int textSize = array.getDimensionPixelSize(R.styleable.titlebar_text_size, 0);
        if (textSize != 0) {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        Drawable leftDrawable = array.getDrawable(R.styleable.titlebar_left);
        boolean back = array.getBoolean(R.styleable.titlebar_back, true);
        if (!back) {
            mIvLeft.setImageDrawable(leftDrawable);
        }
        String leftText = array.getString(R.styleable.titlebar_left_text);
        mTvLeft.setText(leftText);
        mRlLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (back) {
                    try {
                        ((Activity) getContext()).finish();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } else {
                    if (listener != null && leftDrawable != null) {
                        listener.onLeftClick(v);
                    }
                }
            }
        });

        Drawable rightDrawable = array.getDrawable(R.styleable.titlebar_right);
        mIvRight.setImageDrawable(rightDrawable);
        String rightText = array.getString(R.styleable.titlebar_right_text);
        mTvRight.setText(rightText);
        mRlRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null & rightDrawable != null) {
                    listener.onRightClick(v);
                }
            }
        });

        array.recycle();
    }

    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setLeft(Drawable left) {
        mIvLeft.setImageDrawable(left);
    }

    public void setLeftText(CharSequence left) {
        mTvLeft.setText(left);
    }

    public void setLeftVisible(@Visibility int visible) {
        mRlLeft.setVisibility(visible);
    }

    public void setRight(Drawable right) {
        mIvRight.setImageDrawable(right);
    }

    public void setRightText(CharSequence right) {
        mTvRight.setText(right);
    }

    public void setRightVisible(@Visibility int visible) {
        mRlRight.setVisibility(visible);
    }

    public void setListener(OnLeftAndRightClickListener listener) {
        this.listener = listener;
    }


    public interface OnLeftAndRightClickListener {
        void onLeftClick(View v);

        void onRightClick(View v);
    }
}
