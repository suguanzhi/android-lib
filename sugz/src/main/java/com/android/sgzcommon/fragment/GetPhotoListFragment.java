package com.android.sgzcommon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.take_photo.GetPhotoListImpl;
import com.android.sgzcommon.take_photo.ShowImages;
import com.android.sgzcommon.take_photo.ShowImagesImpl;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/12/27.
 */
public abstract class GetPhotoListFragment extends BaseFragment implements ShowImages {

    private RecyclerView mRvShowImages;
    private RecyclerView mRvTakePhotos;
    private GetPhotoListImpl mTakePhotoList;
    private ShowImages mShowImages;

    protected abstract int getShowImageGridViewId();

    protected abstract int getTakePhotoGridViewId();

    protected abstract void onPhotos(List<PhotoUpload> uploads);

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        mRvShowImages = parent.findViewById(getShowImageGridViewId());
        mRvTakePhotos = parent.findViewById(getTakePhotoGridViewId());
        mShowImages = new ShowImagesImpl(mContext, mRvShowImages);
        mTakePhotoList = new GetPhotoListImpl(getActivity(), mRvTakePhotos);
        mTakePhotoList.setOnPhotoListListener(new OnTakePhotoListListener() {
            @Override
            public void onPhotos(List<PhotoUpload> uploads) {
                GetPhotoListFragment.this.onPhotos(uploads);
            }
        });
    }

    @Override
    public void setImageUrls(List<String> urls) {
        mShowImages.setImageUrls(urls);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoActivity", "onActivityResult: requestCode = " + requestCode);
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
        mShowImages.notifyPhotoChanged();
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
