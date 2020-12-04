package com.sgz.androidlib.activity.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.GetPhotosActivity;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.GetPhotosImpl;
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.sgz.androidlib.R;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class TestGetPhotosActivity extends GetPhotosActivity {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    private GetPhotosImpl mTakePhotoGrid;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sample_take_photo_list;
    }

    @Override
    protected int getShowImageGridViewId() {
        return R.id.rv_list;
    }

    @Override
    protected int getTakePhotoGridViewId() {
        return 0;
    }

    @Override
    protected void onPhotos(List<PhotoUpload> uploads) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        checkRequestPermissions(permissions, new OnPermissionResultListener() {
            @Override
            public void onResult(List<String> grants, List<String> denies) {

            }
        });
       List<String> urls = new ArrayList<>();
       urls.add("http://192.168.0.118:8082/upload/20201204/20201204172649E5UR5naT.jpg");
       urls.add("http://192.168.0.118:8082/upload/20201204/20201204172649nLkA0h0K.jpg");
       urls.add("http://192.168.0.118:8082/upload/20201204/20201204172649WFtKpW10.jpg");
       urls.add("http://192.168.0.118:8082/upload/20201204/20201204174314PYsT80qi.jpg");
       urls.add("http://192.168.0.118:8082/upload/20201204/20201204174415fZyHnni9.jpg");
       urls.add("http://192.168.0.118:8082/upload/20201204/20201204174415S6tzuWFU.jpg");
       urls.add("http://192.168.0.118:8082/upload/20201204/20201204174415J5bZsSq5.jpg");
       setImageUrls(urls);
       setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SampleTakePhotoListActivity", "onClick: ---------------------");
                //选择照片
                choosePhoto();
                //拍照
                //takePhoto(true);
            }

            @Override
            public void onDelete(int position, PhotoUpload photoUpload) {

            }
        });
    }

    class TestResource implements Comparable {
        private int score;

        public TestResource(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public int compareTo(Object o) {
            TestResource testResource = (TestResource) o;
            return testResource.score - this.score;
        }
    }
}
