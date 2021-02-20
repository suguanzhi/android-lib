package com.android.sgzcommon.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.sgzcommon.utils.BitmapUtils;
import com.android.sgzcommon.view.DragPhotoView;
import com.android.sugz.R;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;

public class PhotoViewActivity extends BaseActivity {

    private DragPhotoView mDpvImage;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgz_photo_view);
        String path = getIntent().getStringExtra("path");
        if (!TextUtils.isEmpty(path)) {
            if (path.startsWith("http")) {
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(mDpvImage, 0, 0);
                mImageLoader.get(path, listener, 1080, 1080);
            } else {
                File file = new File(path);
                if (file.exists()) {
                    mDpvImage.setImageBitmap(BitmapUtils.getWindowFitBitmap(this, path));
                }
            }
        }
        mDpvImage.setOnTouchListener(new DragPhotoView.OnTouchListener() {
            @Override
            public void onTap(DragPhotoView view) {

            }

            @Override
            public void onMovePercent(DragPhotoView view, float percent) {
                Log.d("PhotoViewActivity", "onMovePercent: percent == " + percent);
                mIvBack.setAlpha(percent);
            }

            @Override
            public void onExit(DragPhotoView view, float translateX, float translateY, float width, float height) {
                finish();
            }
        });
    }

    @Override
    public void onContentChanged() {
        mDpvImage = findViewById(R.id.dpv_image);
        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        super.onContentChanged();
    }
}