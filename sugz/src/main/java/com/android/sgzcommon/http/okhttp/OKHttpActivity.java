package com.android.sgzcommon.http.okhttp;


import com.android.sgzcommon.http.util.OnHttpResponseListener;
import com.android.sgzcommon.http.util.ResultSet;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by sgz on 2017/2/27.
 */

public class OKHttpActivity extends AppCompatActivity {

    protected void postRequest(final String url, final Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        OKHttpFactory.getInstance().postRequest(url, data, resultSet, responseListener);
    }

    protected void getRequest(final String url, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        OKHttpFactory.getInstance().getRequest(url, resultSet, responseListener);
    }
}
