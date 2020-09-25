package com.sgz.androidlib.others;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.service.entity.VersionDownload;
import com.android.sgzcommon.view.TitleBar;
import com.sgz.androidlib.R;
import com.sgz.androidlib.others.sample.TestCreateCodeActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/9/24
 */
public class TestOthersActivity extends BaseActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R.id.btn_code)
    Button mBtnCode;
    @BindView(R.id.btn_version_update)
    Button mBtnOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_code, R.id.btn_version_update})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_code:
                intent = new Intent(mContext, TestCreateCodeActivity.class);
                break;
            case R.id.btn_version_update:
                final String url ="http://192.168.0.129:80/upload/files/20200921/盛马桂v1_1_0_1600656839357.apk";
                EventBus.getDefault().post(new VersionDownload(url, 101));
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
