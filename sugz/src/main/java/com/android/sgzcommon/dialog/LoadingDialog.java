package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.sgzcommon.view.LoadingView;
import com.android.sugz.R;

public class LoadingDialog extends Dialog {

    protected Context mContext;
    private LoadingView mLoading;

    public LoadingDialog(Context context) {
        super(context,R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        initLayout();
        mLoading = findViewById(R.id.lv_loading);
    }

    @Override
    protected void onStart() {
        mLoading.setVisibility(View.VISIBLE);
        super.onStart();
    }

    private void initLayout() {
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = lp.width;
        win.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        mLoading.setVisibility(View.VISIBLE);
    }
}