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
import java.util.Map;
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
        enqueueRequest(getPostRequest(url, null,data), resultSet, responseListener);
    }

    /**
     * post请求
     *
     * @param url
     * @param data
     */
    public void postRequest(String url, Map<String, String> data, final ResultSet resultSet) {
        excuteRequest(getPostRequest(url,null, data), resultSet);

    }

    /**
     * post请求
     *
     * @param url
     * @param data
     * @param responseListener
     */
    public void postEnqueueRequest(String url, Map<String, String> headers, Map<String, String> data, final ResultSet resultSet, final OnHttpResponseListener responseListener) {
        enqueueRequest(getPostRequest(url,headers,data), resultSet, responseListener);
    }

    /**
     * post请求
     *
     * @param url
     * @param data
     */
    public void postRequest(String url, Map<String, String> headers, Map<String, String> data, final ResultSet resultSet) {
        excuteRequest(getPostRequest(url,headers,data), resultSet);

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
    public void getPostRequest(String url, final ResultSet resultSet) {
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

    /**
     *
     * @param url
     * @param headers
     * @param data
     * @return
     */
    private Request getPostRequest(String url, Map<String, String> headers, Map<String, String> data) {
        Request request;
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    requestBuilder.addHeader(key, value);
                }
            }
        }
        if (data == null || data.size() == 0) {
            request = requestBuilder.url(url).post(RequestBody.create(null, "")).build();
        } else {
            FormEncodingBuilder bodyBuilder = new FormEncodingBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    bodyBuilder.add(key, value);
                }
            }
            RequestBody body = bodyBuilder.build();
            request = requestBuilder.url(url).post(body).build();
        }
        return request;
    }
}
