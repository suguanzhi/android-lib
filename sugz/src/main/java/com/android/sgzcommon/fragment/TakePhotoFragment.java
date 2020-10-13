package com.android.sgzcommon.fragment;

import android.content.Context;
import android.content.Intent;

import com.android.sgzcommon.take_photo.GetPhotoImpl;
import com.android.sgzcommon.take_photo.listener.OnPhotoListener;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * @author sgz
 * @date 2020/6/24
 */
public abstract class TakePhotoFragment extends BaseFragment {

    private GetPhotoImpl mTakePhoto;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTakePhoto = new GetPhotoImpl(mActivity);
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
    protected void takePhoto(String path, OnPhotoListener listener) {
        if (mTakePhoto != null) {
            mTakePhoto.setOnTakePhotoListener(listener);
            mTakePhoto.takePhoto(path);
        }
    }

    /**
     * 调用选取照片
     */
    protected void choosePhoto(OnPhotoListener listener) {
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
