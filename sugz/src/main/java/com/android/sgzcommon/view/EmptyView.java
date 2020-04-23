package com.android.sgzcommon.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.sgzcommon.utils.UnitUtil;

public class EmptyView extends RelativeLayout implements OnClickListener {

    private TextView mTipsTV;
    private ImageView mIconIV;
    private OnEmptyViewListener mListener;
    private static final String DEFAULT_TIP_TEXT_COLOR = "#969696";

    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        int padding = UnitUtil.dp2px(context, 10);
        setPadding(padding, padding, padding, padding);
        mTipsTV = new TextView(context);
        mTipsTV.setBackgroundResource(0);
        mTipsTV.setTextSize(14);
        mTipsTV.setTextColor(Color.parseColor(DEFAULT_TIP_TEXT_COLOR));
        mIconIV = new ImageView(context);
        mIconIV.setOnClickListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        LinearLayout contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.addView(mIconIV, params);
        contentLayout.addView(mTipsTV, params);
        LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params1.addRule(CENTER_IN_PARENT);
        addView(contentLayout, params1);
    }

    public void setResource(String tip) {
        setResource(tip, 0);
    }

    public void setResource(String tip, int iconResId) {
        setResource(tip, 0, iconResId);
    }

    public void setResource(String tip, int tipBackResId, int iconResId) {
        if (0 == iconResId) {
            mIconIV.setImageBitmap(null);
        } else {
            mIconIV.setImageResource(iconResId);
        }
        if (0 == tipBackResId) {
            mTipsTV.setBackgroundResource(0);
        } else {
            mTipsTV.setBackgroundResource(tipBackResId);
        }
        mTipsTV.setText(tip);
    }

    public void setTextColor(int color) {
        mTipsTV.setTextColor(color);
    }

    public void setTextSize(float size) {
        mTipsTV.setTextSize(size);
    }

    public void dismiss() {
        setVisibility(View.GONE);
    }

    public void setOnEmptyViewListener(OnEmptyViewListener listener) {
        mListener = listener;
    }

    public interface OnEmptyViewListener {
        public void onClick();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick();
        }
    }
}
