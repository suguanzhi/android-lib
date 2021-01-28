package com.android.sgzcommon.recycleview;

import android.graphics.Rect;
import android.view.View;

import com.android.sgzcommon.utils.UnitUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int hMargin;
    private int vMargin;

    public MarginDecoration(int h, int v) {
        this.hMargin = UnitUtils.dp2px(h);
        this.vMargin = UnitUtils.dp2px(v);
    }

    @Override

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int column = 1;
        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildLayoutPosition(view);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            column = gridLayoutManager.getSpanCount();
        }
        if (1 >= column) {
            if (position < count - 1) {
                outRect.set(0, vMargin, 0, 0);
            } else {
                outRect.set(0, vMargin, 0, vMargin);
            }

        } else {
            if (position % column == column - 1) {
                outRect.set(0, vMargin, hMargin, 0);
            } else {
                outRect.set(0, vMargin, hMargin, 0);
            }
        }
    }

}
