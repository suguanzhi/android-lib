package com.android.sgzcommon.take_photo;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.sgzcommon.activity.PhotoViewActivity;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sgzcommon.recycleview.MarginDecoration;
import com.android.sgzcommon.take_photo.adapter.PictureGridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2020/1/10.
 */
final public class ShowImageGridImpl implements ShowImageGrid {

    Context mContext;
    RecyclerView mRecyclerView;
    PictureGridAdapter mAdapter;
    int mColumn;
    int mHorizontalMargin;
    int mVerticalMargin;
    List<String> mUrls;

    public ShowImageGridImpl(Context context, RecyclerView recyclerView) {
        this(context, recyclerView, 4, 5, 5);
    }

    public ShowImageGridImpl(Context context, RecyclerView recyclerView, int column, int horizontalMargin, int verticalmargin) {
        mContext = context;
        mRecyclerView = recyclerView;
        mColumn = column;
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalmargin;
        init();
    }

    private void init() {
        mUrls = new ArrayList<>();
        mAdapter = new PictureGridAdapter(mContext, mUrls, new BaseViewHolder.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < mUrls.size()) {
                    Intent intent = new Intent(mContext, PhotoViewActivity.class);
                    intent.putExtra("path", mUrls.get(position));
                    mContext.startActivity(intent);
                }
            }
        }, null);
        if (mRecyclerView != null) {
            GridLayoutManager grid = new GridLayoutManager(mContext, mColumn);
            MarginDecoration decoration = new MarginDecoration(mColumn, mHorizontalMargin, mVerticalMargin);
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
