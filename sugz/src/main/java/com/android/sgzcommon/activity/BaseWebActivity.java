package com.android.sgzcommon.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.sgzcommon.activity.utils.JavascriptInterface;
import com.android.sgzcommon.activity.utils.WebviewHandler;
import com.android.sgzcommon.dialog.TwoButtonDialog;
import com.android.sgzcommon.view.TitleBar;
import com.android.sugz.R;

/**
 * Created by sgz on 2019/11/16 0016.
 */
public abstract class BaseWebActivity extends BaseActivity implements WebviewHandler.OnLoadListener {

    protected TitleBar mTbTitle;
    WebView mWvContent;
    ProgressBar mPbLoading;
    RelativeLayout mRlWebContainer;
    private TwoButtonDialog mWarmDialog;
    private WebviewHandler mWebviewHandler;

    protected abstract JavascriptInterface getJavascriptInterface();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgz_web);
        mTbTitle = findViewById(R.id.tb_title);
        mWvContent = findViewById(R.id.wv_content);
        mPbLoading = findViewById(R.id.pb_loading);
        mRlWebContainer = findViewById(R.id.rl_web_container);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        Log.d("BaseWebActivity", "onCreate: url = " + url + "; title = " + title);
        if (TextUtils.isEmpty(title)) {
            mTbTitle.setVisibility(View.GONE);
        }
        mWarmDialog = new TwoButtonDialog(this, "是否允许打开本地应用浏览商品？");
        mWebviewHandler = new WebviewHandler(mWvContent, mPbLoading, false);
        mWebviewHandler.addJavascriptInterface(getJavascriptInterface());
        mWebviewHandler.setOnOpenAppListener(new WebviewHandler.OnOpenAppListener() {
            @Override
            public void onOpen(final Intent intent) {
                mWarmDialog.setOnclickListener(new TwoButtonDialog.OnClickListener() {
                    @Override
                    public void onCancle(View view, Dialog dialog) {
                        dialog.dismiss();
                        mWebviewHandler.setCanOpenApp(false);
                    }

                    @Override
                    public void onConfirm(View view, Dialog dialog) {
                        dialog.dismiss();
                        mWebviewHandler.setCanOpenApp(true);
                        startActivity(intent);
                        finish();
                    }
                });
                mWarmDialog.show();
                mWarmDialog.setButtonLeftText("否");
                mWarmDialog.setButtonRightText("是");
            }
        });
        mWebviewHandler.load(url, this);
        mTbTitle.setTitle(title);
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        if (mTbTitle != null) {
            mTbTitle.setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebviewHandler.canGoBack()) {
            mWebviewHandler.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void loadStart(String url) {

    }

    @Override
    public void loadFinish(String url) {

    }

    @Override
    public void loadError(int errorCode) {

    }

    @Override
    public void reload() {

    }

    @Override
    protected void onDestroy() {
        mRlWebContainer.removeAllViews();
        mWebviewHandler.release();
        super.onDestroy();
    }
}
