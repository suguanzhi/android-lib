package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.sugz.R;

/**
 * @author sgz
 * @date 2020/6/10
 */
public abstract class BaseLoginActivity extends BaseActivity {

    protected EditText mEtUsername;
    protected EditText mEtPassword;
    protected Button mBtnLogin;
    protected ImageView mIvLogo;
    protected ImageView mIvPasswordVisiable;

    protected abstract void onLogoClick(View v);

    protected abstract void onLoginClick(View v);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgz_login);
        mEtUsername = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick(v);
            }
        });
        mIvLogo = findViewById(R.id.iv_login_logo);
        mIvLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoClick(v);
            }
        });
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

    /**
     * 跳转MainActivity
     * @param exit 是否退出MainActivity
     * @param c MainActivity
     */
    protected void toMainActivity(boolean exit, Class<?> c) {
        Log.d("LoginActivity", "toMainActivity: ");
        Intent intent = new Intent(this, c);
        intent.putExtra("login", true);
        intent.putExtra("exit", exit);
        startActivity(intent);
        finish();
    }
}
