package com.sgz.androidlib.activity.sample;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.TakePhotosActivity;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.TakePhotosImpl;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoClickListener;
import com.sgz.androidlib.R;

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
public class TestTakePhotosActivity extends TakePhotosActivity {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    private TakePhotosImpl mTakePhotoGrid;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sample_take_photo_list;
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
    protected void onPhotos(List<PhotoUpload> uploads) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        checkRequestePermissions(permissions, new OnPermissionResultListener() {
            @Override
            public void onResult(List<String> grants, List<String> denies) {

            }
        });
        PriorityQueue<TestResource> queue = new PriorityQueue<>();
        TestResource resource1 = new TestResource(98);
        TestResource resource2 = new TestResource(21);
        TestResource resource3 = new TestResource(101);
        TestResource resource4 = new TestResource(52);
        TestResource resource5 = new TestResource(121);
        queue.add(resource1);
        queue.add(resource2);
        queue.add(resource3);
        queue.add(resource4);
        queue.add(resource5);
        Log.d("MainActivity", "onCreate: " + queue.peek().score);
        Log.d("MainActivity", "onCreate: size == " + queue.size());
        Log.d("MainActivity", "onCreate: " + queue.element().score);
        Log.d("MainActivity", "onCreate: size == " + queue.size());
        Log.d("MainActivity", "onCreate: " + queue.element().score);
        Log.d("MainActivity", "onCreate: size == " + queue.size());
        Log.d("MainActivity", "onCreate: " + queue.element().score);
        Log.d("MainActivity", "onCreate: size == " + queue.size());
        Log.d("MainActivity", "onCreate: " + queue.element().score);
        Log.d("MainActivity", "onCreate: size == " + queue.size());
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

        setOnTakePhotoClickListener(new OnTakePhotoClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SampleTakePhotoListActivity", "onClick: ---------------------");
                //选择照片
                //                choosePhoto();
                //拍照
                takePhoto(null);
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
