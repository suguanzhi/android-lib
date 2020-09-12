package com.android.sgzcommon.activity.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.sgzcommon.utils.SystemUtil;

import org.greenrobot.greendao.annotation.NotNull;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;

public final class WebviewHandler {

    private boolean canOpenApp;
    private boolean isReload;
    private Context mContext;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private MyHandler mHandler;
    private OnLoadListener mListener;
    private OnOpenAppListener mOpenAppListener;

    /**
     * @param webView
     * @param progressBar
     * @param isReload    加载失败5s后是否重新加载
     */
    public WebviewHandler(@NotNull WebView webView, @Nullable ProgressBar progressBar, final boolean isReload) {
        this.isReload = isReload;
        canOpenApp = true;
        mHandler = new MyHandler(this);
        mWebView = webView;
        mContext = mWebView.getContext();
        mProgressBar = progressBar;
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setBlockNetworkImage(false);
        settings.setAllowFileAccess(true);
        settings.setSaveFormData(true);
        String appCacheDir = mContext.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(appCacheDir);
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WebviewHandler", "shouldOverrideUrlLoading: " + url);
                try {
                    if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                        Log.d("WebviewHandler", "shouldOverrideUrlLoading: -------- 1");
                        Uri uri = Uri.parse(url);
                        String host = uri.getHost();
                        String scheme = uri.getScheme();
                        if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(scheme)) {
                            Log.d("WebviewHandler", "shouldOverrideUrlLoading: -----------2");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            if (SystemUtil.isInstall(mContext, intent)) {
                                Log.d("WebviewHandler", "shouldOverrideUrlLoading: ------3");
                                if (canOpenApp) {
                                    Log.d("WebviewHandler", "shouldOverrideUrlLoading: ----------4");
                                    if (mOpenAppListener != null) {
                                        Log.d("WebviewHandler", "shouldOverrideUrlLoading: -----------5");
                                        mOpenAppListener.onOpen(intent);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("WebviewHandler", "shouldOverrideUrlLoading: -----------6");
                    e.printStackTrace();
                }
                Log.d("WebviewHandler", "shouldOverrideUrlLoading: -----------7");
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("BaseWebActivity", "onPageFinished: ");
                if (mListener != null) {
                    mListener.loadFinish(url);
                }
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("BaseWebActivity", "onPageStarted: ");
                if (mListener != null) {
                    mListener.loadStart(url);
                }
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (mListener != null) {
                    mListener.loadError(errorCode);
                }
                Log.d("BaseWebActivity", "onReceivedError: " + errorCode + ";" + description);
                if (isReload) {
                    mHandler.sendEmptyMessageDelayed(MyHandler.RELOAD, 5000);
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d("BaseWebActivity", "onLoadResource: url = " + url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d("BaseWebActivity", "onProgressChanged: " + newProgress);
                if (mProgressBar != null) {
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }


        });
        //不加这个图片显示不出来
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //        mWebView.addJavascriptInterface(new MyJavascriptInterface(this), "androidJs");
        //允许cookie 不然有的网站无法登陆
        CookieManager mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        mCookieManager.setAcceptThirdPartyCookies(mWebView, true);
    }

    public void setCanOpenApp(boolean canOpen) {
        canOpenApp = canOpen;
    }

    public void setOnOpenAppListener(OnOpenAppListener listener) {
        mOpenAppListener = listener;
    }

    public interface OnOpenAppListener {
        void onOpen(Intent intent);
    }

    public void goBack() {
        Log.d("WebviewHandler", "goBack: " + mWebView.canGoBack());
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(JavascriptInterface javascriptInterface) {
        javascriptInterface.setTarget(this);
        mWebView.addJavascriptInterface(javascriptInterface, "androidJs");
    }

    public void load(String url, OnLoadListener listener) {
        mListener = listener;
        mWebView.loadUrl(url);
    }

    public void loadData(String data, OnLoadListener listener) {
        mListener = listener;
        mWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
    }

    public void reload() {
        mHandler.obtainMessage(MyHandler.RELOAD).sendToTarget();
    }

    public void setOnLoadListener(OnLoadListener listener) {
        mListener = listener;
    }

    public void release() {
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
//            mWebView.pauseTimers();
            mWebView = null;
        }
    }

    public interface OnLoadListener {
        void loadStart(String url);

        void loadFinish(String url);

        void loadError(int errorCode);

        void reload();

    }

    private static class MyHandler extends Handler {

        public static final int RELOAD = 1;

        WeakReference<WebviewHandler> mReference;

        public MyHandler(WebviewHandler webHandler) {
            mReference = new WeakReference<>(webHandler);
        }

        @Override
        public void handleMessage(Message msg) {
            WebviewHandler webHandler = mReference.get();
            if (webHandler != null) {
                switch (msg.what) {
                    case RELOAD:
                        removeMessages(RELOAD);
                        if (webHandler.mListener != null) {
                            webHandler.mListener.reload();
                        }
                        webHandler.mWebView.reload();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }
}