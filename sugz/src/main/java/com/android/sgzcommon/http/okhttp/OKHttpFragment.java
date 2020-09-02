package com.android.sgzcommon.http.okhttp;

import com.android.sgzcommon.http.util.OnHttpResponseListener;
import com.android.sgzcommon.http.util.ResultSet;

import java.util.Map;

import androidx.fragment.app.Fragment;

/**
 * Created by sgz on 2017/2/27.
 */

public class OKHttpFragment extends Fragment {

    protected void postRequest(final String url, final Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        OKHttpFactory.getInstance().postEnqueueRequest(url, data,resultSet, responseListener);
    }

    protected void getRequest(final String url, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        OKHttpFactory.getInstance().getEnqueueRequest(url,resultSet, responseListener);
    }
}
