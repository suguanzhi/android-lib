package com.sgz.androidlib.activity.sample;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import com.android.sgzcommon.activity.BaseQRCodeActivity;
import com.sgz.androidlib.R;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sgz
 * @date 2020/10/20
 */
public class TestMyQRCodeActivity extends BaseQRCodeActivity {

    @BindView(R.id.tv_result)
    TextView mTvResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResult(String result) {
        Log.d("TestQRCodeActivity", "onResult: ");
        mTvResult.setText(result);
    }

    @Override
    protected void onResultError(Exception e) {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_qrcode_content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
