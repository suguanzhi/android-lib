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
import android.widget.TextView;

import com.android.sugz.R;

import androidx.annotation.Nullable;

/**
 * Created by sgz on 2019/9/24 0024.
 */
public class TitileBar extends LinearLayout {

    private ImageView mIvLeft;
    private ImageView mIvRight;
    private TextView mTvTitle;
    private Drawable mLeft;
    private Drawable mRight;
    private OnLeftAndRightClickListener listener;

    public TitileBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_title, this);
        LinearLayout barLayout = view.findViewById(R.id.ll_content);
        mIvLeft = view.findViewById(R.id.iv_left);
        mIvRight = view.findViewById(R.id.iv_right);
        mTvTitle = view.findViewById(R.id.tv_title);

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
        mIvLeft.setOnClickListener(new OnClickListener() {
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
        mIvRight.setOnClickListener(new OnClickListener() {
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

    public void setRight(Drawable right) {
        mIvRight.setImageDrawable(right);
    }

    public void setListener(OnLeftAndRightClickListener listener) {
        this.listener = listener;
    }


    public interface OnLeftAndRightClickListener {
        void onLeftClick(View v);

        void onRightClick(View v);
    }
}
