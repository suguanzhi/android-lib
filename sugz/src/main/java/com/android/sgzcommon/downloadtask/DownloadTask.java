package com.android.sgzcommon.downloadtask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sgz on 2016/1/a12.
 * changed by sgz on 2019/1/5
 */
public class DownloadTask {

    private long mFinished;
    public static boolean isPause;
    private static MyHandler mHandler;
    private ThreadDAO mDao;
    private ExecutorService mExecutorService;
    private static DownloadTask sDownloadTask;
    private static OnDownloadListener mListener;
    private static final int DOWNLOAD_START = 304;
    private static final int DOWNLOAD_SUCCESS = 179;
    private static final int DOWNLOAD_PAUSE = 27;
    private static final int DOWNLOAD_PROGRESS = 990;
    private static final int DOWNLOAD_FAIL = 637;

    private DownloadTask(Context context) {
        mDao = new ThreadDAOImpl(context);
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    public static DownloadTask getInstance(Context context) {
        synchronized (DownloadTask.class) {
            if (sDownloadTask == null) {
                sDownloadTask = new DownloadTask(context);
            }
        }
        if (mHandler == null) {
            mHandler = new MyHandler();
        }
        return sDownloadTask;
    }

    public void shutdown() {
        mExecutorService.shutdownNow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }

    public void download(final String downloadUrl, final String savePath, final OnDownloadListener listener) {
        Log.d("DownloadTask", "download: ");
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i("DownloadTask", "run.........");
                mFinished = 0;
                isPause = false;
                mListener = listener;
                Exception exception = null;
                HttpURLConnection coon1 = null;
                RandomAccessFile raf1 = null;
                InputStream input = null;
                ThreadInfo threadInfo = null;
                final DownloadInfo downloadInfo = new DownloadInfo(downloadUrl, savePath);
                try {
                    Log.i("DownloadTask", "url=" + downloadInfo.getUrl());
                    URL url = new URL(downloadInfo.getUrl());
                    coon1 = (HttpURLConnection) url.openConnection();
                    coon1.setConnectTimeout(10 * 1000);
                    coon1.setReadTimeout(10 * 1000);
                    coon1.setRequestProperty("Accept-Encoding", "identity");
                    coon1.setRequestMethod("GET");
                    coon1.connect();
                    int length = -1;
                    Log.i("DownloadTask", "ResponseCode=" + coon1.getResponseCode());
                    if (coon1.getResponseCode() == HttpStatus.SC_OK) {
                        length = coon1.getContentLength();
                        Log.i("DownloadTask", "length=" + length);
                        if (length < 0) {
                            exception = new RuntimeException("HttpURLConnection contentLength < 0");
                        } else {
                            File file = new File(downloadInfo.getSavePath());
                            Log.i("DownloadTask", "savePath=" + downloadInfo.getSavePath());
                            File dir = new File(file.getParent());
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            Log.i("DownloadTask", "**************");
                            raf1 = new RandomAccessFile(file, "rw");
                            Log.i("DownloadTask", "*************11");
                            raf1.setLength(length);
                            Log.i("DownloadTask", "*************22");
                            downloadInfo.setLength(length);
                            Log.i("DownloadTask", "*************33");
                            List<ThreadInfo> threadInfos = mDao.getThreads(downloadInfo.getUrl());
                            Log.i("DownloadTask", "*************44:" + threadInfos.size());
                            if (threadInfos.size() == 0) {
                                threadInfo = new ThreadInfo(0, 0, downloadInfo.getLength(), 0, downloadInfo.getUrl());
                            } else {
                                threadInfo = threadInfos.get(0);
                            }
                            if (!mDao.isExists(threadInfo.getUrl(), threadInfo.getId())) {
                                mDao.insertThread(threadInfo);
                            }
                        }
                    } else {
                        exception = new RuntimeException("responseCode = " + coon1.getResponseCode());
                    }
                } catch (IOException e) {
                    Log.i("DownloadTask", "Exception:" + Log.getStackTraceString(e));
                    exception = e;
                    e.printStackTrace();
                } finally {
                    if (exception == null) {
                        exception = startDownload(threadInfo, downloadInfo);
                    }
                    //下载完成移除该任务
                    if (exception == null) {
                        //下载完毕
                        sendMessage(DOWNLOAD_SUCCESS, downloadInfo, null);
                    } else {
                        sendMessage(DOWNLOAD_FAIL, downloadInfo, exception);
                    }
                    try {
                        if (input != null) {
                            input.close();
                        }
                        if (raf1 != null) {
                            raf1.close();
                        }
                        if (coon1 != null) {
                            coon1.disconnect();
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    Log.i("DownloadTask", "run done");
                }
            }
        });
    }

