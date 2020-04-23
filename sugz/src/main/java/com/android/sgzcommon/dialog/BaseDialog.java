package com.android.sgzcommon.dialog;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.sgzcommon.http.okhttp.OKHttpDialog;
import com.android.sgzcommon.utils.SystemUtil;
import com.android.sugz.R;

import androidx.annotation.StyleRes;

/**
 * Created by sgz on 2017/3/24.
 */

public abstract class BaseDialog extends OKHttpDialog {


    protected Context mContext;
    protected int mMinSize;
    protected Point mWindowSize;
    protected static final int DISMISS = 1;
    private static final String TAG = "BaseDialog";

    protected abstract int getContentViewId();

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected void onDismiss() {

    }

    public BaseDialog(Context context) {
        super(context, R.style.MyDialog);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mWindowSize = SystemUtil.getWindowSize(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mMinSize = Math.min(mWindowSize.x, mWindowSize.y);
        int width = getWidth();
        if (0 == width) {
            width = mMinSize * 4 / 5;
        }
        int height = getHeight();
        if (0 == height) {
            height = width;
        }
        setLayoutParamsAnim(width, height);
    }

    protected View getRootView() {
        View view = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        return view;
    }

    protected void setLayoutParamsAnim(int width, int height) {
        initLayout(width, height, Gravity.CENTER, R.style.anim_scale);
    }

    protected void setLayoutParamsAnim(int width, int height, @StyleRes int animRes) {
        initLayout(width, height, Gravity.CENTER, animRes);
    }

    protected void setLayoutParams(int width, int height) {
        initLayout(width, height, Gravity.CENTER);
    }

    protected void initLayout(int width, int height, int gravity) {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = gravity;
        params.width = width;
        params.height = height;
        window.setAttributes(params);
    }

    protected void initLayout(int width, int height, int gravity, @StyleRes int animRes) {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = gravity;
        params.width = width;
        params.height = height;
        window.setAttributes(params);
        window.setWindowAnimations(animRes);
    }


}
