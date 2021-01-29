package com.android.sgzcommon.take_photo.listener;

import com.android.sgzcommon.take_photo.entity.PhotoUpload;

import java.util.List;

public interface OnTakePhotoListListener {

    void onPhotos(List<PhotoUpload> uploads);
}