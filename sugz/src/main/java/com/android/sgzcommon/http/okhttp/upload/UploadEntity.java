package com.android.sgzcommon.http.okhttp.upload;

import java.io.File;

/**
 * @author sgz
 * @date 2020/5/22
 */
public interface UploadEntity {

    enum STATE {
        STATE_START,STATE_UPLOADING, STATE_SUCCESS, STATE_FAIL,
    }

    void setProgress(int progress);

    int getProgress();

    void setState(STATE state);

    STATE getState();

    File getFile();
}
