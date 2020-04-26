package com.android.sgzcommon.image;

import com.android.sgzcommon.http.okhttp.upload.UploadResultSet;

import java.io.File;
import java.util.Map;

/**
 * Created by sgz on 2020/1/10.
 */
public interface TakePhotoGrid {

    int CAMERA_REQUEST_CODE = 207;

    int getImageSize();

    void uploadImages(String url, Map<String, String> data, final OnImageUploadListener listener);

    /**
     * 调用系统照相机拍照
     */
    void takePhoto();

    /**
     * 清除已拍照片
     */
    void clearImages();

    void notifyTakePhotoChanged();

    void notifyTakePhotoChanged(int position);

    interface OnImageUploadListener {

        void onAllSuccess();

        void onSuccess(File file, UploadResultSet result);

        void onFail(File file, Exception e);
    }

}
