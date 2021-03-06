/*
 * Copyright JiongBull 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sgzcommon.logutil.jlog.util;


import android.util.Log;

import com.android.sgzcommon.logutil.jlog.JLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件工具类.
 */
public class FileUtils {

    /**
     * 读写文件的线程池，单线程模型.
     */
    private static ExecutorService sExecutorService;

    static {
        sExecutorService = Executors.newSingleThreadExecutor();
    }

    private FileUtils() {
    }

    /**
     * 判断文件或目录是否存在.
     *
     * @param filePath 路径
     * @return true - 存在，false - 不存在
     */
    public static boolean isExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 创建目录，若目录已存在则不处理.
     *
     * @param dirPath 目录路径
     * @return true - 目录存在（创建成功或已存在），false - 目录不存在
     */
    public static boolean createDir(String dirPath) {
        Log.d("FileUtils", "createDir: dirPath == " + dirPath);
        File file = new File(dirPath);
        if (!file.exists()) {
            Log.d("FileUtils", "createDir: 1");
            try {
                boolean a = file.mkdirs();
                Log.d("FileUtils", "createDir: a == " + a);
            }catch (Exception e){
               e.printStackTrace();
                Log.d("FileUtils", "createDir: e == " + Log.getStackTraceString(e));
            }
        }
        return file.exists();
    }

    /**
     * 把文本写入文件中.
     *
     * @param dirPath    目录路径
     * @param fileName   文件名
     * @param content    待写内容
     * @param isOverride 写入模式，true - 覆盖，false - 追加
     */
    public static void write(final String dirPath, final String fileName, final String content, final boolean isOverride) {
        sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("FileUtils", "run: 1");
                String filePath = dirPath + File.separator + fileName;
                FileOutputStream fos = null;
                try {
                    Log.d("FileUtils", "run: 2");
                    if (createDir(dirPath)) {
                        Log.d("FileUtils", "run: 3");
                        File file = new File(filePath);
                        boolean isExist = file.exists();
                        Log.d("FileUtils", "run: 4");
                        fos = new FileOutputStream(file, !(!isExist || isOverride));
                        Log.d("FileUtils", "run: 5");
                        fos.write(content.getBytes(JLog.getSettings().getCharset()));
                        Log.d("FileUtils", "run: 6");
                    }
                } catch (IOException e) {
                    Log.e("FileUtils", "run：7 -- " + e.getMessage());
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            Log.e("FileUtils", "run：7 --" + e.getMessage());
                        }
                    }
                }
            }
        });
    }
}