package com.android.sgzcommon.http.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.android.sgzcommon.http.util.OnHttpResponseListener;
import com.android.sgzcommon.http.util.ResultSet;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by sgz on 2017/2/24.
 */

public class OKHttpFactory {

    private OkHttpClient mOkHttpClient;
    private Executor mExecutor;
    private Handler mHandler;
    private static OKHttpFactory mOKHttpFactory;
    private static String TAG = "OKHttpFactory";

    private OKHttpFactory() {
        mHandler = new Handler(Looper.getMainLooper());
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public static OKHttpFactory getInstance() {
        synchronized (OKHttpFactory.class) {
            if (mOKHttpFactory == null) {
                mOKHttpFactory = new OKHttpFactory();
            }
        }
        return mOKHttpFactory;
    }

    /**
     * post请求
     *
     * @param url
     * @param data
     * @param responseListener
     */
    public void postRequest(String url, Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        RequestBody body = getFormEncodingBuilder(data).build();
        try {
            Request request = new Request.Builder().url(url).post(body).build();
            excuteRequest(request, resultSet, responseListener);
        } catch (Exception e) {
            if (responseListener != null) {
                responseListener.handleError(e);
            }
            e.printStackTrace();
        }

    }

    /**
     * post请求
     *
     * @param url
     * @param data
     * @param responseListener
     */
    public void postRequest(String url, Map<String, String> headers, Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        RequestBody body = getFormEncodingBuilder(data).build();
        try {
            Request.Builder builder = new Request.Builder().url(url).post(body);
            Iterator<Map.Entry<String,String>> it= headers.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String,String> header = it.next();
                builder.addHeader(header.getKey(),header.getValue());
            }
             excuteRequest(builder.build(), resultSet, responseListener);
        } catch (Exception e) {
            if (responseListener != null) {
                responseListener.handleError(e);
            }
            e.printStackTrace();
        }

    }

    /**
     * get请求
     *
     * @param url
     * @param responseListener
     */
    public void getRequest(String url, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        Request request = new Request.Builder().url(url).build();
        excuteRequest(request, resultSet, responseListener);
    }

    private void excuteRequest(final Request request, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    String result = response.body().string();
                    Log.d("OKHttpFactory", "run: result = " + result);
                    int code = response.code();
                    resultSet.setNetCode(code);
                    resultSet.parseResult(result);
                } catch (Exception e) {
                    resultSet.setError(e);
                    e.printStackTrace();
                } finally {
                    mHandler.post(new OKResponseRunnable(resultSet, responseListener));
                }
            }
        });
    }

    private FormEncodingBuilder getFormEncodingBuilder(Map<String, String> data) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("test", "123");
        if (data != null) {
            Set<String> set = data.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = data.get(key);
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    builder.add(key, value);
                }
            }
        }
        return builder;
    }
}
