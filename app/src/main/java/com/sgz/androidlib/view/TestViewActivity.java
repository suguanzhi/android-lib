package com.sgz.androidlib.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.view.InputEditView;
import com.android.sgzcommon.view.LoadResultView;
import com.android.sgzcommon.view.MyGridView;
import com.android.sgzcommon.view.TitleBar;
import com.android.sgzcommon.view.imageview.CornerImageView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sgz.androidlib.R;
import com.sgz.androidlib.view.adapter.MyGridAdapter;
import com.sgz.androidlib.view.sample.TestSmartRefreshLayoutActivity;
import com.sgz.androidlib.view.sample.TestWebLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/9/18
 */
public class TestViewActivity extends BaseActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R.id.btn_web_layout)
    Button mBtnWebLayout;
    @BindView(R.id.lrv)
    LoadResultView mLrv;
    @BindView(R.id.btn_refresh)
    Button mBtnRefresh;
    @BindView(R.id.civ_picture)
    CornerImageView mCivPicture;
    @BindView(R.id.btn_focus)
    Button mBtnFocus;
    @BindView(R.id.iev)
    InputEditView mIev;
    @BindView(R.id.mgv)
    MyGridView mMgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        ButterKnife.bind(this);
        mLrv.empty("暂无数据");
        String url = "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3363295869,2467511306&fm=26&gp=0.jpg";
        if (!TextUtils.isEmpty(url)) {
            mCivPicture.setTag(url);
            mImageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    String tag = (String) mCivPicture.getTag();
                    String rUrl = response.getRequestUrl();
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                        if (tag.equals(rUrl)) {
                            mCivPicture.setImageBitmap(bitmap);
                        }
                    } else {
                        mCivPicture.setImageBitmap(null);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    mCivPicture.setImageBitmap(null);
                }
            });
        } else {
            mCivPicture.setImageBitmap(null);
        }
        mIev.setOnValueChangeListener(new InputEditView.OnValueChangeListener() {
            @Override
            public void onValueChange(int value, boolean isSetValue) {
                Log.d("TestViewActivity", "onValueChange: value == " + value + "; isSetValue == " + isSetValue);
            }
        });
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(i+"");
        }
        MyGridAdapter adapter = new MyGridAdapter(this,list);
        mMgv.setAdapter(adapter);

    }

    @OnClick({R.id.btn_web_layout, R.id.btn_refresh, R.id.btn_focus})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_web_layout:
                intent = new Intent(mContext, TestWebLayoutActivity.class);
                break;
            case R.id.btn_refresh:
                intent = new Intent(mContext, TestSmartRefreshLayoutActivity.class);
                break;
            case R.id.btn_focus:
                mIev.setValue(1234);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
