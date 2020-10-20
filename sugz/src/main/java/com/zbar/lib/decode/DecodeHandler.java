package com.zbar.lib.decode;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.sgzcommon.utils.FilePathUtils;
import com.zbar.lib.DecodeCaptureManager;
import com.zbar.lib.ZbarManager;
import com.zbar.lib.bitmap.PlanarYUVLuminanceSource;

import java.io.File;
import java.io.FileOutputStream;


/**
 * 作者: 陈涛(1076559197@qq.com)
 * <p>
 * 时间: 2014年5月9日 下午12:24:13
 * <p>
 * 版本: V_1.0.0
 * <p>
 * 描述: 接受消息后解码
 */
final class DecodeHandler extends Handler {

    Context mContext;
    DecodeCaptureManager mCaptureManager;

    DecodeHandler(Context context, DecodeCaptureManager manager) {
        this.mCaptureManager = manager;
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == CaptureActivityHandler.MSG_DECODE) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == CaptureActivityHandler.MSG_DECODE_QUIT) {
            Looper.myLooper().quit();
        }
    }

    private void decode(byte[] data, int width, int height) {
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width;// Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;

        ZbarManager manager = new ZbarManager();
        String result = manager.decode(rotatedData, width, height, true, mCaptureManager.getX(), mCaptureManager.getY(), mCaptureManager.getCropWidth(), mCaptureManager.getCropHeight());

        if (result != null) {
            if (mCaptureManager.isNeedCapture()) {
                // 生成bitmap
                PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(rotatedData, width, height, 0, 0, mCaptureManager.getCropWidth(), mCaptureManager.getCropHeight(), false);
                int[] pixels = source.renderThumbnail();
                int w = source.getThumbnailWidth();
                int h = source.getThumbnailHeight();
                Bitmap bitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888);
                try {
                    String rootPath = FilePathUtils.getAppPictureDir(mContext) + "/Qrcode/";
                    File root = new File(rootPath);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File f = new File(rootPath + "Qrcode.jpg");
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();

                    FileOutputStream out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != mCaptureManager.getHandler()) {
                Message msg = new Message();
                msg.obj = result;
                msg.what = CaptureActivityHandler.MSG_DECODE_SUCCESS;
                mCaptureManager.getHandler().sendMessage(msg);
            }
        } else {
            if (null != mCaptureManager.getHandler()) {
                mCaptureManager.getHandler().sendEmptyMessage(CaptureActivityHandler.MSG_DECODE_FAIL);
            }
        }
    }

}
