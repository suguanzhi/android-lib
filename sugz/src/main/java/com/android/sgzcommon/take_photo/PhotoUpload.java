package com.android.sgzcommon.take_photo;

import com.android.sgzcommon.http.okhttp.upload.UploadEntity;

public class PhotoUpload extends UploadEntity {

    private int position;

    public PhotoUpload(String path) {
        super(path);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}