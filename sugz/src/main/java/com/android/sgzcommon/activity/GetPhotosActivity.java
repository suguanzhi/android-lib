package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.sgzcommon.take_photo.GetPhotos;
import com.android.sgzcommon.take_photo.GetPhotosImpl;
import com.android.sgzcommon.take_photo.ShowImages;
import com.android.sgzcommon.take_photo.ShowImagesImpl;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoGridListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/5/10 0010.
 */
public abstract class GetPhotosActivity extends BaseActivity implements GetPhotos, ShowImages {

    private RecyclerView mShowPhotoView;
    private RecyclerView mTakePhotoView;
    private GetPhotosImpl mTakePhotoGrid;
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
        mTakePhotoGrid = new GetPhotosImpl(this, mTakePhotoView);
        mTakePhotoGrid.setOnPhotoGridListener(new OnTakePhotoGridListener() {
            @Override
            public void onPhotos(List<PhotoUpload> uploads) {
                GetPhotosActivity.this.onPhotos(uploads);
            }
        });
    }

    @Override
    public void setImageUrls(List<String> urls) {
        mShowImages.setImageUrls(urls);
    }

    @Override
    public void takePhoto() {
        mTakePhotoGrid.takePhoto();
    }

    @Override
    public void choosePhoto() {
        mTakePhotoGrid.choosePhoto();
    }

    @Override
    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        mTakePhotoGrid.setOnPhotoClickListener(listener);
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
    public void notifyPhotoChanged(int position) {
        mTakePhotoGrid.notifyPhotoChanged(position);
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
