package com.android.sgzcommon.take_photo;

import androidx.annotation.Nullable;

/**
 * @author sgz
 * @date 2020/6/24
 */
public interface GetPhoto {

    int CAMERA_REQUEST_CODE = 207;
    int REQUEST_TAKE_PHOTO_CODE = 371;
    int REQUEST_CHOOSE_PHOTO_CODE = 836;

    /**
     * 调用系统照相机拍照
     *
     * @param path 照片存放路径
     */
    void takePhoto(@Nullable String path);

    void choosePhoto();
}
