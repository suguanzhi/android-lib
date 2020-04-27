package com.android.sgzcommon.recycleview;

import android.graphics.Rect;
import android.view.View;

import com.android.sgzcommon.utils.UnitUtil;

import androidx.recyclerview.widget.RecyclerView;

public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int hmargin;
    private int vmargin;
    private int column;

    public MarginDecoration(int column, int hmargin,int vmargin) {
        this.hmargin = UnitUtil.dp2px(hmargin);
        this.vmargin = UnitUtil.dp2px(vmargin);
        this.column = column;
    }

    @Override

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //由于每行都只有2个，所以第一个都是2的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % column == 0) {
            outRect.set(hmargin, vmargin, hmargin, 0);
        } else {
            outRect.set(0, vmargin, hmargin, 0);
        }

    }

}
