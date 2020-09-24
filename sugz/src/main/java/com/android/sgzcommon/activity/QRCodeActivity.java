package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.sugz.R;
import com.zbar.lib.DecodeCaptureManager;

/**
 * 1.app的build.gradle中添加：
 * defaultConfig {
 * ......
 * ndk {
 * abiFilters "armeabi", "armeabi-v7a", "x86", "mips"
 * }
 * }
 * <p>
 * 2.AndroidManifest文件中：
 * a）添加拍照权限
 * <uses-permission android:name="android.permission.CAMERA" />
 * b)activity组件注册
 * <activity android:name="com.android.sgzcommon.activity.QRCodeActivity" />
 * <p>
 * 3.通过startActivityForResult启动该activity
 * <p>
 * 4.onActivityResult中获取返回结果
 * <p>
 * Created by sgz on 2019/4/19 0019.
 */
public class QRCodeActivity extends BaseActivity implements SurfaceHolder.Callback {

    SurfaceView mSfvCamera;
    ImageView mIvScanLine;
    RelativeLayout mRlScrop;
    ImageView mIvClose;
    private DecodeCaptureManager mDecodeCaptureManager;

    public static final String RESULT_NAME = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgz_qrcode);
        mSfvCamera = findViewById(R.id.sfv_camera);
        mIvScanLine = findViewById(R.id.iv_scan_line);
        mRlScrop = findViewById(R.id.rl_scrop);
        mIvClose = findViewById(R.id.iv_close);
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SurfaceHolder holder = mSfvCamera.getHolder();
        holder.addCallback(QRCodeActivity.this);
    }

    /**
     * 获取扫码结果后是否销毁当前activity，并返回扫码结果result。
     *
     * @param result
     * @return
     */
    protected void onCodeResult(String result) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("QRCodeActivity", "surfaceCreated: 000000000");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("QRCodeActivity", "surfaceChanged: ");
        mDecodeCaptureManager = new DecodeCaptureManager(this, true);
        try {
            mDecodeCaptureManager.initCamera(mSfvCamera, mRlScrop, 0);
            mDecodeCaptureManager.setOnDecodeListener(new DecodeCaptureManager.OnDecodeListener() {
                @Override
                public void onResult(String result) {
                    Log.d("QRCodeActivity", "onResult: " + result);
                    stopScan();
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_NAME, result);
                    setResult(RESULT_OK, intent);
                    onCodeResult(result);
                }

                @Override
                public void onError(String error) {
                    showToast("扫码异常：" + error);
                }
            });
            startScan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("QRCodeActivity", "surfaceDestroyed: ");
        mDecodeCaptureManager.releaseDecodeCamera();
    }

    public void startScan() {
        mIvScanLine.setVisibility(View.VISIBLE);
        TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
        mAnimation.setDuration(1500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mIvScanLine.startAnimation(mAnimation);
    }

    public void stopScan() {
        mIvScanLine.setVisibility(View.GONE);
        mIvScanLine.clearAnimation();
    }
}