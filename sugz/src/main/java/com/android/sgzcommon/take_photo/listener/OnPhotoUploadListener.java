package com.android.sgzcommon.take_photo.listener;

import com.android.sgzcommon.http.okhttp.upload.UploadResultSet;
import com.android.sgzcommon.take_photo.PhotoUpload;

public interface OnPhotoUploadListener {

    void onAllSuccess();

    void onSuccess(PhotoUpload photoUpload, UploadResultSet result);

    void onFail(PhotoUpload photoUpload, Exception e);
}