package com.android.sgzcommon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.take_photo.ShowImageGrid;
import com.android.sgzcommon.take_photo.ShowImageGridImpl;
import com.android.sgzcommon.take_photo.TakePhotoGrid;
import com.android.sgzcommon.take_photo.TakePhotoGridImpl;
import com.android.sgzcommon.take_photo.listener.OnPhotoListener;
import com.android.sgzcommon.take_photo.utils.PhotoUpload;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/12/27.
 */
public abstract class TakePhotoFragment extends BaseFragment implements TakePhotoGrid, ShowImageGrid {

    private RecyclerView mShowPhotoView;
    private RecyclerView mTakePhotoView;
    private TakePhotoGridImpl mTakePhotoGrid;
    private ShowImageGrid mShowImageGrid;

    protected abstract int getShowImageGridViewId();

    protected abstract int getTakePhotoGridViewId();

    protected abstract void onPhotoResult(List<PhotoUpload> uploads, PhotoUpload photo);

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        mShowPhotoView = parent.findViewById(getShowImageGridViewId());
        mTakePhotoView = parent.findViewById(getTakePhotoGridViewId());
        mShowImageGrid = new ShowImageGridImpl(mContext, mShowPhotoView);
        mTakePhotoGrid = new TakePhotoGridImpl(getActivity(), mTakePhotoView);
        mTakePhotoGrid.setOnPhotoListener(new OnPhotoListener() {
            @Override
            public void onPhoto(List<PhotoUpload> uploads, PhotoUpload photo) {
                onPhotoResult(uploads, photo);
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoActivity", "onActivityResult: requestCode = " + requestCode);
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
    public int getPhotoSize() {
        return mTakePhotoGrid.getPhotoSize();
    }

    @Override
    public void clearPhotos() {
        mTakePhotoGrid.clearPhotos();
    }
}
