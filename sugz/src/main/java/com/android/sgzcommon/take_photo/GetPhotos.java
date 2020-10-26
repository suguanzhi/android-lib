package com.android.sgzcommon.take_photo;

import android.content.Intent;

import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by sgz on 2020/1/10.
 */
public interface GetPhotos extends GetPhoto {

    /**
     * @return
     */
    List<PhotoUpload> getPhotoUploads();

    /**
     * @param listener
     */
    void setOnPhotoClickListener(OnPhotoClickListener listener);

    /**
     * @param url
     * @param data
     * @param headers
     * @param listener
     */
    void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, final OnPhotoUploadListener listener);

    /**
     *
     */
    void clearPhotos();

    /**
     *
     */
    void notifyPhotoChanged();

    /**
     * @param position
     */
    void notifyPhotoChanged(int position);

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}
