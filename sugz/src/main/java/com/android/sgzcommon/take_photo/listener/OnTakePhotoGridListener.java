package com.android.sgzcommon.take_photo.listener;

import com.android.sgzcommon.take_photo.entity.PhotoUpload;

import java.util.List;

public interface OnTakePhotoGridListener {

    void onPhotos(List<PhotoUpload> uploads);
}