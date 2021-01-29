package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.sgzcommon.take_photo.GetPhotoListImpl;
import com.android.sgzcommon.take_photo.ShowImages;
import com.android.sgzcommon.take_photo.ShowImagesImpl;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/5/10 0010.
 */
public abstract class BaseGetPhotoListActivity extends BaseActivity implements ShowImages {

    private RecyclerView mShowPhotoRecyclerView;
    private RecyclerView mGetPhotoRecyclerView;
    private GetPhotoListImpl mTakePhotoList;
    private ShowImages mShowImageGrid;

    @LayoutRes
    protected abstract int getContentViewId();

    @IdRes
    protected abstract int getShowImageGridViewId();

    @IdRes
    protected abstract int getTakePhotoGridViewId();

    protected abstract void onPhotos(List<PhotoUpload> uploads);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mGetPhotoRecyclerView = findViewById(getTakePhotoGridViewId());
        mShowPhotoRecyclerView = findViewById(getShowImageGridViewId());
        mShowImageGrid = new ShowImagesImpl(this, mShowPhotoRecyclerView);
        mTakePhotoList = new GetPhotoListImpl(this, mGetPhotoRecyclerView);
        mTakePhotoList.setOnPhotoListListener(new OnTakePhotoListListener() {
            @Override
            public void onPhotos(List<PhotoUpload> uploads) {
                BaseGetPhotoListActivity.this.onPhotos(uploads);
            }
        });
    }

    @Override
    public void setImageUrls(List<String> urls) {
        mShowImageGrid.setImageUrls(urls);
    }

    public void takePhoto() {
        mTakePhotoList.takePhoto();
    }

    public void choosePhoto() {
        mTakePhotoList.choosePhoto();
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        mTakePhotoList.setOnPhotoClickListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTakePhotoList.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mTakePhotoList.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void notifyPhotoChanged() {
        mShowImageGrid.notifyPhotoChanged();
    }

    public void notifyPhotoChanged(int position) {
        mTakePhotoList.notifyPhotoChanged(position);
    }

    public List<PhotoUpload> getPhotoUploads() {
        return mTakePhotoList.getPhotoUploads();
    }

    public void clearPhotos() {
        mTakePhotoList.clearPhotos();
    }

    public void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, OnPhotoUploadListener listener) {
        mTakePhotoList.uploadPhotos(url, data, headers, listener);
    }

}
