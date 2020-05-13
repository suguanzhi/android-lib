package com.android.sgzcommon.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by sgz on 2016/a12/20.
 */

public class FileUtil {

    /**
     * 按文件最后修改时间从小到大排序
     *
     * @param fs
     */
    public static void fileSort(File[] fs) {
        File temp = null;
        final int len = fs.length;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (fs[i].lastModified() > fs[j].lastModified()) {
                    temp = fs[j];
                    fs[j] = fs[i];
                    fs[i] = temp;
                }
            }
        }
    }

    /**
     * 删除指定文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                return deleteFile(new File(path));
            }
        }
        return false;
    }

    /**
     * 删除指定文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        try {
            if (file.isFile()) {
                return file.delete();
            }
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    return file.delete();
                }
                for (int i = 0; i < childFiles.length; i++) {
                    deleteFile(childFiles[i]);
                }
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 文件复制
     * @param source 源文件
     * @param dest
     * @return
     * @throws IOException
     */
    public static boolean copyFile(File source, File dest) throws IOException {
        boolean result = false;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
        return result;
    }
}
