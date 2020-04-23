package com.android.sgzcommon.http.okhttp.upload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by sgz on 2017/2/15.
 */

public class OKUploadTask {

    public static final int ON_START = 1;
    public static final int ON_SUCCESS = 2;
    public static final int ON_VALUE = 3;
    public static final int ON_FAIL = 4;
    /**
     * 上传参数名称
     */
    private static final String PARAMS_NAME = "upload";

    private static final String TAG = "OKUploadTask";
    private static OKUploadTask mOKUploadTask;
    private OkHttpClient mOkHttpClient;
    private ExecutorService mExecutorService;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            Response response = (Response) msg.obj;
            OnUploadFileListener listener = response.getListener();
            UploadResultSet result = response.getResult();
            File f = result.getFile();
            if (listener != null) {
                switch (msg.what) {
                    case ON_START:
                        listener.onUploadStart(f);
                        break;
                    case ON_SUCCESS:
                        listener.onUploadSuccess(f, result);
                        break;
                    case ON_VALUE:
                        int value = result.getProgress();
                        listener.onValue(f, value);
                        break;
                    case ON_FAIL:
                        Exception e = result.getError();
                        listener.onUploadFail(f, e);
                        break;
                }
            }
        }
    };

    private OKUploadTask() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(50, TimeUnit.SECONDS);
        mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static OKUploadTask getInstance() {
        synchronized (OKUploadTask.class) {
            if (mOKUploadTask == null) {
                mOKUploadTask = new OKUploadTask();
            }
        }
        return mOKUploadTask;
    }

    /**
     * 上传文件
     */
    public void upLoadFile(final String url, final Map<String, String> data,final UploadResultSet resultSet, final File f, final OnUploadFileListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    resultSet.setFile(f);
                    resultSet.setProgress(0);
                    mHandler.sendMessage(createMessage(ON_START, listener, resultSet));
                    log("**********upload:run:url=" + url);
                    MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
                    String fileName = f.getName();
                    //                    RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), f);
                    RequestBody fileBody = createProgressRequestBody(MediaType.parse("multipart/form-data"),resultSet, f, listener);

                    Headers.Builder hb = new Headers.Builder();
                    hb.add("Content-Disposition", "form-data; name=\"uploadify\";" + "filename=\"" + fileName + "\"");
                    builder.addPart(hb.build(), fileBody);
                    RequestBody requestBody = builder.build();
                    //创建Request
                    Request.Builder rb = new Request.Builder();
                    rb.url(url);
                    if (data != null) {
                        for (String key : data.keySet()) {
                            String value = data.get(key);
                            rb.addHeader(key, value);
                        }
                    }
                    rb.post(requestBody);
                    Call call = mOkHttpClient.newCall(rb.build());
                    log("**********upload:run>>>222");
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            log("IOException=" + e.toString());
                            resultSet.setError(e);
                            mHandler.sendMessage(createMessage(ON_FAIL, listener, resultSet));
                        }

                        @Override
                        public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String result = response.body().string();
                                Log.d(TAG, "onResponse: result = " + result);
                                resultSet.setResponse(result);
                                resultSet.parseResult(result);
                                if (resultSet.isSuccess()) {
                                    resultSet.setProgress(100);
                                    mHandler.sendMessage(createMessage(ON_VALUE, listener, resultSet));
                                    mHandler.sendMessage(createMessage(ON_SUCCESS, listener, resultSet));
                                } else {
                                    mHandler.sendMessage(createMessage(ON_FAIL, listener, resultSet));
                                }
                            } else {
                                int code = response.code();
                                resultSet.setError(new RuntimeException("code == " + code));
                                mHandler.sendMessage(createMessage(ON_FAIL, listener, resultSet));
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    resultSet.setError(e);
                    mHandler.sendMessage(createMessage(ON_FAIL, listener, resultSet));
                }
            }
        });
    }

    private Message createMessage(int what, OnUploadFileListener listener, UploadResultSet result) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = new Response(listener, result);
        return msg;
    }

    private class Response {
        private OnUploadFileListener listener;
        private UploadResultSet result;

        public Response(OnUploadFileListener listener, UploadResultSet result) {
            this.listener = listener;
            this.result = result;
        }

        public OnUploadFileListener getListener() {
            return listener;
        }

        public UploadResultSet getResult() {
            return result;
        }
    }

    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @return
     */
    private RequestBody createProgressRequestBody(final MediaType contentType,final UploadResultSet resultSet, final File file, OnUploadFileListener listener) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    log("contentLength------>" + remaining);
                    long current = 0;
                    int ratioPercent = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        ratioPercent = (int) ((current * 1f / remaining) * 100);
                        if (ratioPercent <= 98) {
                            resultSet.setProgress(ratioPercent);
                            mHandler.sendMessage(createMessage(ON_VALUE, listener, resultSet));
                            log("ratio=" + ratioPercent + "%");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultSet.setError(e);
                    mHandler.sendMessage(createMessage(ON_FAIL, listener, resultSet));
                }
            }
        };
    }

    private void log(String log) {
        Log.d(TAG, log);
    }
}
