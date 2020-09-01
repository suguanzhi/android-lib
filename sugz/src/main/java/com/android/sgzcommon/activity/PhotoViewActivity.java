package com.android.sgzcommon.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.sgzcommon.utils.BitmapUtil;
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
                    mDpvImage.setImageBitmap(BitmapUtil.getFitBitmap(this, path));
                }
            }
        }
        mDpvImage.setOnExitListener(new DragPhotoView.OnExitListener() {
            @Override
            public void onExit(DragPhotoView dragPhotoView, float v, float v1, float v2, float v3) {
                Log.d("PhotoViewActivity", "onExit: v = " + v + "; v1 = " + v1 + "; v2 = " + v2 + ";v3 = " + v3);
                finish();
            }
        });
        mDpvImage.setOnTapListener(new DragPhotoView.OnTapListener() {
            @Override
            public void onTap(DragPhotoView dragPhotoView) {
                Log.d("PhotoViewActivity", "onTap: ");
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