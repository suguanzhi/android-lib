package com.android.sgzcommon.activity;

import android.content.Intent;

/**
 * @author sgz
 * @date 2020/10/20
 */
public class QRCodeActivity extends BaseQRCodeActivity {
    @Override
    protected void onResult(String result) {
        stopScan();
        Intent intent = new Intent();
        intent.putExtra(RESULT_NAME, result);
        setResult(RESULT_OK, intent);
        onCodeResult(result);
        finish();
    }

    @Override
    protected void onResultError(Exception e) {

    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }
}
