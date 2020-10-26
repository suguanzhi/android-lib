package com.android.sgzcommon.take_photo.listener;

import android.view.View;

import com.android.sgzcommon.take_photo.entity.PhotoUpload;

public interface OnPhotoClickListener {
    void onClick(View view);

    void onDelete(int position, PhotoUpload photoUpload);
}