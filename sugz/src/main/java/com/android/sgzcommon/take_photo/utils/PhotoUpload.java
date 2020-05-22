package com.android.sgzcommon.take_photo.utils;

import com.android.sgzcommon.http.okhttp.upload.UploadEntity;

import java.io.File;

public class PhotoUpload implements UploadEntity {

    private STATE state;
    private int progress;
    private int position;
    private String path;
    private OnProgressListener listener;


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
        if (listener != null) {
            listener.onProgress(position, progress);
        }
    }

    @Override
    public File getFile() {
        return new File(path);
    }

    public void setOnProgressListener(OnProgressListener listener) {
        this.listener = listener;
    }

    public interface OnProgressListener {
        void onProgress(int position, int progress);
    }
}