package com.android.sgzcommon.logutil;

import android.content.Context;

import com.android.sgzcommon.logutil.jlog.JLog;
import com.android.sgzcommon.logutil.jlog.Settings;
import com.android.sgzcommon.logutil.jlog.constant.LogLevel;
import com.android.sgzcommon.logutil.jlog.constant.LogSegment;
import com.android.sugz.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import static com.android.sgzcommon.logutil.jlog.JLog.init;

/**
 * Created by sgz on 2016/3/15.
 */
public class SLogUtil {

    /**
     * @param context
     * @param logDirName log的根文件名(xxx或者xxx/yyy/...)，根路径是：Environment.getExternalStorageDirectory() + /logDirName
     */
    public static void initLog(Context context, String logDirName) {
        List<LogLevel> logLevels = new ArrayList<LogLevel>();
        logLevels.add(LogLevel.ERROR);
        logLevels.add(LogLevel.INFO);
        logLevels.add(LogLevel.JSON);
        logLevels.add(LogLevel.WARN);
        init(context).setDebug(BuildConfig.DEBUG).setLogDir(logDirName).writeToFile(true).setLogLevelsForFile(logLevels).setLogSegment(LogSegment.ONE_HOUR).setTimeFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param context
     * @param logDirName log的根文件名(xxx或者xxx/yyy/...)，根路径是：Environment.getExternalStorageDirectory() + /logDirName
     * @param logSegment
     */
    public static void initLog(Context context, String logDirName, LogSegment logSegment) {
        initLog(context, logDirName, logSegment, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param context
     * @param logDirName log的根文件名(xxx或者xxx/yyy/...)，根路径是：Environment.getExternalStorageDirectory() + /logDirName
     * @param logSegment
     * @param timeFormat 日志文件名，以时间命名格式
     */
    public static void initLog(Context context, String logDirName, LogSegment logSegment, String timeFormat) {
        List<LogLevel> logLevels = new ArrayList<LogLevel>();
        logLevels.add(LogLevel.ERROR);
        logLevels.add(LogLevel.INFO);
        logLevels.add(LogLevel.JSON);
        logLevels.add(LogLevel.WARN);
        init(context).setDebug(BuildConfig.DEBUG).setLogDir(logDirName).writeToFile(true).setLogLevelsForFile(logLevels).setLogSegment(logSegment).setTimeFormat(timeFormat);
    }

    public static void i(String dirName, String log) {
        Settings settings = JLog.getSettings().setLogDir(dirName);
        JLog.setSettings(settings);
        JLog.i(log);
    }
}
