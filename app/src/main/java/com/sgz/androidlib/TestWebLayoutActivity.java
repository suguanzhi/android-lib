package com.sgz.androidlib;

import android.os.Bundle;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.activity.utils.WebviewHandler;
import com.android.sgzcommon.view.WebLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sgz
 * @date 2020/9/2
 */
public class TestWebLayoutActivity extends BaseActivity {

    @BindView(R.id.wl)
    WebLayout mWl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weblayout);
        ButterKnife.bind(this);
        mWl.load("http://www.hao123.com", new WebviewHandler.OnLoadListener() {
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
    public void onBackPressed() {
        if (mWl.canGoBack()){
            mWl.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
