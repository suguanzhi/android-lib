package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.sugz.R;

/**
 * Created by sgz on 2019/10/15 0015.
 */
public class OneButtonDialog extends BaseDialog implements View.OnClickListener {

    private CharSequence mMsg;
    private CharSequence mSecondMsg;
    private CharSequence mButtonText;
    private TextView mTvMsg;
    private TextView mTvSecondMsg;
    private TextView mBtn;
    private OnClickListener listener;
    public static final String DEFAULT_BUTTON_TEXT = "确定";

    public OneButtonDialog(Context context) {
        super(context);
    }

    public OneButtonDialog(Context context, String msg) {
        super(context);
        this.mMsg = msg;
    }

    public OneButtonDialog(Context context, String msg, String secondMsg) {
        super(context);
        this.mMsg = msg;
        this.mSecondMsg = secondMsg;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_sgz_one_button;
    }

    @Override
    protected void onStop() {
        super.onStop();
        resetButtonText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvMsg = findViewById(R.id.tv_msg);
        mTvSecondMsg = findViewById(R.id.tv_msg_second);
        mButtonText = DEFAULT_BUTTON_TEXT;
        mBtn = findViewById(R.id.tv_confirm);
        mBtn.setOnClickListener(this);
        setMsg(mMsg);
        setSecondMsg(mSecondMsg);
        setButtonText(mButtonText);
    }

    public void setMsg(CharSequence msg) {
        mMsg = msg;
        if (mTvMsg != null) {
            if (TextUtils.isEmpty(msg)) {
                mTvMsg.setVisibility(View.GONE);
            } else {
                mTvMsg.setVisibility(View.VISIBLE);
            }
            mTvMsg.setText(msg);
        }
    }

    public void setSecondMsg(CharSequence secondMsg) {
        mSecondMsg = secondMsg;
        if (mTvSecondMsg != null) {
            if (TextUtils.isEmpty(secondMsg)) {
                mTvSecondMsg.setVisibility(View.GONE);
            } else {
                mTvSecondMsg.setVisibility(View.VISIBLE);
            }
            mTvSecondMsg.setText(secondMsg);
        }
    }

    public void setButtonText(CharSequence text) {
        mButtonText = text;
        if (mBtn != null) {
            if (TextUtils.isEmpty(text)) {
                mBtn.setVisibility(View.GONE);
            } else {
                mBtn.setVisibility(View.VISIBLE);
            }
            mBtn.setText(mButtonText);
        }
    }

    /**
     *
     */
    private void resetButtonText() {
        mButtonText = DEFAULT_BUTTON_TEXT;
        setButtonText(mButtonText);
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

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {

        void onConfirm(View view, Dialog dialog);
    }
}
