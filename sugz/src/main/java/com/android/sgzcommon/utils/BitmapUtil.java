package com.android.sgzcommon.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by sgz on 2016/a12/20.
 */

public class BitmapUtil {

    /**
     * 从图片的网络地址获取图片bitmap
     *
     * @param serverUrl
     * @return
     */
    public static Bitmap getBitmapFromUrl(String serverUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(serverUrl);
            InputStream inputStream = url.openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = (int) Math.floor((double) height / (double) reqHeight);
            int widthRatio = (int) Math.floor((double) width / (double) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;
    }

    /**
     * 获取压缩的bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getShowBitmap(String filePath) {
        BitmapFactory.Options options = getOptions(filePath, 200, 200);
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取压缩的bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getShowBitmap(String filePath, int width, int height) {
        BitmapFactory.Options options = getOptions(filePath, width, height);
        return BitmapFactory.decodeFile(filePath, options);
    }

    private static BitmapFactory.Options getOptions(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = caculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return options;
    }

    /**
     * 将图片压缩后另存
     *
     * @param context
     * @param sourcePath 原图片路
     * @param targetPath 压缩后保存路
     * @return
     */
    public static boolean saveSmallImage(Context context, String sourcePath, String targetPath) {
        int reqWidth = SystemUtil.getWindowSize(context).x;
        int reqHeight = SystemUtil.getWindowSize(context).y;
        if (reqWidth > 1000) {
            reqWidth = 864;
            reqHeight = reqWidth / 2;
        }
        BitmapFactory.Options options = getOptions(sourcePath, reqWidth, reqHeight);
        Bitmap bitmap = BitmapFactory.decodeFile(sourcePath, options);
        return saveBimapToLocal(targetPath, bitmap);
    }

    /**
     * 获取根据屏幕的宽高进行相应压缩后的bitmap
     *
     * @param context
     * @param path
     * @return
     */
    public static Bitmap getFitBitmap(Context context, String path) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options opts = getOptions(path, SystemUtil.getWindowSize(context).x, SystemUtil.getWindowSize(context).y);
            bitmap = BitmapFactory.decodeFile(path, opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将bitmap保存到本地
     *
     * @param targetPath
     * @param bitmap
     * @return
     */
    public static boolean saveBimapToLocal(String targetPath, Bitmap bitmap) {
        boolean result = false;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(targetPath));
            result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Log.d(TAG, "drawableToBitamp:width = " + w);
        Log.d(TAG, "drawableToBitamp:height = " + h);
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获取视频第一帧
     *
     * @param path 视频资源路径
     * @return
     */
    public static Bitmap getFirstFrameFromVedio(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

}
