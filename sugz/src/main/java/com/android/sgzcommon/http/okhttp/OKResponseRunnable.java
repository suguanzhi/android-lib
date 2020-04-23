package com.android.sgzcommon.http.okhttp;

import com.android.sgzcommon.http.util.OnHttpResponseListener;
import com.android.sgzcommon.http.util.ResultSet;

/**
 * Created by sgz on 2017/2/24.
 */

public class OKResponseRunnable implements Runnable {

    private ResultSet mResultSet;
    private OnHttpResponseListener mResponseListener;
    private static final String TAG = "OKResponseRunnable";

    public OKResponseRunnable(ResultSet resultSet, OnHttpResponseListener listener) {
        mResultSet = resultSet;
        mResponseListener = listener;
    }

    @Override
    public void run() {
        if (mResponseListener == null) {
            return;
        }
        mResponseListener.onResponse(mResultSet.getResponse());
        if (mResultSet.isSuccess()) {
            mResponseListener.handleSuccess(mResultSet.getResponse(),mResultSet);
        } else {
            Exception error = mResultSet.getError();
            if (error != null) {
                mResponseListener.handleError(error);
            } else {
                mResponseListener.handleFailed(mResultSet);
            }
        }
    }
}
