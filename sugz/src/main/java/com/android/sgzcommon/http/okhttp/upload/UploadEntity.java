package com.android.sgzcommon.http.okhttp.upload;

import java.io.File;

import androidx.annotation.MainThread;

/**
 * @author sgz
 * @date 2020/5/22
 */
public class UploadEntity {

    private STATE state;
    private int progress;
    private String path;
    private OnProgressListener listener;

    public enum STATE {
        STATE_LOADING,STATE_LOADING_FAIL, STATE_UPLOAD_READY, STATE_UPLOADING, STATE_UPLOAD_SUCCESS, STATE_UPLOAD_FAIL,
    }

    public UploadEntity(String path) {
        this.path = path;
        state = STATE.STATE_LOADING;
    }

    /**
     * 必须在主线程中调用
     * @param state
     */
    @MainThread
    public void setState(STATE state) {
        this.state = state;
        if (listener != null) {
            listener.onState(state);
        }
    }

    /**
     * 必须在主线程中调用
     * @param progress
     */
    @MainThread
    public void setProgress(int progress) {
        this.progress = progress;
        if (listener != null) {
            listener.onProgress(progress);
        }
    }

    public STATE getState() {
        return state;
    }

    public int getProgress() {
        return progress;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return new File(path);
    }

    public void setOnProgressListener(OnProgressListener listener) {
        this.listener = listener;
    }

    public interface OnProgressListener {
        void onProgress(int progress);

        void onState(STATE state);
    }
}
