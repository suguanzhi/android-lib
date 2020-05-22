package com.android.sgzcommon.http.okhttp.upload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
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

public class OKUploadTask<T extends UploadEntity> {

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
            UploadResponse uploadResponse = (UploadResponse) msg.obj;
            OnUploadFileListener listener = uploadResponse.getListener();
            UploadResultSet result = uploadResponse.getResult();
            T entity = uploadResponse.getEntity();
            if (listener != null) {
                switch (msg.what) {
                    case ON_START:
                        entity.setProgress(0);
                        entity.setState(UploadEntity.STATE.STATE_START);
                        listener.onUploadStart(entity);
                        break;
                    case ON_SUCCESS:
                        entity.setState(UploadEntity.STATE.STATE_SUCCESS);
                        listener.onUploadSuccess(entity, result);
                        break;
                    case ON_VALUE:
                        int value = entity.getProgress();
                        entity.setProgress(value);
                        entity.setState(UploadEntity.STATE.STATE_UPLOADING);
                        listener.onValue(entity, value);
                        break;
                    case ON_FAIL:
                        entity.setState(UploadEntity.STATE.STATE_FAIL);
                        Exception e = result.getError();
                        listener.onUploadFail(entity, e);
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
    public void upLoadFile(final String url, final T entity, final Map<String, String> data, final UploadResultSet resultSet, final OnUploadFileListener<T> listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log("upload:run:url=" + url);
                    if (entity == null) {
                        resultSet.setError(new RuntimeException("UploadEntity == null"));
                        mHandler.sendMessage(createMessage(ON_FAIL, entity, listener, resultSet));
                        return;
                    }
                    final File f = entity.getFile();
                    if (f == null || !f.exists()) {
                        resultSet.setError(new RuntimeException("file == null || file is not exists."));
                        mHandler.sendMessage(createMessage(ON_FAIL, entity, listener, resultSet));
                        return;
                    }
                    mHandler.sendMessage(createMessage(ON_START, entity, listener, resultSet));
                    MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
                    RequestBody fileBody = createProgressRequestBody(MediaType.parse("multipart/form-data"), resultSet, entity, listener);

                    Headers.Builder hb = new Headers.Builder();
                    hb.add("Content-Disposition", "form-data; name=\"uploadify\";" + "filename=\"" + f.getName() + "\"");
                    builder.addPart(hb.build(), fileBody);
                    RequestBody requestBody = builder.build();
                    //创建Request
                    Request.Builder rb = new Request.Builder();
                    rb.url(url);
                    if (data != null) {
                        for (String key : data.keySet()) {
                            String value = data.get(key);
                            if (!TextUtils.isEmpty(value)) {
                                rb.addHeader(key, value);
                            }
                        }
                    }
                    rb.post(requestBody);
                    Call call = mOkHttpClient.newCall(rb.build());
                    log("upload:run>>>222");
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            log("IOException=" + e.toString());
                            resultSet.setError(e);
                            mHandler.sendMessage(createMessage(ON_FAIL, entity, listener, resultSet));
                        }

                        @Override
                        public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String result = response.body().string();
                                Log.d(TAG, "onResponse: result = " + result);
                                resultSet.setResponse(result);
                                resultSet.parseResult(result);
                                if (resultSet.isSuccess()) {
                                    entity.setProgress(100);
                                    mHandler.sendMessage(createMessage(ON_VALUE, entity, listener, resultSet));
                                    mHandler.sendMessage(createMessage(ON_SUCCESS, entity, listener, resultSet));
                                } else {
                                    mHandler.sendMessage(createMessage(ON_FAIL, entity, listener, resultSet));
                                }
                            } else {
                                int code = response.code();
                                resultSet.setError(new RuntimeException("code == " + code));
                                mHandler.sendMessage(createMessage(ON_FAIL, entity, listener, resultSet));
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    resultSet.setError(e);
                    mHandler.sendMessage(createMessage(ON_FAIL, entity, listener, resultSet));
                }
            }
        });
    }

    private Message createMessage(int what, T entity, OnUploadFileListener listener, UploadResultSet result) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = new UploadResponse(entity, listener, result);
        return msg;
    }

    private class UploadResponse {
        private T entity;
        private OnUploadFileListener listener;
        private UploadResultSet result;

        public UploadResponse(T entity, OnUploadFileListener listener, UploadResultSet result) {
            this.entity = entity;
            this.listener = listener;
            this.result = result;
        }

        public T getEntity() {
            return entity;
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
     * @param entity      准备上传的entity
     * @return
     */
    private RequestBody createProgressRequestBody(final MediaType contentType, final UploadResultSet resultSet, T entity, OnUploadFileListener listener) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                File file = entity.getFile();
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                try {
                    File file = entity.getFile();
                    Source source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    log("createProgressRequestBody : contentLength == " + remaining);
                    long current = 0;
                    int ratioPercent = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        ratioPercent = (int) ((current * 1f / remaining) * 100);
                        if (ratioPercent <= 98) {
                            mHandler.sendMessage(createMessage(ON_VALUE, entity, listener, resultSet));
                            log("createProgressRequestBody : ratio == " + ratioPercent + "%");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultSet.setError(e);
                    mHandler.sendMessage(createMessage(ON_FAIL, entity, listener, resultSet));
                }
            }
        };
    }

    private void log(String log) {
        Log.d(TAG, log);
    }
}
