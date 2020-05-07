package com.sgz.androidlib;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.android.sgzcommon.activity.TakePhotoActivity;
import com.android.sgzcommon.take_photo.TakePhotoGridImpl;
import com.android.sgzcommon.take_photo.utils.PhotoUpload;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MainActivity extends TakePhotoActivity {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    private TakePhotoGridImpl mTakePhotoGrid;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getShowImageGridViewId() {
        return 0;
    }

    @Override
    protected int getTakePhotoGridViewId() {
        return R.id.rv_list;
    }

    @Override
    protected void onPhotoResult(List<PhotoUpload> uploads, PhotoUpload photo) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        checkAndRequestePermissions(permissions, new OnPermissionResultListener() {
            @Override
            public void onResult(String permission, boolean granted) {
                Log.d("MainActivity", "onResult: permission == " + permission + "; granted == " + granted);
            }
        });

    }

    @Override
    protected void onDestroy() {
        clearPhotos();
        super.onDestroy();
    }
}
