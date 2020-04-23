package com.android.sgzcommon.toast;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by sgz on 2016/4/28.
 */
public class SToast {

    private static SToast sSToast;
    private Toast mToast;
    private Context mContext;
    private TextView mTextView;
    private static final int DEFAULT_TEXT_SIZE = 60;
    private static final int DEFAULT_TEXT_SIZE_SMALL = 30;
    private static final String DEFAULT_TEXT_COLOR = "#FFFFFF";
    private static final String DEFAULT_BACKGROUND_COLOR = "#AA3E3B3B";

    private SToast(Context context) {
        mContext = context;
        mToast = new Toast(context);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mTextView = new TextView(context);
        mTextView.setTextColor(Color.parseColor(DEFAULT_TEXT_COLOR));
        mTextView.setBackgroundColor(Color.parseColor(DEFAULT_BACKGROUND_COLOR));
        mTextView.setPadding(dp2px(10f), dp2px(5f), dp2px(10f), dp2px(5f));
        mToast.setView(mTextView);
    }

    public synchronized void showText(String text) {
        mTextView.setText(text);
        mTextView.setTextSize(DEFAULT_TEXT_SIZE);
        mToast.show();
    }

    public synchronized void showSmallText(String text) {
        mTextView.setText(text);
        mTextView.setTextSize(DEFAULT_TEXT_SIZE_SMALL);
        mToast.show();
    }


    public synchronized void showText(String text, int textSize) {
        mTextView.setText(text);
        mTextView.setTextSize(textSize);
        mToast.show();
    }

    public synchronized static SToast getInstance(Context context) {
        if (sSToast == null) {
            sSToast = new SToast(context);
        }
        return sSToast;
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return
     */
    public int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px px值
     * @return
     */
    public int px2dp(float px) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue px值
     * @return
     */
    public int px2sp(float pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue sp值
     * @return
     */
    public int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
