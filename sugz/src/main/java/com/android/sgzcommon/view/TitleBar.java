package com.android.sgzcommon.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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

import androidx.annotation.Nullable;

/**
 * Created by sgz on 2019/9/24 0024.
 */
public class TitleBar extends LinearLayout {

    private ImageView mIvBack;
    private ImageView mIvLeft;
    private ImageView mIvRight;
    private TextView mTvLeft;
    private TextView mTvRight;
    private TextView mTvTitle;
    private RelativeLayout mRlLeft;
    private RelativeLayout mRlRight;
    private OnLeftRightClickListener listener;

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_title, this);
        LinearLayout barLayout = view.findViewById(R.id.ll_content);
        mIvBack = view.findViewById(R.id.iv_back);
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
        setTitle(title);

        int textSize = array.getDimensionPixelSize(R.styleable.titlebar_text_size, 0);
        if (textSize != 0) {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        Drawable leftDrawable = array.getDrawable(R.styleable.titlebar_left);
        setLeft(leftDrawable);
        Drawable backDrawable = array.getDrawable(R.styleable.titlebar_back);
        if (backDrawable != null) {
            setBack(backDrawable);
        }
        String leftText = array.getString(R.styleable.titlebar_left_text);
        setLeftText(leftText);
        mIvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((Activity) getContext()).finish();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
        mRlLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeftClick(v);
                }
            }
        });

        Drawable rightDrawable = array.getDrawable(R.styleable.titlebar_right);
        setRight(rightDrawable);
        String rightText = array.getString(R.styleable.titlebar_right_text);
        setRightText(rightText);
        mRlRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
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
        left();
    }

    public void setBack(Drawable back) {
        mIvBack.setImageDrawable(back);
        left();
    }

    public void setLeftText(CharSequence left) {
        mTvLeft.setText(left);
        left();
    }

    private void left() {
        Drawable leftDrawable = mIvLeft.getDrawable();
        Drawable backDrawable = mIvBack.getDrawable();
        String leftText = mTvLeft.getText().toString();
        if (leftDrawable == null && backDrawable == null && TextUtils.isEmpty(leftText)) {
            mRlLeft.setVisibility(INVISIBLE);
        } else {
            mRlLeft.setVisibility(VISIBLE);
            if (leftDrawable == null && TextUtils.isEmpty(leftText)) {
                if (backDrawable != null) {
                    mIvBack.setVisibility(VISIBLE);
                } else {
                    mIvBack.setVisibility(GONE);
                }
            } else {
                mIvBack.setVisibility(GONE);
                if (!TextUtils.isEmpty(leftText)) {
                    mTvLeft.setVisibility(VISIBLE);
                } else {
                    mTvLeft.setVisibility(GONE);
                }
                if (leftDrawable != null) {
                    mIvLeft.setVisibility(VISIBLE);
                } else {
                    mIvLeft.setVisibility(GONE);
                }
            }
        }
    }

    public void setRight(Drawable right) {
        mIvRight.setImageDrawable(right);
        right();
    }

    public void setRightText(CharSequence right) {
        mTvRight.setText(right);
        right();
    }

    private void right() {
        Drawable rightDrawable = mIvRight.getDrawable();
        String rightText = mTvRight.getText().toString();
        if (rightDrawable == null && TextUtils.isEmpty(rightText)) {
            mRlRight.setVisibility(INVISIBLE);
        } else {
            mRlRight.setVisibility(VISIBLE);
            if (!TextUtils.isEmpty(rightText)) {
                mTvRight.setVisibility(VISIBLE);
            } else {
                mTvRight.setVisibility(GONE);
            }
            if (rightDrawable != null) {
                mIvRight.setVisibility(VISIBLE);
            } else {
                mIvRight.setVisibility(GONE);
            }
        }
    }

    public void setOnLeftRightClickListener(OnLeftRightClickListener listener) {
        this.listener = listener;
    }


    public interface OnLeftRightClickListener {
        void onLeftClick(View v);

        void onRightClick(View v);
    }
}
