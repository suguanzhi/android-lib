package com.android.sgzcommon.take_photo;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.sgzcommon.activity.PhotoViewActivity;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.MarginDecoration;
import com.android.sgzcommon.take_photo.adapter.PictureGridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2020/1/10.
 */
final public class ShowImagesImpl implements ShowImages {

    Context mContext;
    RecyclerView mRecyclerView;
    PictureGridAdapter mAdapter;
    int mColumn;
    int mHorizontalMargin;
    int mVerticalMargin;
    List<String> mUrls;

    public ShowImagesImpl(Context context, RecyclerView recyclerView) {
        this(context, recyclerView, 4, 5, 5);
    }

    public ShowImagesImpl(Context context, RecyclerView recyclerView, int column, int horizontalMargin, int verticalmargin) {
        mContext = context;
        mRecyclerView = recyclerView;
        mColumn = column;
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalmargin;
        init();
    }

    private void init() {
        mUrls = new ArrayList<>();
        mAdapter = new PictureGridAdapter(mContext, mUrls, new BaseRecyclerviewAdapter.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < mUrls.size()) {
                    try {
                        Intent intent = new Intent(mContext, PhotoViewActivity.class);
                        intent.putExtra("path", mUrls.get(position));
                        mContext.startActivity(intent);
                    }catch (Exception e){
                       e.printStackTrace();
                    }
                }
            }
        }, null);
        if (mRecyclerView != null) {
            GridLayoutManager grid = new GridLayoutManager(mContext, mColumn);
            MarginDecoration decoration = new MarginDecoration(mHorizontalMargin, mVerticalMargin);
            mRecyclerView.addItemDecoration(decoration);
            mRecyclerView.setLayoutManager(grid);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void setImageUrls(List<String> urls) {
        mUrls.clear();
        if (urls != null) {
            mUrls.addAll(urls);
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyPhotoChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
