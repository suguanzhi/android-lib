package com.android.sgzcommon.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by sgz on 2017/4/11.
 */

public abstract class BaseAutoDismissDialog extends BaseDialog {
    private boolean isStop;
    private long mDelay;
    private int mTime = 0;
    protected Handler mHandler;

    public BaseAutoDismissDialog(Context context, int time) {
        super(context);
        init(time);
    }

    public BaseAutoDismissDialog(Context context, int time, int theme) {
        super(context);
        init(time);
    }

    private void init(int time) {
        mTime = time;
        mHandler = new MyHandler(this);
    }

    public void show(boolean delay) {
        super.show();
        if (delay) {
            dismissDelay(mTime);
        }
    }

    public void dismissDelay(long delay) {
        stopTimeCount(false);
        mDelay = delay;
        mHandler.sendEmptyMessageDelayed(DISMISS, mDelay);
    }

    public void stopTimeCount(boolean stop) {
        isStop = stop;
    }

    @Override
    public void onDetachedFromWindow() {
        mDelay = 0;
        isStop = false;
        super.onDetachedFromWindow();
    }

    private static class MyHandler extends Handler {

        private WeakReference<BaseAutoDismissDialog> mReference;

        public MyHandler(BaseAutoDismissDialog dialog) {
            mReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseAutoDismissDialog dialog = mReference.get();
            if (dialog != null) {
                switch (msg.what) {
                    case DISMISS:
                        if (!dialog.isStop) {
                            dialog.dismiss();
                            dialog.onDismiss();
                        }
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void show() {
        super.show();
        dismissDelay(mTime);
    }

    @Override
    public void onBackPressed() {

    }
}
