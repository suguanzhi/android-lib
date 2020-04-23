package com.android.sgzcommon.http.okhttp;

import android.app.Dialog;
import android.content.Context;

import com.android.sgzcommon.http.util.OnHttpResponseListener;
import com.android.sgzcommon.http.util.ResultSet;

import java.util.Map;

/**
 * Created by sgz on 2017/5/5.
 */

public class OKHttpDialog extends Dialog {

    protected Context mContext;

    public OKHttpDialog(Context context) {
        super(context);
        mContext = context;
    }

    public OKHttpDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected OKHttpDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    protected void postRequest(final String url, final Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        OKHttpFactory.getInstance().postRequest(url, data,resultSet, responseListener);
    }

    protected void getRequest(final String url, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        OKHttpFactory.getInstance().getRequest(url,resultSet, responseListener);
    }
}
