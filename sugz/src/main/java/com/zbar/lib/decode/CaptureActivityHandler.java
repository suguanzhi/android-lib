package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Message;

import com.zbar.lib.DecodeCaptureManager;
import com.zbar.lib.camera.CameraManager;


/**
 * 作者: 陈涛(1076559197@qq.com)
 * <p>
 * 时间: 2014年5月9日 下午12:23:32
 * <p>
 * 版本: V_1.0.0
 * <p>
 * 描述: 扫描消息转发
 */
public final class CaptureActivityHandler extends Handler {

    DecodeThread decodeThread = null;
    DecodeCaptureManager activity = null;
    private State state;
    public static final int MSG_PREVIEW = 181;
    public static final int MSG_AUTO_FOCUS = 727;
    public static final int MSG_DECODE = 5;
    public static final int MSG_DECODE_SUCCESS = 210;
    public static final int MSG_DECODE_FAIL = 315;
    public static final int MSG_DECODE_QUIT = 564;
    private static final String TAG = "CaptureActivityHandler";

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(DecodeCaptureManager activity) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity);
        decodeThread.start();
        state = State.SUCCESS;
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == MSG_AUTO_FOCUS) {
            if (state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, MSG_AUTO_FOCUS);
            }
        } else if (message.what == MSG_PREVIEW) {
            restartPreviewAndDecode();
        } else if (message.what == MSG_DECODE_SUCCESS) {
            state = State.SUCCESS;
            activity.onDecodeResult((String) message.obj);// 解析成功，回调
        } else if (message.what == MSG_DECODE_FAIL) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), MSG_DECODE);
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        removeMessages(MSG_DECODE_SUCCESS);
        removeMessages(MSG_DECODE_FAIL);
        removeMessages(MSG_DECODE);
        removeMessages(MSG_AUTO_FOCUS);
    }

    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), MSG_DECODE);
            CameraManager.get().requestAutoFocus(this, MSG_AUTO_FOCUS);
        }
    }

}
