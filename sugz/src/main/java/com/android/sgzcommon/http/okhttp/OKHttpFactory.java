package com.android.sgzcommon.http.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.android.sgzcommon.http.util.OkHttpCallback;
import com.android.sgzcommon.http.util.OnHttpResponseListener;
import com.android.sgzcommon.http.util.ResultSet;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Created by sgz on 2017/2/24.
 */

public class OKHttpFactory {

    protected Handler mHandler;
    protected OkHttpClient mOkHttpClient;
    private static OKHttpFactory mOKHttpFactory;
    private static String TAG = "OKHttpFactory";

    private OKHttpFactory() {
        mHandler = new Handler(Looper.getMainLooper());
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
    }

    public static OKHttpFactory getInstance() {
        synchronized (OKHttpFactory.class) {
            if (mOKHttpFactory == null) {
                mOKHttpFactory = new OKHttpFactory();
            }
        }
        return mOKHttpFactory;
    }

    public Handler getHandler() {
        return mHandler;
    }

    /**
     * post请求
     *
     * @param url
     * @param data
     * @param responseListener
     */
    public void postEnqueueRequest(String url, Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        RequestBody body = getFormEncodingBuilder(data).build();
        Request request = new Request.Builder().url(url).post(body).build();
        enqueueRequest(request, resultSet, responseListener);
    }

    /**
     * post请求
     *
     * @param url
     * @param data
     */
    public void postRequest(String url, Map<String, String> data, final ResultSet resultSet) {
        RequestBody body = getFormEncodingBuilder(data).build();
        Request request = new Request.Builder().url(url).post(body).build();
        excuteRequest(request, resultSet);

    }

    /**
     * post请求
     *
     * @param url
     * @param data
     * @param responseListener
     */
    public void postEnqueueRequest(String url, Map<String, String> headers, Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        RequestBody body = getFormEncodingBuilder(data).build();
        Request.Builder builder = new Request.Builder().url(url).post(body);
        Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> header = it.next();
            builder.addHeader(header.getKey(), header.getValue());
        }
        enqueueRequest(builder.build(), resultSet, responseListener);
    }

    /**
     * post请求
     *
     * @param url
     * @param data
     */
    public void postRequest(String url, Map<String, String> headers, Map<String, String> data, final ResultSet resultSet) {
        RequestBody body = getFormEncodingBuilder(data).build();
        Request.Builder builder = new Request.Builder().url(url).post(body);
        Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> header = it.next();
            builder.addHeader(header.getKey(), header.getValue());
        }
        excuteRequest(builder.build(), resultSet);

    }

    /**
     * get请求
     *
     * @param url
     * @param responseListener
     */
    public void getEnqueueRequest(String url, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        Request request = new Request.Builder().url(url).build();
        enqueueRequest(request, resultSet, responseListener);
    }

    /**
     * get请求
     *
     * @param url
     */
    public void getRequest(String url, final ResultSet resultSet) {
        Request request = new Request.Builder().url(url).build();
        excuteRequest(request, resultSet);
    }

    /**
     * 异步请求网络
     *
     * @param request
     * @param resultSet
     * @param responseListener
     */
    private void enqueueRequest(final Request request, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        try {
            Call call = mOkHttpClient.newCall(request);
            OkHttpCallback callback = new OkHttpCallback(responseListener) {
                @Override
                public void onCallback(Response response, IOException e, OnHttpResponseListener listener) {
                    try {
                        if (response != null) {
                            String result = response.body().string();
                            Log.d("OKHttpFactory", "run: result = " + result);
                            resultSet.setNetCode(response.code());
                            resultSet.parseResult(result);
                        } else {
                            if (e == null) {
                                throw new IllegalArgumentException("response == null && e == null !");
                            }
                            throw e;
                        }
                    } catch (Exception ex) {
                        e.printStackTrace();
                        resultSet.setMessage(ex.getMessage());
                        resultSet.setError(e);
                    } finally {
                        mHandler.post(new OKResponseRunnable(resultSet, listener));
                    }
                }
            };
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
            resultSet.setMessage(e.getMessage());
            resultSet.setError(e);
            mHandler.post(new OKResponseRunnable(resultSet, responseListener));
        }
    }

    /**
     * 同步请求网络
     *
     * @param request
     * @param resultSet
     */
    private void excuteRequest(final Request request, final ResultSet resultSet) {
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String result = response.body().string();
            Log.d("OKHttpFactory", "run: result = " + result);
            int code = response.code();
            resultSet.setNetCode(code);
            resultSet.parseResult(result);
        } catch (Exception e) {
            resultSet.setMessage(e.getMessage());
            resultSet.setError(e);
            e.printStackTrace();
        }

    }

    private FormEncodingBuilder getFormEncodingBuilder(Map<String, String> data) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("test","1");
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
