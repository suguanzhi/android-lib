package com.android.sgzcommon.recycleview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OnMyRecycleScrollListener extends RecyclerView.OnScrollListener {

    protected abstract void onAllItemVisiable();

    protected abstract void onBottom(boolean yes);

    protected abstract void onScrolled(int dx, int dy);

    protected abstract void onIdle();

    protected abstract void onDragging();

    protected abstract void onSettling();

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (RecyclerView.SCROLL_STATE_IDLE == newState) {
            onIdle();
        } else if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
            onDragging();
        } else if (RecyclerView.SCROLL_STATE_SETTLING == newState) {
            onSettling();
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        onScrolled(dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int countItem = layoutManager.getItemCount();
        int firstCompItem = -1;
        int lastCompItem = -1;
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            firstCompItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            lastCompItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            firstCompItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            lastCompItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        }
        View lastView = layoutManager.findViewByPosition(countItem - 1);
        if (lastView != null) {
            //项数不超过屏幕
            if ((lastCompItem + 1 - firstCompItem) == countItem) {
                onAllItemVisiable();
            } else {
                //Recycleview到达底部
                if (lastCompItem == countItem - 1) {
                    onBottom(true);
                }else {
                    onBottom(false);
                }
            }
        }else {
            onBottom(false);
        }
        super.onScrolled(recyclerView, dx, dy);
    }
}