    /**
     * 开始下载资源
     *
     * @param threadInfo
     * @return
     */
    private Exception startDownload(ThreadInfo threadInfo, DownloadInfo tarInfo) {
        isPause = false;
        Exception exception = null;
        HttpURLConnection coon2 = null;
        RandomAccessFile raf2 = null;
        InputStream input = null;
        try {
            URL url2 = new URL(threadInfo.getUrl());
            Log.i("DownloadTask", "url=" + threadInfo.getUrl());
            coon2 = (HttpURLConnection) url2.openConnection();
            coon2.setConnectTimeout(20 * 1000);
            coon2.setReadTimeout(20 * 1000);
            coon2.setRequestMethod("GET");
            Log.i("DownloadTask", ">>>>>>>>>>>>>>>>>>1");
            long start = threadInfo.getStart() + threadInfo.getFinished();
            coon2.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());
            coon2.connect();
            Log.i("DownloadTask", ">>>>>>>>>>>>>>>>>>2");
            File file2 = new File(tarInfo.getSavePath());
            raf2 = new RandomAccessFile(file2, "rw");
            Log.i("DownloadTask", "start1=" + threadInfo.getStart());
            Log.i("DownloadTask", "finished=" + threadInfo.getFinished());
            Log.i("DownloadTask", "start2=" + start);
            raf2.seek(start);
            Log.i("DownloadTask", ">>>>>>>>>>>>>>>>>>3");
            mFinished += threadInfo.getFinished();
            Log.i("DownloadTask", ">>>>>>>>>>>>>>>>>>4:" + mFinished);
            sendMessage(DOWNLOAD_START, tarInfo, null);
            Log.i("DownloadTask", "getResponseCode=" + coon2.getResponseCode());
            if (coon2.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
                input = coon2.getInputStream();
                byte[] buffer = new byte[1024 * 4];
                int len = -1;
                int persentValue1;
                int persentValue2 = 0;
                while ((len = input.read(buffer)) != -1) {
                    Log.i("DownloadTask", ">>>>>>>>>>>>>>>>>>read:len=" + len);
                    raf2.write(buffer, 0, len);
                    persentValue1 = (int) ((mFinished * 100) / tarInfo.getLength());
                    if (persentValue1 > persentValue2 | persentValue1 == 100) {
                        persentValue2 = persentValue1;
                        Message msg = new Message();
                        msg.what = DOWNLOAD_PROGRESS;
                        msg.arg1 = persentValue1;
                        mHandler.sendMessage(msg);
                    }
                    mFinished += len;
                    if (isPause) {
                        mDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), mFinished);
                        exception = new RuntimeException("download pause!");
                        break;
                    }
                }
                if (!isPause) {
                    mDao.deleteThread(threadInfo.getUrl(), threadInfo.getId());
                }
            } else {
                mDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), mFinished);
                exception = new RuntimeException("responseCode = " + coon2.getResponseCode());
            }
        } catch (Exception e) {
            Log.i("DownloadTask", "Exception=mFinished=" + mFinished);
            exception = e;
            mDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), mFinished);
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (raf2 != null) {
                    raf2.close();
                }
                if (coon2 != null) {
                    coon2.disconnect();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return exception;
        }
    }

    private void sendMessage(int what, DownloadInfo downloadInfo, Exception e) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = new DownloadResult(downloadInfo, e);
        mHandler.sendMessage(msg);
    }

    public static class DownloadInfo {

        private long length;
        private String url;
        private String savePath;

        public DownloadInfo() {

        }

        public DownloadInfo(String url, String savePath) {
            this.url = url;
            this.savePath = savePath;
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSavePath() {
            return savePath;
        }

        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }
    }

    public class DownloadResult {
        private DownloadInfo downloadInfo;
        private Exception exception;

        public DownloadResult(DownloadInfo downloadInfo, Exception exception) {
            this.downloadInfo = downloadInfo;
            this.exception = exception;
        }

        public DownloadInfo getDownloadInfo() {
            return downloadInfo;
        }

        public Exception getException() {
            return exception;
        }
    }

    private static class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (mListener == null) {
                return;
            }
            DownloadResult result = (DownloadResult) msg.obj;
            switch (msg.what) {
                case DOWNLOAD_START:
                    mListener.onDownloadStart(result.getDownloadInfo());
                    break;
                case DOWNLOAD_SUCCESS:
                    mListener.onDownloadSuccess(result.getDownloadInfo());
                    break;
                case DOWNLOAD_PAUSE:
                    mListener.onDownloadPause(result.getDownloadInfo());
                    break;
                case DOWNLOAD_PROGRESS:
                    int progress = msg.arg1;
                    mListener.onDownlaodProgress(progress);
                    break;
                case DOWNLOAD_FAIL:
                    mListener.onDownloadFail(result.getDownloadInfo(), result.getException());
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
