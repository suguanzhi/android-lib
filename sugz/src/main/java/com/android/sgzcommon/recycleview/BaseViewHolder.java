package com.android.sgzcommon.recycleview;

/**
 * Created by sgz on 2019/4/18 0018.
 */

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private int mCurrPosition = -1;
    private OnItemtClickListener mListener;
    private OnItemtLongClickListener mLongListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setListener(OnItemtClickListener listener) {
        mListener = listener;
    }

    public void setLongListener(OnItemtLongClickListener longListener) {
        mLongListener = longListener;
    }

    @Override
    public void onClick(View v) {
        mCurrPosition = getLayoutPosition();
        if (mListener != null && mCurrPosition >= 0) {
            mListener.onItemClick(v, mCurrPosition);
        }
    }


    @Override
    public boolean onLongClick(View v) {
        if (mLongListener != null && mCurrPosition >= 0) {
            mLongListener.onItemLongClick(v, mCurrPosition);
        }
        return false;
    }

    public interface OnItemtClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemtLongClickListener {
        void onItemLongClick(View view, int position);
    }
}


