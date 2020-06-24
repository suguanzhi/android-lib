package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.sgzcommon.take_photo.ShowImages;
import com.android.sgzcommon.take_photo.ShowImagesImpl;
import com.android.sgzcommon.take_photo.TakePhotos;
import com.android.sgzcommon.take_photo.TakePhotosImpl;
import com.android.sgzcommon.take_photo.listener.OnDeletePhotoListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoGridListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoClickListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/5/10 0010.
 */
public abstract class TakePhotosActivity extends BaseActivity implements TakePhotos, ShowImages {

    private RecyclerView mShowPhotoView;
    private RecyclerView mTakePhotoView;
    private TakePhotosImpl mTakePhotoGrid;
    private ShowImages mShowImages;

    protected abstract int getContentViewId();

    protected abstract int getShowImageGridViewId();

    protected abstract int getTakePhotoGridViewId();

    protected abstract void onPhotos(List<PhotoUpload> uploads);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mShowPhotoView = findViewById(getShowImageGridViewId());
        mTakePhotoView = findViewById(getTakePhotoGridViewId());
        mShowImages = new ShowImagesImpl(this, mShowPhotoView);
        mTakePhotoGrid = new TakePhotosImpl(this, mTakePhotoView);
        mTakePhotoGrid.setOnPhotoGridListener(new OnTakePhotoGridListener() {
            @Override
            public void onPhotos(List<PhotoUpload> uploads) {
                TakePhotosActivity.this.onPhotos(uploads);
            }
        });
    }

    @Override
    public void addPhotoUploads(List<? extends PhotoUpload> photoUploads) {
        mTakePhotoGrid.addPhotoUploads(photoUploads);
    }

    @Override
    public void setImageUrls(List<String> urls) {
        mShowImages.setImageUrls(urls);
    }

    @Override
    public void takePhoto(String path) {
        mTakePhotoGrid.takePhoto(path);
    }

    @Override
    public void choosePhoto() {
        mTakePhotoGrid.choosePhoto();
    }

    @Override
    public void setOnTakePhotoClickListener(OnTakePhotoClickListener listener) {
        mTakePhotoGrid.setOnTakePhotoClickListener(listener);
    }

    @Override
    public void setOnDeletePhotoListener(OnDeletePhotoListener listener) {
        mTakePhotoGrid.setOnDeletePhotoListener(listener);
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
        mShowImages.notifyPhotoChanged();
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
