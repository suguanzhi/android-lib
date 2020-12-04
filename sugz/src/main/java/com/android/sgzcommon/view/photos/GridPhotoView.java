package com.android.sgzcommon.view.photos;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.android.sgzcommon.take_photo.GetPhotoImpl;
import com.android.sgzcommon.take_photo.GetPhotos;
import com.android.sgzcommon.take_photo.adapter.PictureGridEditAdapter;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoGridListener;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author sgz
 * @date 2020/10/24
 */
public class GridPhotoView extends RelativeLayout implements GetPhotos {

    RecyclerView mRecyclerView;
    PictureGridEditAdapter mAdapter;
    OnTakePhotoGridListener mListener;
    GetPhotoImpl mTakePhoto;

    int mColumn;
    List<PhotoUpload> mPhotoUploads;

    public GridPhotoView(Context context) {
        super(context);
    }

    public GridPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GridPhotoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context){

    }

    @Override
    public List<PhotoUpload> getPhotoUploads() {
        return null;
    }

    @Override
    public void setOnPhotoClickListener(OnPhotoClickListener listener) {

    }

    @Override
    public void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, OnPhotoUploadListener listener) {

    }

    @Override
    public void clearPhotos() {

    }

    @Override
    public void notifyPhotoChanged() {

    }

    @Override
    public void notifyPhotoChanged(int position) {

    }

    @Override
    public void takePhoto() {

    }

    @Override
    public void choosePhoto() {

    }
}
