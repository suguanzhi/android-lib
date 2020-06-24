package com.android.sgzcommon.take_photo;

import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnDeletePhotoListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoClickListener;

import java.util.List;
import java.util.Map;

/**
 * Created by sgz on 2020/1/10.
 */
public interface TakePhotos extends TakePhoto {

    /**
     * @param photoUploads
     */
    void addPhotoUploads(List<? extends PhotoUpload> photoUploads);

    /**
     * @return
     */
    List<PhotoUpload> getPhotoUploads();

    /**
     * @param listener
     */
    void setOnTakePhotoClickListener(OnTakePhotoClickListener listener);

    /**
     * @param listener
     */
    void setOnDeletePhotoListener(OnDeletePhotoListener listener);

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
    void notifyTakePhotoChanged();

    /**
     * @param position
     */
    void notifyTakePhotoChanged(int position);

}
