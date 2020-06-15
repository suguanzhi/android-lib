package com.sgz.androidlib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sgzcommon.fragment.NavigationFragment;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListener;
import com.android.sgzcommon.utils.BitmapUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class TakePhotoFragment extends NavigationFragment {
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;

    @Override
    public boolean isOnlyClick() {
        return false;
    }

    @Override
    public void onOnlyClick(Activity activity) {

    }

    public void setMsg(String msg){
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
        takePhoto(null, new OnTakePhotoListener() {
            @Override
            public void onPhoto(File photo) {
                Log.d("TakePhotoFragment", "onPhoto: 1");
                if (photo != null) {
                    Log.d("TakePhotoFragment", "onPhoto: 2");
                    Bitmap bitmap = BitmapUtil.getFitBitmap(mContext, photo.getAbsolutePath());
                    mIvPhoto.setImageBitmap(bitmap);
                }
            }
        });
        Log.d("TakePhotoFragment", "onViewClicked: ");
    }
}
