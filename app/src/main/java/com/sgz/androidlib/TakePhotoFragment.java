package com.sgz.androidlib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.sgzcommon.fragment.NavigationFragment;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListener;

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

    @Override
    public boolean isCreate() {
        return false;
    }

    @Override
    public boolean isOnlyClick() {
        return false;
    }

    @Override
    public void onOnlyClick(Activity activity) {

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
                if (photo != null){
                    Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath());
                    mIvPhoto.setImageBitmap(bitmap);
                }
            }
        });
        Log.d("OneFragment", "onViewClicked: ");
    }
}
