package com.sgz.androidlib.activity.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.sgzcommon.activity.TakePhotoActivity;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListener;
import com.sgz.androidlib.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class TestTakePhotoActivity extends TakePhotoActivity {

    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        ButterKnife.bind(this);
    }

    public void take(View v) {
        takePhoto(null, new OnTakePhotoListener() {
            @Override
            public void onPhoto(Bitmap bitmap) {
                Log.d("SecondActivity", "onPhoto: ");
                //                if (bitmap != null) {
                //                    Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath());
                mIvPhoto.setImageBitmap(bitmap);
                //                }
            }
        });
    }
}
