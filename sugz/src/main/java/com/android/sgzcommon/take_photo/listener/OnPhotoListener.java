package com.android.sgzcommon.take_photo.listener;

import com.android.sgzcommon.take_photo.PhotoUpload;

import java.util.List;

public interface OnPhotoListener {

    void onAddPhoto(List<PhotoUpload> uploads, PhotoUpload photo);

}