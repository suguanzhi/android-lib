package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.sugz.R;

import androidx.annotation.ColorInt;

/**
 * Created by sgz on 2019/10/15 0015.
 */
public class TwoButtonDialog extends BaseDialog implements View.OnClickListener {

    private String mMsg;
    private String mLeftText;
    private String mRightText;
    private String mSecondMsg;
    private TextView mTvMsg;
    private TextView mTvSecondMsg;
    private TextView mBtnLeft;
    private TextView mBtnRight;
    private OnclickListener listener;

    private SpannableStringBuilder mMsgSpanBuilder;
    private SpannableStringBuilder mSecondMsgSpanBuilder;

    public TwoButtonDialog(Context context) {
        super(context);
    }

    public TwoButtonDialog(Context context, String msg) {
        super(context);
        this.mMsg = msg;
    }

    public TwoButtonDialog(Context context, String msg, String secondMsg) {
        super(context);
        mMsg = msg;
        mSecondMsg = secondMsg;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_two_button;
    }

    @Override
    protected int getWidth() {
        return mMinSize * 4 / 5;
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
        mBtnLeft = findViewById(R.id.tv_left);
        mBtnRight = findViewById(R.id.tv_right);
        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this::onClick);

        mMsgSpanBuilder = new SpannableStringBuilder();
        mSecondMsgSpanBuilder = new SpannableStringBuilder();

        setMsg(mMsg);
        setSecondMsg(mSecondMsg);
        if (!TextUtils.isEmpty(mLeftText)) {
            mBtnLeft.setText(mLeftText);
        }
        if (!TextUtils.isEmpty(mRightText)) {
            mBtnRight.setText(mRightText);
        }
    }

    public void show(String msg, String secondMsg) {
        mMsg = msg;
        mSecondMsg = secondMsg;
        show();
    }

    public void setMsgSpan(ClickableSpan clickableSpan, String clickString, @ColorInt int color) {
        int start = mSecondMsg.indexOf(clickString);
        int end = start + clickString.length();
        String span = mMsgSpanBuilder.toString();
        if (!span.equals(mMsg)) {
            mMsgSpanBuilder.clear();
            mMsgSpanBuilder.append(mMsg);
        }
        mMsgSpanBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        mMsgSpanBuilder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvMsg.setMovementMethod(LinkMovementMethod.getInstance());
        setMsg(mMsgSpanBuilder);
    }

    public void setSecondMsgSpan(ClickableSpan clickableSpan, String clickString, @ColorInt int color) {
        int start = mSecondMsg.indexOf(clickString);
        int end = start + clickString.length();
        String span = mSecondMsgSpanBuilder.toString();
        if (!span.equals(mSecondMsg)) {
            mSecondMsgSpanBuilder.clear();
            mSecondMsgSpanBuilder.append(mSecondMsg);
        }
        mSecondMsgSpanBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        mSecondMsgSpanBuilder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvSecondMsg.setMovementMethod(LinkMovementMethod.getInstance());
        setSecondMsg(mSecondMsgSpanBuilder);
    }

    public void setMsg(CharSequence msg) {
        if (mTvMsg != null) {
            mTvMsg.setText(msg);
            if (TextUtils.isEmpty(msg)) {
                mTvMsg.setVisibility(View.GONE);
            } else {
                mTvMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setSecondMsg(CharSequence msg) {
        if (mTvSecondMsg != null) {
            mTvSecondMsg.setText(msg);
            if (TextUtils.isEmpty(msg)) {
                mTvSecondMsg.setVisibility(View.GONE);
            } else {
                mTvSecondMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setButtonLeftText(String text) {
        mLeftText = text;
        if (mBtnLeft != null) {
            mBtnLeft.setText(text);
        }
    }

    public void setButtonRightText(String text) {
        mRightText = text;
        if (mBtnRight != null) {
            mBtnRight.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }
        if (v.getId() == R.id.tv_left) {
            listener.onCancle(v, this);
        } else if (v.getId() == R.id.tv_right) {
            listener.onConfirm(v, this);
        }
    }

    public void setOnclickListener(OnclickListener listener) {
        this.listener = listener;
    }

    public interface OnclickListener {
        void onCancle(View view, Dialog dialog);

        void onConfirm(View view, Dialog dialog);
    }
}
