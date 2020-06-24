package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.sgzcommon.take_photo.TakePhotoImpl;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListener;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * @author sgz
 * @date 2020/6/24
 */
public class TakePhotoActivity extends BaseActivity {

    private TakePhotoImpl mTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTakePhoto = new TakePhotoImpl(this);
    }

    /**
     * @return
     */
    protected File getTakePhotoDir() {
        if (mTakePhoto != null) {
            return mTakePhoto.getPhotoDir();
        }
        return null;
    }

    /**
     * 调用系统照相机拍照
     */
    protected void takePhoto(String path, OnTakePhotoListener listener) {
        if (mTakePhoto != null) {
            mTakePhoto.setOnTakePhotoListener(listener);
            mTakePhoto.takePhoto(path);
        }
    }

    /**
     * 调用选取照片
     */
    protected void choosePhoto(OnTakePhotoListener listener) {
        if (mTakePhoto != null) {
            mTakePhoto.setOnTakePhotoListener(listener);
            mTakePhoto.choosePhoto();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTakePhoto != null) {
            mTakePhoto.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mTakePhoto != null) {
            mTakePhoto.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
