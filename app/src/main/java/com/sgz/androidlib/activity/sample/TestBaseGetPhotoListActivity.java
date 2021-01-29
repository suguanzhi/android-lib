package com.sgz.androidlib.activity.sample;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.BaseGetPhotoListActivity;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.sgz.androidlib.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class TestBaseGetPhotoListActivity extends BaseGetPhotoListActivity {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.rv_list_show)
    RecyclerView mRvListShow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sample_take_photo_list;
    }

    @Override
    protected int getShowImageGridViewId() {
        return R.id.rv_list_show;
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
        checkRequestPermissions(permissions, new OnPermissionResultListener() {
            @Override
            public void onResult(List<String> grants, List<String> denies) {

            }
        });
        List<String> urls = new ArrayList<>();
        urls.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=121352583,3553479540&fm=26&gp=0.jpg");
        urls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2005235653,1742582269&fm=26&gp=0.jpg");
        urls.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=305022367,1443492564&fm=26&gp=0.jpg");
        urls.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3848402655,92542552&fm=26&gp=0.jpg");
        urls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1355153719,3297569375&fm=26&gp=0.jpg");
        urls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2114611637,2615047297&fm=26&gp=0.jpg");
        urls.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2941782042,3120113709&fm=26&gp=0.jpg");
        urls.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2485954775,21433741&fm=26&gp=0.jpg");
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
