package com.android.sgzcommon.take_photo;

import android.content.Intent;

import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by sgz on 2020/1/10.
 */
public interface TakePhotoGrid {

    int CAMERA_REQUEST_CODE = 207;

    int getPhotoSize();

    void uploadPhotos(String url, Map<String, String> data, final OnPhotoUploadListener listener);

    /**
     * 调用系统照相机拍照
     * @param path 照片存放路径
     */
    void takePhoto(@Nullable String path);

    void clearPhotos();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void notifyTakePhotoChanged();

    void notifyTakePhotoChanged(int position);

}
