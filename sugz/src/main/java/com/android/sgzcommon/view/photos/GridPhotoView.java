package com.android.sgzcommon.view.photos;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.android.sgzcommon.take_photo.GetPhotoImpl;
import com.android.sgzcommon.take_photo.adapter.PictureGridEditAdapter;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author sgz
 * @date 2020/10/24
 */
public class GridPhotoView extends RelativeLayout {

    RecyclerView mRecyclerView;
    PictureGridEditAdapter mAdapter;
    OnTakePhotoListListener mListener;
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
}
