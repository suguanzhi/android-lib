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
import com.android.sgzcommon.utils.SystemUtils;
import com.android.sugz.R;

import androidx.annotation.StyleRes;

/**
 * Created by sgz on 2017/3/24.
 */

public abstract class BaseDialog extends OKHttpDialog {

    private int mWidth;
    private int mHeight;
    protected int x;
    protected int y;
    protected Context mContext;
    protected Point mWindowSize;
    protected static final int DISMISS = 1;
    private static final String TAG = "BaseDialog";

    protected abstract int getContentViewId();

    public BaseDialog(Context context) {
        super(context, R.style.MyDialog);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mWindowSize = SystemUtils.getWindowSize(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        x = mWindowSize.x;
        y = mWindowSize.y;
        setLayoutParamsAnim(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    protected View getRootView() {
        View view = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        return view;
    }

    protected void setLayoutParamsAnim(int width, int height) {
        setLayoutParamsAnim(width, height, R.style.anim_scale);
    }

    protected void setLayoutParamsAnim(int width, int height, @StyleRes int animRes) {
        setLayoutParamsAnim(width, height, Gravity.CENTER, animRes);
    }

    protected void setLayoutParamsAnim(int width, int height, int gravity, @StyleRes int animRes) {
        initLayout(width, height, gravity, animRes);
    }

    protected void setLayoutParams(int width, int height) {
        setLayoutParams(width, height, Gravity.CENTER);
    }

    protected void setLayoutParams(int width, int height, int gravity) {
        initLayout(width, height, gravity);
    }

    private void initLayout(int width, int height, int gravity) {
        initLayout(width, height, gravity, 0);
    }

    private void initLayout(int width, int height, int gravity, @StyleRes int animRes) {
        mWidth = width;
        mHeight = height;
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = gravity;
        params.width = width;
        params.height = height;
        window.setAttributes(params);
        window.setWindowAnimations(animRes);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
