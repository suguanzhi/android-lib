package com.android.sgzcommon.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.sgzcommon.activity.utils.JavascriptInterface;
import com.android.sgzcommon.activity.utils.WebviewHandler;
import com.android.sgzcommon.dialog.TwoButtonDialog;
import com.android.sugz.R;

import androidx.annotation.Nullable;

/**
 * @author sgz
 * @date 2020/9/2
 */
public class SuWebView extends RelativeLayout {

    private TwoButtonDialog mWarmDialog;
    private WebviewHandler mWebviewHandler;

    public SuWebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_sgz_web, this);
        ProgressBar progressBar = findViewById(R.id.pb_loading);
        WebView webView = findViewById(R.id.wv_content);
        mWebviewHandler = new WebviewHandler(webView, progressBar, false);
        mWebviewHandler.setOnOpenAppListener(new WebviewHandler.OnOpenAppListener() {
            @Override
            public void onOpen(final Intent intent) {
                if (mWarmDialog == null) {
                    mWarmDialog = new TwoButtonDialog(getContext(), "是否允许打开本地应用浏览商品？");
                }
                mWarmDialog.setOnclickListener(new TwoButtonDialog.OnclickListener() {
                    @Override
                    public void onCancle(View view, Dialog dialog) {
                        dialog.dismiss();
                        mWebviewHandler.setCanOpenApp(false);
                    }

                    @Override
                    public void onConfirm(View view, Dialog dialog) {
                        dialog.dismiss();
                        mWebviewHandler.setCanOpenApp(true);
                        getContext().startActivity(intent);
                    }
                });
                mWarmDialog.show();
                mWarmDialog.setButtonLeftText("否");
                mWarmDialog.setButtonRightText("是");
            }
        });
    }

    /**
     * @param url
     * @param listener
     */
    public void load(String url, WebviewHandler.OnLoadListener listener) {
        mWebviewHandler.load(url, listener);
    }

    public void loadData(String data, WebviewHandler.OnLoadListener listener) {
        mWebviewHandler.loadData(data, listener);
    }

    public void reload() {
        mWebviewHandler.reload();
    }

    /**
     * @param obj
     */
    public void addJavascriptInterface(JavascriptInterface obj) {
        mWebviewHandler.addJavascriptInterface(obj);
    }

    /**
     * @param listener
     */
    public void setOnOpenAppListener(WebviewHandler.OnOpenAppListener listener) {
        mWebviewHandler.setOnOpenAppListener(listener);
    }

    public boolean canGoBack() {
        return mWebviewHandler.canGoBack();
    }

    public void goBack() {
        mWebviewHandler.goBack();
    }

    /**
     * 移除Webview，释放缓存
     */
    public void release() {
        this.removeAllViews();
        mWebviewHandler.release();
    }

}
