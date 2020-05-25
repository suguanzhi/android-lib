package com.sgz.androidlib;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.TakePhotoActivity;
import com.android.sgzcommon.http.okhttp.upload.UploadResultSet;
import com.android.sgzcommon.take_photo.TakePhotoGridImpl;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.PhotoUpload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            public void onResult(List<String> grants, List<String> denies) {

            }
        });
    }

    public void upload(View v){
        String url = "http://mildyak.xicp.net/api/uploadController.do?ajaxSaveFile";
        Map<String, String> data = new HashMap<>();
        uploadPhotos(url, data, new OnPhotoUploadListener() {
            @Override
            public void onAllSuccess() {
                Log.d("MainActivity", "onAllSuccess: ");
            }

            @Override
            public void onSuccess(PhotoUpload photoUpload, UploadResultSet result) {
                Log.d("MainActivity", "onSuccess: ");
            }

            @Override
            public void onFail(PhotoUpload photoUpload, Exception e) {
                Log.d("MainActivity", "onFail: " + Log.getStackTraceString(e));
            }
        });
    }

    @Override
    protected void onDestroy() {
        clearPhotos();
        super.onDestroy();
    }
}
