package com.sgz.androidlib.fragment.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sgzcommon.take_photo.listener.OnPhotoListener;
import com.sgz.androidlib.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class TestTakePhotoFragment extends com.android.sgzcommon.fragment.TakePhotoFragment {
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;

    public void setMsg(String msg) {
        mTvMsg.setText(msg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_take_photo;
    }

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        Log.d("NavigationFragment", "init: one init !");
    }

    @OnClick(R.id.btn_take_photo)
    public void onViewClicked() {
        takePhoto(new OnPhotoListener() {
            @Override
            public void onPhoto(Bitmap bitmap) {
                Log.d("TakePhotoFragment", "onPhoto: 1");
                Log.d("TakePhotoFragment", "onPhoto: 2");
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 480, 640, true);
                mIvPhoto.setImageBitmap(b);
            }
        });
        Log.d("TakePhotoFragment", "onViewClicked: ");
    }
}
