package com.android.sgzcommon.take_photo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

public class CircleTransform implements Transformation {

    private float r;
    private int width;
    private int height;

    public CircleTransform(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public CircleTransform(int width, int height, float r) {
        this.r = r;
        this.width = width;
        this.height = height;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (width <= 0 || width > source.getWidth()) {
            width = source.getWidth();
        }
        if (height <= 0 || height > source.getHeight()) {
            height = source.getHeight();
        }
        /**
         * 求起始点
         */
        int x = (source.getWidth() - width) / 2;
        int y = (source.getHeight() - height) / 2;

        /**
         * 生成BitMap
         */
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, width, height);
        if (squaredBitmap != source) {
            //释放
            source.recycle();
        }

        /**
         * 建立新的Bitmap
         */
        Bitmap bitmap = Bitmap.createBitmap(width, height, source.getConfig());

        /**
         * 画布画笔
         */
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        if (r == 0f) {
            int size = Math.min(width, height);
            r = size * 1f / 2;
        }
        /**
         * 画圆角
         */
        RectF rectF = new RectF(0f, 0f, (float) width, (float) height);
        canvas.drawRoundRect(rectF, r, r, paint);
        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}