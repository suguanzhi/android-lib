package com.sgz.androidlib;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.sgzcommon.activity.BaseLoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class LoginActivity extends BaseLoginActivity {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_password_visiable)
    ImageView mIvPasswordVisiable;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @Override
    protected int getPasswordViewId() {
        return R.id.et_password;
    }

    @Override
    protected int getVisiableViewId() {
        return R.id.iv_password_visiable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        Log.d("LoginActivity", "onViewClicked: ");
    }
}
