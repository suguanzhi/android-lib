package com.android.sgzcommon.take_photo;

import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;

import java.util.List;
import java.util.Map;

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

}
