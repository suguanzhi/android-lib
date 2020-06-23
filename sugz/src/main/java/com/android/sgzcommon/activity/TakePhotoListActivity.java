package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.sgzcommon.take_photo.ShowImageGrid;
import com.android.sgzcommon.take_photo.ShowImageGridImpl;
import com.android.sgzcommon.take_photo.TakePhotoGrid;
import com.android.sgzcommon.take_photo.TakePhotoGridImpl;
import com.android.sgzcommon.take_photo.listener.OnPhotoDeleteListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoClickListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/5/10 0010.
 */
public abstract class TakePhotoListActivity extends BaseActivity implements TakePhotoGrid, ShowImageGrid {

    private RecyclerView mShowPhotoView;
    private RecyclerView mTakePhotoView;
    private TakePhotoGridImpl mTakePhotoGrid;
    private ShowImageGrid mShowImageGrid;

    protected abstract int getContentViewId();

    protected abstract int getShowImageGridViewId();

    protected abstract int getTakePhotoGridViewId();

    protected abstract void onPhotoResult(List<PhotoUpload> uploads, PhotoUpload photo);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mShowPhotoView = findViewById(getShowImageGridViewId());
        mTakePhotoView = findViewById(getTakePhotoGridViewId());
        mShowImageGrid = new ShowImageGridImpl(this, mShowPhotoView);
        mTakePhotoGrid = new TakePhotoGridImpl(this, mTakePhotoView);
        mTakePhotoGrid.setOnPhotoListener(new OnPhotoListener() {
            @Override
            public void onAddPhoto(List<PhotoUpload> uploads, PhotoUpload photo) {
                onPhotoResult(uploads, photo);
            }
        });
    }

    @Override
    public void addPhotoUploads(List<? extends PhotoUpload> photoUploads) {
        mTakePhotoGrid.addPhotoUploads(photoUploads);
    }

    @Override
    public void setImageUrls(List<String> urls) {
        mShowImageGrid.setImageUrls(urls);
    }

    @Override
    public void takePhoto(String path) {
        mTakePhotoGrid.takePhoto(path);
    }

    @Override
    public void setOnTakePhotoClickListener(OnTakePhotoClickListener listener) {
        mTakePhotoGrid.setOnTakePhotoClickListener(listener);
    }

    @Override
    public void setOnPhotoDeleteListener(OnPhotoDeleteListener listener) {
        mTakePhotoGrid.setOnPhotoDeleteListener(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTakePhotoGrid.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mTakePhotoGrid.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void notifyPhotoChanged() {
        mShowImageGrid.notifyPhotoChanged();
    }

    @Override
    public void notifyTakePhotoChanged() {
        mTakePhotoGrid.notifyTakePhotoChanged();
    }

    @Override
    public void notifyTakePhotoChanged(int position) {
        mTakePhotoGrid.notifyTakePhotoChanged(position);
    }

    @Override
    public List<PhotoUpload> getPhotoUploads() {
        return mTakePhotoGrid.getPhotoUploads();
    }

    @Override
    public void clearPhotos() {
        mTakePhotoGrid.clearPhotos();
    }

    @Override
    public void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, OnPhotoUploadListener listener) {
        mTakePhotoGrid.uploadPhotos(url, data, headers, listener);
    }

}
