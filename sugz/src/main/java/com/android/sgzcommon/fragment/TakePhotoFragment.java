package com.android.sgzcommon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.take_photo.ShowPhotoGrid;
import com.android.sgzcommon.take_photo.ShowPhotoGridHandler;
import com.android.sgzcommon.take_photo.TakePhotoGrid;
import com.android.sgzcommon.take_photo.TakePhotoGridImpl;
import com.android.sgzcommon.take_photo.TakePhotoGridImplHandler;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/12/27.
 */
public abstract class TakePhotoFragment extends BaseFragment implements TakePhotoGrid, ShowPhotoGrid{

    private RecyclerView mShowPhotoView;
    private RecyclerView mTakePhotoView;
    private TakePhotoGridImpl mTakePhotoGridHandler;
    private ShowPhotoGrid mShowPhotoGridHandler;

    protected abstract int getShowPhotoGridViewId();

    protected abstract int getTakePhotoGridViewId();

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        mShowPhotoView = parent.findViewById(getShowPhotoGridViewId());
        mTakePhotoView = parent.findViewById(getTakePhotoGridViewId());
        mTakePhotoGridHandler = new TakePhotoGridImplHandler(getActivity(), mTakePhotoView);
        mShowPhotoGridHandler = new ShowPhotoGridHandler(mContext, mShowPhotoView);
    }


    @Override
    public void uploadImages(String url, Map<String, String> data, TakePhotoGrid.OnImageUploadListener listener) {
        mTakePhotoGridHandler.uploadImages(url,data,listener);
    }


    @Override
    public void setImageUrls(List<String> urls) {
        mShowPhotoGridHandler.setImageUrls(urls);
    }

    @Override
    public void takePhoto() {
        mTakePhotoGridHandler.takePhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoActivity", "onActivityResult: requestCode = " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        mTakePhotoGridHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        mTakePhotoGridHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void notifyPhotoChanged() {
        mShowPhotoGridHandler.notifyPhotoChanged();
    }

    @Override
    public void notifyTakePhotoChanged() {
        mTakePhotoGridHandler.notifyTakePhotoChanged();
    }

    @Override
    public void notifyTakePhotoChanged(int position) {
        mTakePhotoGridHandler.notifyTakePhotoChanged(position);
    }

    @Override
    public int getImageSize() {
        return mTakePhotoGridHandler.getImageSize();
    }

    @Override
    public void clearImages() {
        mTakePhotoGridHandler.clearImages();
    }
}
