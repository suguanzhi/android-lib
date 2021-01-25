package com.sgz.androidlib.others;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.service.entity.VersionDownload;
import com.android.sgzcommon.utils.SystemUtils;
import com.android.sgzcommon.view.TitleBar;
import com.sgz.androidlib.R;
import com.sgz.androidlib.others.sample.TestBaseAdapterActivity;
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
    @BindView(R.id.btn_base_adapter)
    Button mBtnBaseAdapter;
    @BindView(R.id.btn_vibrate)
    Button mBtnVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_base_adapter, R.id.btn_code, R.id.btn_version_update, R.id.btn_vibrate})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_base_adapter:
                intent = new Intent(mContext, TestBaseAdapterActivity.class);
                break;
            case R.id.btn_code:
                intent = new Intent(mContext, TestCreateCodeActivity.class);
                break;
            case R.id.btn_version_update:
                final String url = "http://139.9.86.110:80/upload/files/20200929/盛马桂v1_0_9_1601367084098.apk";
                EventBus.getDefault().post(new VersionDownload(url, 101));
                break;
            case R.id.btn_vibrate:
                SystemUtils.vibrate(mContext);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
