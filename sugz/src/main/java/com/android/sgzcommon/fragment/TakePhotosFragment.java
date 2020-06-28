package com.android.sgzcommon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.ShowImages;
import com.android.sgzcommon.take_photo.ShowImagesImpl;
import com.android.sgzcommon.take_photo.TakePhotos;
import com.android.sgzcommon.take_photo.TakePhotosImpl;
import com.android.sgzcommon.take_photo.listener.OnDeletePhotoListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoGridListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoClickListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/12/27.
 */
public abstract class TakePhotosFragment extends BaseFragment implements TakePhotos, ShowImages {

    private RecyclerView mRvShowImages;
    private RecyclerView mRvTakePhotos;
    private TakePhotosImpl mTakePhotos;
    private ShowImages mShowImages;

    protected abstract int getShowImageGridViewId();

    protected abstract int getTakePhotoGridViewId();

    protected abstract void onPhotos(List<PhotoUpload> uploads);

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        mRvShowImages = parent.findViewById(getShowImageGridViewId());
        mRvTakePhotos = parent.findViewById(getTakePhotoGridViewId());
        mShowImages = new ShowImagesImpl(mContext, mRvShowImages);
        mTakePhotos = new TakePhotosImpl(getActivity(), mRvTakePhotos);
        mTakePhotos.setOnPhotoGridListener(new OnTakePhotoGridListener() {
            @Override
            public void onPhotos(List<PhotoUpload> uploads) {
                TakePhotosFragment.this.onPhotos(uploads);
            }
        });
    }

    @Override
    public void addPhotoUploads(List<? extends PhotoUpload> photoUploads) {
        mTakePhotos.addPhotoUploads(photoUploads);
    }

    @Override
    public void setImageUrls(List<String> urls) {
        mShowImages.setImageUrls(urls);
    }

    @Override
    public void takePhoto(String path) {
        mTakePhotos.takePhoto(path);
    }

    @Override
    public void choosePhoto() {
        mTakePhotos.choosePhoto();
    }

    @Override
    public void setOnTakePhotoClickListener(OnTakePhotoClickListener listener) {
        mTakePhotos.setOnTakePhotoClickListener(listener);
    }

    @Override
    public void setOnDeletePhotoListener(OnDeletePhotoListener listener) {
        mTakePhotos.setOnDeletePhotoListener(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoActivity", "onActivityResult: requestCode = " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        mTakePhotos.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mTakePhotos.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void notifyPhotoChanged() {
        mShowImages.notifyPhotoChanged();
    }

    @Override
    public void notifyTakePhotoChanged() {
        mTakePhotos.notifyTakePhotoChanged();
    }

    @Override
    public void notifyTakePhotoChanged(int position) {
        mTakePhotos.notifyTakePhotoChanged(position);
    }

    @Override
    public List<PhotoUpload> getPhotoUploads() {
        return mTakePhotos.getPhotoUploads();
    }

    @Override
    public void clearPhotos() {
        mTakePhotos.clearPhotos();
    }

    @Override
    public void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, OnPhotoUploadListener listener) {
        mTakePhotos.uploadPhotos(url, data, headers, listener);
    }
}
