package com.android.sgzcommon.take_photo;

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
     */
    void takePhoto();

    void choosePhoto();
}
