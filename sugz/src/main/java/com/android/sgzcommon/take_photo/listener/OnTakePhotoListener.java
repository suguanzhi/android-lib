package com.android.sgzcommon.take_photo.listener;

import java.io.File;

public interface OnTakePhotoListener {
    /**
     * photo可能过大，使用ImageView展示时，需要压缩
     * @param photo photo or null
     */
    void onPhoto(File photo);
}