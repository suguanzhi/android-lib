package com.android.sgzcommon.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.sgzcommon.activity.utils.EmptyEntity;
import com.android.sgzcommon.utils.SystemUtil;

import androidx.annotation.Nullable;
import de.greenrobot.event.EventBus;

/**
 * Created by sgz on 2017/1/3.
 */

public class BaseService extends Service {

    protected Context mContext;
    protected WindowManager mWindowManager;
    private RelativeLayout mWindowTipView;
    private static final int TIP_TEXT_SIZE = 20;
    private static final String TAG_TIP_VIEW = "tip";

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mContext = getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        setupTipView();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onEventMainThread(EmptyEntity emptyEntity){

    }

    private void setupTipView() {
        mWindowTipView = new RelativeLayout(mContext);
        mWindowTipView.setBackgroundColor(Color.GRAY);
        TextView tipTV = new TextView(mContext);
        tipTV.setTag(TAG_TIP_VIEW);
        tipTV.setTextSize(TIP_TEXT_SIZE);
        tipTV.setTextColor(Color.BLACK);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mWindowTipView.addView(tipTV, params);
    }

    protected void showWindowTip(CharSequence text) {
        try {
            mWindowManager.removeViewImmediate(mWindowTipView);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.i("ServiceTest", Log.getStackTraceString(e));
        }
        Log.i("ServiceTest", "showWindowTip");
        if (!TextUtils.isEmpty(text)) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
            lp.format = PixelFormat.RGBA_8888;
            lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            lp.gravity = Gravity.CENTER;
            int width = SystemUtil.getWindowSize(mContext).x / 3;
            int height = width / 2;
            lp.width = width;
            lp.height = height;
            mWindowManager.addView(mWindowTipView, lp);
            TextView tipTV = (TextView) mWindowTipView.findViewWithTag(TAG_TIP_VIEW);
            if (tipTV != null) {
                Log.i("ServiceTest", "tipTV != null");
                tipTV.setText(text);
            } else {
                Log.i("ServiceTest", "tipTV == null");
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
