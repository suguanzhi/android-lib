package com.android.sgzcommon.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by sgz on 2017/4/11.
 */

public abstract class BaseAutoDismissDialog extends BaseDialog {

    private long mDelay;
    private Handler mHandler;

    public BaseAutoDismissDialog(Context context, long delay) {
        super(context);
        mDelay = delay;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mHandler == null) {
            mHandler = new MyHandler(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }

    /**
     *
     * @param delay
     */
    public void show(long delay) {
        mDelay = delay;
        dismissDelay();
        super.show();
    }

    /**
     *
     */
    private void dismissDelay() {
        mHandler.sendEmptyMessageDelayed(DISMISS, mDelay);
    }

    private static class MyHandler extends Handler {

        private WeakReference<BaseAutoDismissDialog> mReference;

        private MyHandler(BaseAutoDismissDialog dialog) {
            mReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseAutoDismissDialog dialog = mReference.get();
            if (dialog != null) {
                switch (msg.what) {
                    case DISMISS:
                        dialog.dismiss();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void show() {
        super.show();
        dismissDelay();
    }
}
