package com.sgz.androidlib.others.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.view.TitleBar;
import com.android.sgzcommon.zxing.CodeUtils;
import com.sgz.androidlib.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/9/24
 */
public class TestCreateCodeActivity extends BaseActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R.id.btn_barcode)
    Button mBtnBarcode;
    @BindView(R.id.btn_qrcode)
    Button mBtnQrcode;
    @BindView(R.id.iv_barcode)
    ImageView mIvBarcode;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_code);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_barcode, R.id.btn_qrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_barcode:
                Bitmap barcodeBitmap = CodeUtils.createBarcode("123456789");
                mIvBarcode.setImageBitmap(barcodeBitmap);
                break;
            case R.id.btn_qrcode:
                Bitmap qrcodeBitmap = CodeUtils.createQrcode("123456789");
                mIvQrcode.setImageBitmap(qrcodeBitmap);
                break;
        }
    }
}
