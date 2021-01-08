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

    private ImageView mIvLeft;
    private ImageView mIvRight;
    private TextView mTvLeft;
    private TextView mTvRight;
    private TextView mTvTitle;
    private RelativeLayout mRlLeft;
    private RelativeLayout mRlRight;

    boolean isBack;
    private OnClickListener listener;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_sgz_title, this);
        LinearLayout barLayout = view.findViewById(R.id.ll_content);
        mIvLeft = view.findViewById(R.id.iv_left);
        mIvRight = view.findViewById(R.id.iv_right);
        mTvLeft = view.findViewById(R.id.tv_left);
        mTvRight = view.findViewById(R.id.tv_right);
        mTvTitle = view.findViewById(R.id.tv_title);
        mRlLeft = view.findViewById(R.id.rl_left);
        mRlRight = view.findViewById(R.id.rl_right);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Titlebar);
        Drawable backgroundDrawable = array.getDrawable(R.styleable.Titlebar_background);
        if (backgroundDrawable != null) {
            barLayout.setBackground(backgroundDrawable);
        }
        int height = array.getDimensionPixelSize(R.styleable.Titlebar_height, 0);
        if (height != 0) {
            ViewGroup.LayoutParams params = barLayout.getLayoutParams();
            params.height = height;
            barLayout.setLayoutParams(params);
        }

        String title = array.getString(R.styleable.Titlebar_title);
        setTitle(title);

        int textSize = array.getDimensionPixelSize(R.styleable.Titlebar_text_size, 0);
        if (textSize != 0) {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        isBack = array.getBoolean(R.styleable.Titlebar_back, true);
        Drawable leftDrawable = array.getDrawable(R.styleable.Titlebar_left);
        if (leftDrawable == null && isBack) {
            leftDrawable = getResources().getDrawable(R.drawable.ic_sgz_back_white);
        }
        setLeftDrawable(leftDrawable);

        String leftText = array.getString(R.styleable.Titlebar_left_text);
        setLeftText(leftText);
        mRlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBack) {
                    try {
                        ((Activity) getContext()).finish();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }else {
                    if (listener != null) {
                        listener.onLeftClick(v);
                    }
                }
            }
        });

        Drawable rightDrawable = array.getDrawable(R.styleable.Titlebar_right);
        setRightDrawable(rightDrawable);
        String rightText = array.getString(R.styleable.Titlebar_right_text);
        setRightText(rightText);
        mRlRight.setOnClickListener(new View.OnClickListener() {
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

    public void setLeftDrawable(Drawable leftDrawable) {
        mIvLeft.setImageDrawable(leftDrawable);
        if (leftDrawable == null) {
            mIvLeft.setVisibility(GONE);
        } else {
            mIvLeft.setVisibility(VISIBLE);
        }
    }

    public void setLeftText(CharSequence leftText) {
        mTvLeft.setText(leftText);
        if (!TextUtils.isEmpty(leftText)) {
            mTvLeft.setVisibility(VISIBLE);
        } else {
            mTvLeft.setVisibility(GONE);
        }
    }

    public void setRightDrawable(Drawable right) {
        mIvRight.setImageDrawable(right);
        if (right != null) {
            mIvRight.setVisibility(VISIBLE);
        } else {
            mIvRight.setVisibility(GONE);
        }
    }

    public void setRightText(CharSequence rightText) {
        mTvRight.setText(rightText);
        if (!TextUtils.isEmpty(rightText)) {
            mTvRight.setVisibility(VISIBLE);
        } else {
            mTvRight.setVisibility(GONE);
        }
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onLeftClick(View v);

        void onRightClick(View v);
    }
}
