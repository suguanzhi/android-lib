package com.android.sgzcommon.take_photo.utils;

import com.android.sgzcommon.http.okhttp.upload.UploadEntity;

import java.io.File;

public class PhotoUpload implements UploadEntity {

    private STATE state;
    private int progress;
    private int position;
    private String path;


    public PhotoUpload(STATE state, String path) {
        this.state = state;
        this.path = path;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void setState(STATE state) {
        this.state = state;
    }

    @Override
    public STATE getState() {
        return state;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public File getFile() {
        return new File(path);
    }
}