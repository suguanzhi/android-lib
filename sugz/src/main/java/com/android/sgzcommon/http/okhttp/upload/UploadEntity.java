package com.android.sgzcommon.http.okhttp.upload;

import java.io.File;

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
        STATE_START, STATE_UPLOADING, STATE_SUCCESS, STATE_FAIL,
    }

    public UploadEntity(String path) {
        this.path = path;
        state = STATE.STATE_START;
    }

    void setState(STATE state) {
        this.state = state;
        if (listener != null) {
            listener.onState(state);
        }
    }

    void setProgress(int progress) {
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
