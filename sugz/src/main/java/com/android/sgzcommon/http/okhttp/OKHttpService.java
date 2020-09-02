package com.android.sgzcommon.http.okhttp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.sgzcommon.http.util.OnHttpResponseListener;
import com.android.sgzcommon.http.util.ResultSet;

import java.util.Map;

/**
 * Created by sgz on 2017/2/27.
 */

public class OKHttpService extends Service {


    protected void postRequest(final String url, final Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        OKHttpFactory.getInstance().postEnqueueRequest(url, data, resultSet, responseListener);
    }

    protected void getRequest(final String url, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        OKHttpFactory.getInstance().getEnqueueRequest(url, resultSet, responseListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
