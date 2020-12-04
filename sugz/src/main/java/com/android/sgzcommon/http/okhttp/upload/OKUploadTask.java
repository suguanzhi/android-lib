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
            UploadResponse uploadResponse = (UploadResponse) msg.obj;
            OnUploadFileListener listener = uploadResponse.getListener();
            UploadResultSet resultSet = uploadResponse.getResultSet();
            String url = uploadResponse.getUrl();
            UploadProgress uploadProgress;
            UploadEntityProgress uploadEntityProgress;
            T entity;
            List<T> entities;
            switch (msg.what) {
                case ON_START:
                    uploadProgress = (UploadProgress) uploadResponse;
                    if (listener != null) {
                        listener.onUploadStart(uploadProgress.getUrl());
                    }
                    break;
                case ON_SUCCESS:
                    uploadProgress = (UploadProgress) uploadResponse;
                    entities = uploadProgress.getEntities();
                    if (listener != null) {
                        listener.onUploadSuccess(url, entities, resultSet);
                    }
                    break;
                case ON_FAIL:
                    uploadProgress = (UploadProgress) uploadResponse;
                    entities = uploadProgress.getEntities();
                    if (listener != null) {
                        listener.onUploadFail(url, entities, resultSet.getError());
                    }
                    break;
                case ON_ENTITY_START:
                    uploadEntityProgress = (UploadEntityProgress) uploadResponse;
                    entity = uploadEntityProgress.getEntity();
                    entity.setProgress(0);
                    entity.setState(UploadEntity.STATE.STATE_UPLOADING);
                    if (listener != null) {
                        listener.onEntityStart(entity);
                    }
                    break;
                case ON_ENTITY_SUCCESS:
                    uploadEntityProgress = (UploadEntityProgress) uploadResponse;
                    entity = uploadEntityProgress.getEntity();
                    entity.setProgress(100);
                    entity.setState(UploadEntity.STATE.STATE_UPLOAD_SUCCESS);
                    if (listener != null) {
                        listener.onEntitySuccess(entity);
                    }
                    break;
                case ON_ENTITY_VALUE:
                    int value = msg.arg1;
                    uploadEntityProgress = (UploadEntityProgress) uploadResponse;
                    entity = uploadEntityProgress.getEntity();
                    entity.setProgress(value);
                    entity.setState(UploadEntity.STATE.STATE_UPLOADING);
                    if (listener != null) {
                        listener.onEntityValue(entity, value);
                    }
                    break;
                case ON_ENTITY_FAIL:
                    uploadEntityProgress = (UploadEntityProgress) uploadResponse;
                    entity = uploadEntityProgress.getEntity();
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
    public void upLoadFileEnqueue(final String url, final List<T> ts, final Map<String, String> data, Map<String, String> headers, final UploadResultSet resultSet, final OnUploadFileListener<T> listener) {
        try {
            mHandler.sendMessage(createResponseMessage(ON_START, url, ts, listener, resultSet));
            final Request request = getRequest(url, ts, data, headers, resultSet, listener);
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                    log("IOException=" + e.toString());
                    resultSet.setError(e);
                    mHandler.sendMessage(createResponseMessage(ON_FAIL, url, ts, listener, resultSet));
                }

                @Override
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                    int netCode = response.code();
                    resultSet.setNetCode(netCode);
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        Log.d(TAG, "onResponse: result = " + result);
                        resultSet.setResponse(result);
                        resultSet.parseResult(result);
                        if (resultSet.isSuccess()) {
                            mHandler.sendMessage(createResponseMessage(ON_SUCCESS, url, ts, listener, resultSet));
                        } else {
                            throw new IOException("json parse failure !");
                        }
                    } else {
                        throw new IOException("netCode == " + netCode);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            resultSet.setError(e);
            mHandler.sendMessage(createResponseMessage(ON_FAIL, url, ts, listener, resultSet));
        }

    }

    /**
     * 上传文件
     */
    public void upLoadFile(final String url, final List<T> ts, final Map<String, String> data, Map<String, String> headers, final UploadResultSet resultSet, final OnUploadFileListener<T> listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mHandler.sendMessage(createResponseMessage(ON_START, url, ts, listener, resultSet));
                    Request request = getRequest(url, ts, data, headers, resultSet, listener);
                    Call call = mOkHttpClient.newCall(request);
                    Response response = call.execute();
                    int netCode = response.code();
                    resultSet.setNetCode(response.code());
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        Log.d(TAG, "onResponse: result = " + result);
                        resultSet.setResponse(result);
                        resultSet.parseResult(result);
                        if (resultSet.isSuccess()) {
                            mHandler.sendMessage(createResponseMessage(ON_SUCCESS, url, ts, listener, resultSet));
                        } else {
                            throw new IOException("json parse failure !");
                        }
                    } else {
                        throw new IOException("netCode == " + netCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultSet.setError(e);
                    mHandler.sendMessage(createResponseMessage(ON_FAIL, url, ts, listener, resultSet));
                }
            }
        });
    }

    /**
     * @param url
     * @param entities
     * @param data
     * @param headers
     * @param resultSet
     * @param listener
     * @return
     */
    private Request getRequest(String url, List<T> entities, Map<String, String> data, Map<String, String> headers, UploadResultSet resultSet, OnUploadFileListener<T> listener) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        if (entities != null) {
            Log.d("OKUploadTask", "getRequest: entities.size == " + entities.size());
            for (T entity : entities) {
                final File f = entity.getFile();
                Log.d("OKUploadTask", "getRequest: f.name == " + f.getName());
                String mimeType = getMimeType(f.getName());
                Log.d("OKUploadTask", "getRequest: mimeType == " + mimeType);
                //RequestBody fileBody = createProgressRequestBody(MediaType.parse("multipart/form-data"), resultSet, entity, listener);
                //Headers.Builder fileBuilder = new Headers.Builder();
                //fileBuilder.add("Content-Disposition", "form-data; name=\"" + name + "\";" + "filename=\"" + f.getName() + "\"");
                //builder.addPart(fileBuilder.build(), fileBody);
                RequestBody fileBody = createProgressRequestBody(url, MediaType.parse(mimeType), resultSet, entity, listener);
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
        msg.obj = new UploadProgress(url, ts, listener, result);
        return msg;
    }

    private Message createProgressMessage(int what, String url, T t, OnUploadFileListener listener, UploadResultSet result) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = new UploadEntityProgress(url, t, listener, result);
        return msg;
    }

    private class UploadResponse {
        private String url;
        private OnUploadFileListener listener;
        private UploadResultSet resultSet;

        public UploadResponse(String url, OnUploadFileListener listener, UploadResultSet resultSet) {
            this.url = url;
            this.listener = listener;
            this.resultSet = resultSet;
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

    private class UploadProgress extends UploadResponse {
        private List<T> entities;

        public UploadProgress(String url, List<T> ts, OnUploadFileListener listener, UploadResultSet resultSet) {
            super(url, listener, resultSet);
            this.entities = ts;
        }

        public List<T> getEntities() {
            return entities;
        }
    }

    private class UploadEntityProgress extends UploadResponse {
        private T entity;

        public UploadEntityProgress(String url, T t, OnUploadFileListener listener, UploadResultSet resultSet) {
            super(url, listener, resultSet);
            this.entity = t;
        }

        public T getEntity() {
            return entity;
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
                    throw e;
                }
            }
        };
    }

    private void log(String log) {
        Log.d(TAG, log);
    }
}
