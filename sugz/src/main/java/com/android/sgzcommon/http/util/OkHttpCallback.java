package com.android.sgzcommon.http.util;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author sgz
 * @date 2020/9/2
 */
public abstract class OkHttpCallback implements Callback {

    private OnHttpResponseListener listener;

    public abstract void onCallback(Response response, IOException e, OnHttpResponseListener listener);

    public OnHttpResponseListener getListener() {
        return listener;
    }

    public OkHttpCallback(OnHttpResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        onCallback(null, e, listener);
    }

    @Override
    public void onResponse(Response response) {
        onCallback(response, null, listener);
    }
}
