package com.android.sgzcommon.take_photo.utils;

public class ImageObject {

    private int state;
    private int progress;
    private String path;
    //上传失败
    public static final int STATE_FAIL = -1;
    //初始状态
    public static final int STATE_INIT = 0;
    //上传中
    public static final int STATE_UPLOADING = 1;
    //上传成功
    public static final int STATE_SUCCESS = 2;


    public ImageObject() {
    }

    public ImageObject(int progress, String path) {
        this.progress = progress;
        this.path = path;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}