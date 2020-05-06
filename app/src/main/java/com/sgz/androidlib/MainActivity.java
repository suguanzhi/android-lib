package com.sgz.androidlib;

import android.os.Bundle;

import com.android.sgzcommon.activity.TakePhotoActivity;
import com.android.sgzcommon.take_photo.TakePhotoGridImpl;
import com.android.sgzcommon.take_photo.utils.PhotoUpload;

import java.util.List;

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

    }

    @Override
    protected void onDestroy() {
        clearPhotos();
        super.onDestroy();
    }
}
