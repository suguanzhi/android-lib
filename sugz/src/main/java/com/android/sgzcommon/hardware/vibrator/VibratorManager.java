package com.android.sgzcommon.hardware.vibrator;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * @author sgz
 * @date 2020/10/30
 */
public class VibratorManager {

    private static VibratorManager sVibratorManager;
    private Vibrator mVibrator;

    private VibratorManager(Context context) {
        mVibrator = (Vibrator) context.getApplicationContext().getSystemService(VIBRATOR_SERVICE);
    }

    public static VibratorManager getInstance(Context context) {
        synchronized (VibratorManager.class) {
            if (sVibratorManager == null) {
                sVibratorManager = new VibratorManager(context);
            }
        }
        return sVibratorManager;
    }

    public void vibrate(long time) {
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 26) {
            mVibrator.vibrate(time);
        } else if (Build.VERSION.SDK_INT >= 26) {
            mVibrator.vibrate(VibrationEffect.createOneShot(time, AudioAttributes.USAGE_NOTIFICATION_RINGTONE));
        }
    }

    public void cancle() {
        mVibrator.cancel();
    }
}
