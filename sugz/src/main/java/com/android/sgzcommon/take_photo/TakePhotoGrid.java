package com.android.sgzcommon.take_photo;

import android.content.Intent;

import com.android.sgzcommon.take_photo.listener.OnPhotoDeleteListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoClickListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by sgz on 2020/1/10.
 */
public interface TakePhotoGrid {

    int CAMERA_REQUEST_CODE = 207;

    void addPhotoUploads(List<? extends PhotoUpload> photoUploads);

    int getPhotoSize();

    void setOnTakePhotoClickListener(OnTakePhotoClickListener listener);

    void setOnPhotoDeleteListener(OnPhotoDeleteListener listener);

    void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, final OnPhotoUploadListener listener);

    /**
     * 调用系统照相机拍照
     *
     * @param path 照片存放路径
     */
    void takePhoto(@Nullable String path);

    void clearPhotos();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void notifyTakePhotoChanged();

    void notifyTakePhotoChanged(int position);

}
