package com.android.sgzcommon.http.okhttp.upload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
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

    /**
     * 上传参数名称
     */
    private String name = "uploadify";

    public static final int ON_START = 1;
    public static final int ON_SUCCESS = 2;
    public static final int ON_FAIL = 3;
    public static final int ON_ENTITY_START = 4;
    public static final int ON_ENTITY_SUCCESS = 5;
    public static final int ON_ENTITY_VALUE = 6;
    public static final int ON_ENTITY_FAIL = 7;
    private static final String TAG = "OKUploadTask";

    private static OKUploadTask mOKUploadTask;
    private OkHttpClient mOkHttpClient;
    private ExecutorService mExecutorService;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            UploadResponse uploadResponse;
            UploadProgress uploadProgress;
            OnUploadFileListener listener;
            UploadResultSet resultSet;
            String url;
            List<T> entities;
            T entity;
            switch (msg.what) {
                case ON_START:
                    uploadResponse = (UploadResponse) msg.obj;
                    listener = uploadResponse.getListener();
                    if (listener != null) {
                        listener.onUploadStart(uploadResponse.getUrl());
                    }
                    break;
                case ON_SUCCESS:
                    uploadResponse = (UploadResponse) msg.obj;
                    listener = uploadResponse.getListener();
                    resultSet = uploadResponse.getResultSet();
                    url = uploadResponse.getUrl();
                    entities = uploadResponse.getEntities();
                    if (listener != null) {
                        listener.onUploadSuccess(url, entities, resultSet);
                    }
                    break;
                case ON_FAIL:
                    uploadResponse = (UploadResponse) msg.obj;
                    listener = uploadResponse.getListener();
                    resultSet = uploadResponse.getResultSet();
                    url = uploadResponse.getUrl();
                    entities = uploadResponse.getEntities();
                    if (listener != null) {
                        listener.onUploadFail(url, entities, resultSet.getError());
                    }
                    break;
                case ON_ENTITY_START:
                    uploadProgress = (UploadProgress) msg.obj;
                    listener = uploadProgress.getListener();
                    entity = uploadProgress.getEntity();
                    entity.setProgress(0);
                    entity.setState(UploadEntity.STATE.STATE_UPLOADING);
                    if (listener != null) {
                        listener.onEntityStart(entity);
                    }
                    break;
                case ON_ENTITY_SUCCESS:
                    uploadProgress = (UploadProgress) msg.obj;
                    listener = uploadProgress.getListener();
                    entity = uploadProgress.getEntity();
                    entity.setProgress(100);
                    entity.setState(UploadEntity.STATE.STATE_UPLOAD_SUCCESS);
                    if (listener != null) {
                        listener.onEntitySuccess(entity);
                    }
                    break;
                case ON_ENTITY_VALUE:
                    int value = msg.arg1;
                    uploadProgress = (UploadProgress) msg.obj;
                    listener = uploadProgress.getListener();
                    entity = uploadProgress.getEntity();
                    entity.setProgress(value);
                    entity.setState(UploadEntity.STATE.STATE_UPLOADING);
                    if (listener != null) {
                        listener.onEntityValue(entity, value);
                    }
                    break;
                case ON_ENTITY_FAIL:
                    uploadProgress = (UploadProgress) msg.obj;
                    listener = uploadProgress.getListener();
                    resultSet = uploadProgress.getResultSet();
                    entity = uploadProgress.getEntity();
                    entity.setState(UploadEntity.STATE.STATE_UPLOAD_FAIL);
                    if (listener != null) {
                        listener.onEntityFail(entity, resultSet.getError());
                    }
                    break;
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

    public OKUploadTask setName(String name) {
        this.name = name;
        return mOKUploadTask;
    }

    /**
     * 上传文件，按队列顺序上传
     */
    public void upLoadFileEnqueue(final String url, final List<T> entity, final Map<String, String> data, Map<String, String> headers, final UploadResultSet resultSet, final OnUploadFileListener<T> listener) {
        try {
            final Request request = getRequest(url, entity, data, headers, resultSet, listener);
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    log("IOException=" + e.toString());
                    resultSet.setError(e);
                    mHandler.sendMessage(createResponseMessage(ON_FAIL, url, entity, listener, resultSet));
                }

                @Override
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                    resultSet.setNetCode(response.code());
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        Log.d(TAG, "onResponse: result = " + result);
                        resultSet.setResponse(result);
                        resultSet.parseResult(result);
                        if (resultSet.isSuccess()) {
                            mHandler.sendMessage(createResponseMessage(ON_SUCCESS, url, entity, listener, resultSet));
                        } else {
                            resultSet.setError(new RuntimeException("success is false !"));
                            mHandler.sendMessage(createResponseMessage(ON_FAIL, url, entity, listener, resultSet));
                        }
                    } else {
                        int code = response.code();
                        resultSet.setError(new RuntimeException("netCode == " + code));
                        mHandler.sendMessage(createResponseMessage(ON_FAIL, url, entity, listener, resultSet));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            resultSet.setError(e);
            mHandler.sendMessage(createResponseMessage(ON_FAIL, url, entity, listener, resultSet));
        }

    }

    /**
     * 上传文件
     */
    public void upLoadFile(final String url, final List<T> entity, final Map<String, String> data, Map<String, String> headers, final UploadResultSet resultSet, final OnUploadFileListener<T> listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = getRequest(url, entity, data, headers, resultSet, listener);
                    Call call = mOkHttpClient.newCall(request);
                    Response response = call.execute();
                    resultSet.setNetCode(response.code());
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        Log.d(TAG, "onResponse: result = " + result);
                        resultSet.setResponse(result);
                        resultSet.parseResult(result);
                        if (resultSet.isSuccess()) {
                            mHandler.sendMessage(createResponseMessage(ON_SUCCESS, url, entity, listener, resultSet));
                        } else {
                            mHandler.sendMessage(createResponseMessage(ON_FAIL, url, entity, listener, resultSet));
                        }
                    } else {
                        int code = response.code();
                        resultSet.setError(new RuntimeException("netCode == " + code));
                        mHandler.sendMessage(createResponseMessage(ON_FAIL, url, entity, listener, resultSet));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultSet.setError(e);
                    mHandler.sendMessage(createResponseMessage(ON_FAIL, url, entity, listener, resultSet));
                }
            }
        });
    }

    private Request getRequest(String url, List<T> entities, Map<String, String> data, Map<String, String> headers, UploadResultSet resultSet, OnUploadFileListener<T> listener) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        if (entities != null) {
            for (T entity : entities) {
                final File f = entity.getFile();
                String fileType = getMimeType(f.getName());
                //RequestBody fileBody = createProgressRequestBody(MediaType.parse("multipart/form-data"), resultSet, entity, listener);
                //Headers.Builder fileBuilder = new Headers.Builder();
                //fileBuilder.add("Content-Disposition", "form-data; name=\"" + name + "\";" + "filename=\"" + f.getName() + "\"");
                //builder.addPart(fileBuilder.build(), fileBody);
                RequestBody fileBody = createProgressRequestBody(url, MediaType.parse(fileType), resultSet, entity, listener);
                builder.addFormDataPart(name, f.getName(), fileBody);
            }
        }
        if (data != null) {
            for (String key : data.keySet()) {
                String value = data.get(key);
                builder.addFormDataPart(key, value);
            }
        }
        RequestBody requestBody = builder.build();
        //创建Request
        Request.Builder headerBuilder = new Request.Builder();
        headerBuilder.url(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (!TextUtils.isEmpty(value)) {
                    headerBuilder.addHeader(key, value);
                }
            }
        }
        headerBuilder.post(requestBody);
        return headerBuilder.build();
    }

    /**
     * 获取文件MimeType
     *
     * @param filename
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }

    private Message createResponseMessage(int what, String url, List<T> ts, OnUploadFileListener listener, UploadResultSet result) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = new UploadResponse(url, ts, listener, result);
        return msg;
    }

    private Message createProgressMessage(int what, String url, T t, OnUploadFileListener listener, UploadResultSet result) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = new UploadProgress(url, t, listener, result);
        return msg;
    }

    private class UploadResponse {
        private String url;
        private List<T> entities;
        private OnUploadFileListener listener;
        private UploadResultSet resultSet;

        public UploadResponse(String url, List<T> ts, OnUploadFileListener listener, UploadResultSet resultSet) {
            this.url = url;
            this.entities = ts;
            this.listener = listener;
            this.resultSet = resultSet;
        }

        public List<T> getEntities() {
            return entities;
        }

        public OnUploadFileListener getListener() {
            return listener;
        }

        public UploadResultSet getResultSet() {
            return resultSet;
        }

        public String getUrl() {
            return url;
        }
    }

    private class UploadProgress {
        private String url;
        private T entity;
        private OnUploadFileListener listener;
        private UploadResultSet resultSet;

        public UploadProgress(String url, T t, OnUploadFileListener listener, UploadResultSet resultSet) {
            this.url = url;
            this.entity = t;
            this.listener = listener;
            this.resultSet = resultSet;
        }

        public T getEntity() {
            return entity;
        }

        public OnUploadFileListener getListener() {
            return listener;
        }

        public UploadResultSet getResultSet() {
            return resultSet;
        }

        public String getUrl() {
            return url;
        }
    }

    /**
     * 创建带进度的RequestBody
     *
     * @param url
     * @param contentType MediaType
     * @param entity      准备上传的entity
     * @return
     */
    private RequestBody createProgressRequestBody(String url, final MediaType contentType, final UploadResultSet resultSet, T entity, OnUploadFileListener listener) {
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
                    mHandler.sendMessage(createProgressMessage(ON_ENTITY_START, url, entity, listener, resultSet));
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
                        Message msg = createProgressMessage(ON_ENTITY_VALUE, url, entity, listener, resultSet);
                        msg.arg1 = ratioPercent;
                        mHandler.sendMessage(msg);
                        log("createProgressRequestBody : " + entity.getFile().getName() + "；ratio == " + ratioPercent + "%");
                    }
                    mHandler.sendMessage(createProgressMessage(ON_ENTITY_SUCCESS, url, entity, listener, resultSet));
                } catch (Exception e) {
                    e.printStackTrace();
                    resultSet.setError(e);
                    mHandler.sendMessage(createProgressMessage(ON_ENTITY_FAIL, url, entity, listener, resultSet));
                }
            }
        };
    }

    private void log(String log) {
        Log.d(TAG, log);
    }
}
