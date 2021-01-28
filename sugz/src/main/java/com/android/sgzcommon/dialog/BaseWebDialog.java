package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sgzcommon.activity.utils.JavascriptInterface;
import com.android.sgzcommon.activity.utils.WebviewHandler;
import com.android.sugz.R;

/**
 * Created by sgz on 2019/12/20.
 */
public abstract class BaseWebDialog extends BaseDialog implements WebviewHandler.OnLoadListener {

    protected TextView mTvTitle;
    protected WebView mWvContent;
    private ProgressBar mPbLoading;
    private TwoButtonDialog mWarmDialog;
    private WebviewHandler mWebviewHandler;
    private String mUrl;
    private String mTitle;

    protected abstract JavascriptInterface getJavascriptInterface();

    public BaseWebDialog(Context context) {
        super(context);
    }
    public BaseWebDialog(Context context, String title) {
        super(context);
        mTitle = title;
    }
    public BaseWebDialog(Context context, String url, String title) {
        super(context);
        mUrl = url;
        mTitle = title;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_sgz_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvTitle = findViewById(R.id.tv_title);
        mWvContent = findViewById(R.id.wv_content);
        mPbLoading = findViewById(R.id.pb_loading);
        Log.d("BaseWebDialog", "onCreate: url = " + mUrl + "; title = " + mTitle);
        initTitle();

        mWarmDialog = new TwoButtonDialog(mContext, "是否允许打开本地应用浏览商品？");
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
                        mContext.startActivity(intent);
                        dismiss();
                    }
                });
                mWarmDialog.show();
                mWarmDialog.setButtonLeftText("否");
                mWarmDialog.setButtonRightText("是");
            }
        });
        if (!TextUtils.isEmpty(mUrl)) {
            mWebviewHandler.load(mUrl, this);
        }
    }

    public void initTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            mTvTitle.setVisibility(View.GONE);
        }else {
            mTvTitle.setVisibility(View.VISIBLE);
        }
        mTvTitle.setText(mTitle);
    }

    public void show(String title,String data){
        super.show();
        mTitle= title;
        initTitle();
        mWebviewHandler.loadData(data, new WebviewHandler.OnLoadListener() {
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
        });
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
}
