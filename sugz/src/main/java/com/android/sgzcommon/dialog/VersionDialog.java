package com.android.sgzcommon.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.sugz.R;

/**
 * Created by sgz on 2019/5/22 0022.
 */
public class VersionDialog extends BaseDialog {

    TextView mTvVersion;
    TextView mTvRemark;
    Button mBtnCancle;
    Button mBtnConfirm;

    private String url;
    private String remark;
    private String versionName;
    private OnConfirmListener listener;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("VersionTipDialog", "onAttachedToWindow: ");
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_sgz_version;
    }

    @Override
    protected int getWidth() {
        return mWindowSize.x * 4 / 5;
    }

    @Override
    protected int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public VersionDialog(Context context, String url, String version, String remark) {
        super(context);
        this.url = url;
        this.versionName = version;
        this.remark = remark;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("VersionTipDialog", "onCreate: ");
        mTvVersion = findViewById(R.id.tv_version);
        mTvRemark = findViewById(R.id.tv_remark);
        mBtnCancle = findViewById(R.id.btn_cancle);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        setVersion(versionName);
        setRemark(remark);
        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onCancle();
                }
            }
        });

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm(VersionDialog.this, url);
                }
            }
        });
    }

    private void setRemark(String remark) {
        this.remark = remark;
        if (mTvRemark != null) {
            mTvRemark.setText("版本说明：" + remark);
        }
    }

    private void setVersion(String version) {
        this.versionName = version;
        if (mTvVersion != null) {
            mTvVersion.setText("最新版本：" + versionName);
        }
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmListener {
        void onCancle();

        void onConfirm(VersionDialog dialog, String url);
    }
}
