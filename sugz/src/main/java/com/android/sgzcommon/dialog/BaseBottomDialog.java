package com.android.sgzcommon.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.android.sugz.R;

/**
 * Created by sgz on 2017/3/24.
 */

public abstract class BaseBottomDialog extends BaseDialog {


    public BaseBottomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutParamsAnim(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void setLayoutParamsAnim(int width, int height) {
        setLayoutParamsAnim(width,height, Gravity.BOTTOM, R.style.anim_y_stranslate);
    }
}
