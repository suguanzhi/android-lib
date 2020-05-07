package com.android.sgzcommon.logutil.applog; /**
 * Created by tongseng on 2016/7/19.
 */

import android.content.Context;
import android.os.Environment;

import com.android.sgzcommon.utils.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * log日志统计保存
 *
 * @author way
 */

public class LogcatHelper {

    private int mFileLimit;
    private String mLogDir;
    private static LogcatHelper sLogcatHelper = null;
    private LogDumpThread mLogDumpThread = null;

    private LogcatHelper(Context context, String dirName) {
        init(context, dirName);
    }

    /**
     * 初始化目录
     */
    public void init(Context context, String dirName) {
        init(context, dirName, 2);
    }

    /**
     * 初始化目录
     */
    public void init(Context context, String dirName, int limit) {
        mFileLimit = limit;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            mLogDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirName;
        } else {
            mLogDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + File.separator + dirName;
        }
        try {
            File file = new File(mLogDir);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LogcatHelper getInstance(Context context, String dirName) {
        synchronized (LogcatHelper.class) {
            if (sLogcatHelper == null) {
                sLogcatHelper = new LogcatHelper(context, dirName);
            }
        }
        return sLogcatHelper;
    }

    public void start() {
        if (mLogDumpThread == null) {
            mLogDumpThread = new LogDumpThread();
        }
        mLogDumpThread.start();
    }

    public void stop() {
        if (mLogDumpThread != null) {
            mLogDumpThread.stopLogs();
            mLogDumpThread = null;
        }
    }

    private class LogDumpThread extends Thread {

        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        private String[] cmds = {"logcat", "-v", "time", "*:D"};
        private FileOutputStream out = null;

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
                String line = null;
                String fileName = null;
                while (mRunning) {
                    if (!LogcatDate.getFileName().equals(fileName)) {
                        fileName = LogcatDate.getFileName();
                        out = new FileOutputStream(new File(mLogDir, fileName + ".log"), true);
                        new DeleteLogFileThread().start();
                    }
                    if (!mRunning) {
                        break;
                    }
                    if ((line = mReader.readLine()) != null) {
                        if (line.length() == 0) {
                            continue;
                        }
                        if (out != null) {
                            out.write((line + "\n").getBytes());
                            out.flush();
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    out = null;
                }

            }

        }
    }

    private class DeleteLogFileThread extends Thread {

        @Override
        public void run() {
            File logDir = new File(mLogDir);
            if (logDir.exists()) {
                File[] files = logDir.listFiles();
                if (files != null) {
                    FileUtil.fileSort(files);
                    int len = files.length;
                    if (len > mFileLimit) {
                        for (int i = 0; i < len - mFileLimit; i++) {
                            FileUtil.deleteFile(files[i]);
                        }
                    }
                }
            }
        }
    }
}  