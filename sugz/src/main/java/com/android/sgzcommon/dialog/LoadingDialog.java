package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sgzcommon.view.LoadingView;
import com.android.sugz.R;

public class LoadingDialog extends Dialog {

    protected Context mContext;
    private LoadingView mLoading;
    private ProgressBar mProgressbar;
    private TextView mTvTip;

    private String mTip = "加载中";
    private TYPE mType = TYPE.LOADING;

    public enum TYPE {
        LOADING, PROGRESS
    }

    public LoadingDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        initLayout();
        mLoading = findViewById(R.id.lv_loading);
        mProgressbar = findViewById(R.id.pb_loading);
        mTvTip = findViewById(R.id.tv_tip);
        setTip(mTip);
        setType(mType);
    }

    @Override
    protected void onStart() {
        mLoading.setVisibility(View.VISIBLE);
        super.onStart();
    }

    public void setTip(String tip) {
        mTip = tip;
        if (mTvTip != null) {
            mTvTip.setText(mTip);
            if (TextUtils.isEmpty(mTip)){
                mTvTip.setVisibility(View.GONE);
            }else {
                mTvTip.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setType(TYPE type) {
        mType = type;
        if (TYPE.LOADING == type) {
            if (mProgressbar != null) {
                mProgressbar.setVisibility(View.GONE);
            }
            if (mLoading != null) {
                mProgressbar.setVisibility(View.VISIBLE);
            }
        } else {
            if (mProgressbar != null) {
                mProgressbar.setVisibility(View.VISIBLE);
            }
            if (mLoading != null) {
                mProgressbar.setVisibility(View.GONE);
            }
        }
    }

    private void initLayout() {
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = lp.width;
        win.setAttributes(lp);
    }

    public void show(String tip) {
        super.show();
        setTip(tip);
    }
}