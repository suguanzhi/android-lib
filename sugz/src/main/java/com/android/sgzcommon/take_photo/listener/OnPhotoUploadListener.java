package com.android.sgzcommon.take_photo.listener;

import com.android.sgzcommon.http.okhttp.upload.UploadResultSet;

import java.io.File;

public interface OnPhotoUploadListener {

    void onAllSuccess();

    void onSuccess(File file, UploadResultSet result);

    void onFail(File file, Exception e);
}