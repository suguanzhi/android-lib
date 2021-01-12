package com.sgz.androidlib.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.view.CircleProgressBar;
import com.android.sgzcommon.view.NumberEditText;
import com.android.sgzcommon.view.LoadResultView;
import com.android.sgzcommon.view.SuButton;
import com.android.sgzcommon.view.SuGridView;
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
import java.util.Random;

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
    SuButton mBtnWebLayout;
    @BindView(R.id.btn_refresh)
    SuButton mBtnRefresh;
    @BindView(R.id.btn_focus)
    SuButton mBtnFocus;
    @BindView(R.id.iev1)
    NumberEditText mIev1;
    @BindView(R.id.civ_picture)
    CornerImageView mCivPicture;
    @BindView(R.id.superview)
    CircleProgressBar mSuperview;
    @BindView(R.id.mgv)
    SuGridView mMgv;
    @BindView(R.id.lrv)
    LoadResultView mLrv;

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
        mTbTitle.setOnClickListener(new TitleBar.OnClickListener() {
            @Override
            public void onLeftClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                showToast("点击");
            }
        });
        mTbTitle.setRightText("右边");
        mIev1.setOnValueChangeListener(new NumberEditText.OnValueChangeListener() {
            @Override
            public void onValueChange(int value, boolean isEditing) {
                Log.d("TestViewActivity", "onValueChange: 1 value == " + value + "; isEditing == " + isEditing);
            }
        });
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(i + "");
        }
        MyGridAdapter adapter = new MyGridAdapter(this, list);
        mMgv.setAdapter(adapter);

        //mSuperview.setValue(100);
        mSuperview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //随机设定圆环大小
                int i = new Random().nextInt(100) + 1;
                mSuperview.setValue(i);
            }
        });

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
                mIev1.requestEditFocus();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
