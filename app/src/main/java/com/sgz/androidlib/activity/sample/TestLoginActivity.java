package com.sgz.androidlib.activity.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.sgzcommon.activity.BaseLoginActivity;
import com.sgz.androidlib.R;

import butterknife.BindView;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class TestLoginActivity extends BaseLoginActivity {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_password_visiable)
    ImageView mIvPasswordVisiable;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @Override
    protected void onLogoClick(View v) {

    }

    @Override
    protected void onLoginClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
