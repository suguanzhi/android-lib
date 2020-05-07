package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.sugz.R;

/**
 * Created by sgz on 2019/10/15 0015.
 */
public class OneButtonDialog extends BaseDialog implements View.OnClickListener {

    private String msg;
    private String secondMsg;
    private TextView mTvMsg;
    private TextView mTvSecondMsg;
    private TextView mBtn;
    private OnclickListener listener;

    public OneButtonDialog(Context context) {
        super(context);
    }

    public OneButtonDialog(Context context, String msg) {
        super(context);
        this.msg = msg;
    }

    public OneButtonDialog(Context context, String msg, String secondMsg) {
        super(context);
        this.msg = msg;
        this.secondMsg = secondMsg;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_one_button;
    }

    @Override
    protected int getWidth() {
        return 0;
    }

    @Override
    protected int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvMsg = findViewById(R.id.tv_msg);
        mTvSecondMsg = findViewById(R.id.tv_msg_second);
        mBtn = findViewById(R.id.tv_confirm);
        mBtn.setOnClickListener(this);
        mTvMsg.setText(msg);
        mTvSecondMsg.setText(secondMsg);
    }

    public void setMsg(CharSequence msg) {
        mTvMsg.setText(msg);
    }

    public void setSecondMsg(CharSequence msg) {
        mTvSecondMsg.setText(msg);
    }

    public void setButtonText(CharSequence text) {
        mBtn.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }
        if (v.getId() == R.id.tv_confirm) {
            listener.onConfirm(v, this);
        }
    }

    public void setOnclickListener(OnclickListener listener) {
        this.listener = listener;
    }

    public interface OnclickListener {

        void onConfirm(View view, Dialog dialog);
    }
}
