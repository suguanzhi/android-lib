package com.android.sgzcommon.logutil.applog; /**
 * Created by tongseng on 2016/7/19.
 */

import android.content.Context;
import android.os.Environment;

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

    private static LogcatHelper INSTANCE = null;
    private static String PATH_LOGCAT;
    private LogDumper mLogDumper = null;

    /**
     * 初始化目录
     */
    public void init(Context context, String dirName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirName;
        } else {
            // 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT = context.getFilesDir().getAbsolutePath() + File.separator + dirName;
        }
        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static LogcatHelper getInstance(Context context, String dirName) {
        synchronized (LogcatHelper.class) {
            if (INSTANCE == null) {
                INSTANCE = new LogcatHelper(context, dirName);
            }
        }
        return INSTANCE;
    }

    private LogcatHelper(Context context, String dirName) {
        init(context, dirName);
    }

    public void start() {
        if (mLogDumper == null)
            mLogDumper = new LogDumper(String.valueOf(PATH_LOGCAT));
        mLogDumper.start();
    }

    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    private class LogDumper extends Thread {

        private String mDir;
        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        private String[] cmds = {"logcat", "-v", "time", "*:D"};
        private FileOutputStream out = null;

        public LogDumper(String dir) {
            mDir = dir;
        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
                String fileName = null;
                String line = null;
                while (mRunning) {
                    if (!LogcatDate.getFileName().equals(fileName)) {
                        fileName = LogcatDate.getFileName();
                        out = new FileOutputStream(new File(mDir, "Log-" + fileName + ".log"), true);
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
}  