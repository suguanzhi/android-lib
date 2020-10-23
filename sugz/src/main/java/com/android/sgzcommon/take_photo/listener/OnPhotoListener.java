package com.android.sgzcommon.take_photo.listener;

import android.graphics.Bitmap;

public interface OnPhotoListener {
    /**
     * photo可能过大，使用ImageView展示时，需要压缩
     *
     * @param bitmap
     */
    void onPhoto(Bitmap bitmap);

}