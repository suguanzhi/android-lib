package com.android.sgzcommon.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.sugz.R;

import butterknife.ButterKnife;

/**
 * @author sgz
 * @date 2020/6/23
 */
public class SelectPhotoWayDialog extends BaseBottomDialog {

    private Button mBtnTakePhoto;
    private Button mBtnSelectPhoto;
    private Button mBtnCancle;
    private OnClickListener listener;

    public SelectPhotoWayDialog(Context context) {
        super(context);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_sgz_select_photo_way;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mBtnTakePhoto = findViewById(R.id.btn_take_photo);
        mBtnSelectPhoto = findViewById(R.id.btn_select_photo);
        mBtnCancle = findViewById(R.id.btn_cancle);
        mBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onTakePhoto();
                }
            }
        });
        mBtnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onSelectPhoto();
                }
            }
        });
        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onCancle();
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onTakePhoto();

        void onSelectPhoto();

        void onCancle();
    }
}
