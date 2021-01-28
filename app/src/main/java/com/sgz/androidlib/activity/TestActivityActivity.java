package com.sgz.androidlib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.activity.BaseQRCodeActivity;
import com.android.sgzcommon.view.TitleBar;
import com.sgz.androidlib.R;
import com.sgz.androidlib.activity.sample.TestBaseRefreshListActivity;
import com.sgz.androidlib.activity.sample.TestBaseGetPhotosActivity;
import com.sgz.androidlib.activity.sample.TestBaseTakePhotoActivity;
import com.sgz.androidlib.activity.sample.TestMyQRCodeActivity;
import com.sgz.androidlib.activity.sample.TestWebActivity;
import com.sgz.androidlib.others.sample.TestUpdateVersionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/9/18
 */
public class TestActivityActivity extends BaseActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R.id.btn_qrcodet)
    Button mBtnQrcodet;
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;
    @BindView(R.id.btn_take_photos)
    Button mBtnTakePhotos;
    @BindView(R.id.btn_web)
    Button mBtnWeb;
    @BindView(R.id.btn_version)
    Button mBtnVersion;
    @BindView(R.id.btn_my_qrcode)
    Button mBtnMyQrcode;
    @BindView(R.id.btn_refresh)
    Button mBtnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_qrcodet, R.id.btn_take_photo, R.id.btn_take_photos, R.id.btn_web, R.id.btn_version, R.id.btn_my_qrcode,R.id.btn_refresh})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_qrcodet:
                intent = new Intent(mContext, BaseQRCodeActivity.class);
                break;
            case R.id.btn_take_photo:
                intent = new Intent(mContext, TestBaseTakePhotoActivity.class);
                break;
            case R.id.btn_take_photos:
                intent = new Intent(mContext, TestBaseGetPhotosActivity.class);
                break;
            case R.id.btn_web:
                intent = new Intent(mContext, TestWebActivity.class);
                intent.putExtra("title", "WebActivity");
                intent.putExtra("url", "http://www.hao123.com");
                break;
            case R.id.btn_version:
                intent = new Intent(mContext, TestUpdateVersionActivity.class);
                break;
            case R.id.btn_my_qrcode:
                intent = new Intent(mContext, TestMyQRCodeActivity.class);
                break;
            case R.id.btn_refresh:
                intent = new Intent(mContext, TestBaseRefreshListActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
