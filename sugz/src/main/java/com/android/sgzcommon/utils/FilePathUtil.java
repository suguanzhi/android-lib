package com.android.sgzcommon.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by sgz on 2019/5/23 0023.
 */
public class FilePathUtil {

    public static File getSDRootDir(Context context) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getPackageName();
        File rootDir = new File(path);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir;
    }

    public static File getAppPictureDir(Context context) {
        File rootDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir;
    }

    public static File getAppDownloadDir(Context context) {
        File rootDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir;
    }

    public static File getAppDcimDir(Context context) {
        File rootDir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir;
    }

    public static File getAppDocumentDir(Context context) {
        File rootDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir;
    }

    public static File getAppMovieDir(Context context) {
        File rootDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir;
    }

    public static File getAppMusicDir(Context context) {
        File rootDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir;
    }

    public static File getAppScrennshotDir(Context context) {
        File rootDir = context.getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir;
    }
}
