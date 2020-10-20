package com.zbar.lib;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;

public class DecodeCaptureManager {

    private boolean flag = true;
    private boolean isCycle;
    private boolean isNeedCapture;
    private int x = 0;
    private int y = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;
    private Context mContext;
    private OnDecodeListener mDecodeListener;
    private CaptureActivityHandler mHandler;
    private static final String TAG = "DecodeCaptureManager";

    public DecodeCaptureManager(Context context) {
        this(context, false);
    }

    public DecodeCaptureManager(Context context, boolean isCycle) {
        this.isCycle = isCycle;
        mContext = context.getApplicationContext();
        CameraManager.init(context);
    }

    public boolean isNeedCapture() {
        return isNeedCapture;
    }

    public void setNeedCapture(boolean isNeedCapture) {
        this.isNeedCapture = isNeedCapture;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }


    protected void light() {
        if (flag == true) {
            flag = false;
            // 开闪光灯
            CameraManager.get().openLight();
        } else {
            flag = true;
            // 关闪光灯
            CameraManager.get().offLight();
        }

    }

    public void decode() {
        mHandler.restartPreviewAndDecode();
    }

    public void close() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    public void onDecodeResult(String result) {
        Log.d("DecodeCaptureManager", "onDecodeResult: " + result);
        if (!TextUtils.isEmpty(result)) {
            if (mDecodeListener != null) {
                mDecodeListener.onResult(result);
            }
            if (isCycle) {
                decode();
            }
        } else {
            decode();
        }
    }

    /**
     * @param
     * @param index
     */
    public void initCamera(SurfaceView surfaceView, View cropLayout, int index) throws Exception {
        if (!CameraManager.get().isPreviewing()) {
            CameraManager.get().openDriver(surfaceView.getHolder(), index);
            Point point = CameraManager.get().getCameraResolution();
            int width = point.y;
            int height = point.x;
            x = cropLayout.getLeft() * width / surfaceView.getWidth();
            y = cropLayout.getTop() * height / surfaceView.getHeight();
            cropWidth = cropLayout.getWidth() * width / surfaceView.getWidth();
            cropHeight = cropLayout.getHeight() * height / surfaceView.getHeight();
            mHandler = new CaptureActivityHandler(mContext, DecodeCaptureManager.this);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isPreviewing() {
        return CameraManager.get().isPreviewing();
    }

    public void releaseDecodeCamera() {
        CameraManager.get().stopPreview();
        CameraManager.get().closeDriver();
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setOnDecodeListener(OnDecodeListener listener) {
        mDecodeListener = listener;
    }

    public interface OnDecodeListener {

        void onResult(String result);

        void onError(Exception e);
    }
}