package com.sgz.androidlib.dialog.sample;

import android.content.Context;

import com.android.sgzcommon.dialog.BaseAutoDismissDialog;
import com.sgz.androidlib.R;

/**
 * @author sgz
 * @date 2020/6/30
 */
public class TestAutoDismissDialog extends BaseAutoDismissDialog {

    public TestAutoDismissDialog(Context context, long delay) {
        super(context, delay);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_test_auto_dismiss;
    }

}
