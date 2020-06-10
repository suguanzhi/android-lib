package com.android.sgzcommon.activity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.sugz.R;

/**
 * @author sgz
 * @date 2020/6/10
 */
public abstract class BaseLoginActivity extends BaseActivity {

    protected EditText mEtPassword;
    protected ImageView mIvPasswordVisiable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgz_login);
        mEtPassword = findViewById(R.id.et_password);
        mIvPasswordVisiable = findViewById(R.id.iv_password_visiable);
        mIvPasswordVisiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = mIvPasswordVisiable.isSelected();
                mIvPasswordVisiable.setSelected(!selected);
                if (!selected) {
                    mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                String pas = mEtPassword.getText().toString();
                mEtPassword.setSelection(pas.length());
            }
        });
    }
}
