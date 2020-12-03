package com.android.sgzcommon.http.okhttp.upload;

import java.util.List;

public interface OnUploadFileListener<T> {
    void onUploadStart(String url);

    void onUploadSuccess(String url,List<T> ts, UploadResultSet result);

    void onUploadFail(String url, List<T> ts, Exception e);

    void onEntityStart(T t);

    void onEntityValue(T t, int value);

    void onEntitySuccess(T t);

    void onEntityFail(T t, Exception e);
}