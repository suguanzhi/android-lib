package com.android.sgzcommon.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.sgzcommon.volley.VolleyManager;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2016/a12/22.
 */

public abstract class BaseRecyclerviewAdapter<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final static int TYPE_NORMAL = 0;//正常内容
    protected final static int TYPE_FOOTER = 1;//下拉刷新

    protected Context mContext;
    protected List<E> mItems;
    protected LayoutInflater mInflater;
    protected ImageLoader mImageLoader;
    protected OnItemtClickListener mClickListener;
    protected OnItemtLongClickListener mClickLongListener;

    protected abstract int getItemViewId(int viewType);

    protected abstract VH getViewHolder(int viewType, View itemView);

    public BaseRecyclerviewAdapter(Context context, List<E> items) {
        this(context, items, null, null);
    }

    public BaseRecyclerviewAdapter(Context context, List<E> items, OnItemtClickListener clickListener, OnItemtLongClickListener longClickListener) {
        mContext = context;
        mItems = items;
        mInflater = LayoutInflater.from(context);
        mClickListener = clickListener;
        mClickLongListener = longClickListener;
        mImageLoader = VolleyManager.getInstance(mContext).getImageLoaderInstance();
    }

    public void setOnItemtClickListener(OnItemtClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemtLongClickListener(OnItemtLongClickListener listener) {
        mClickLongListener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(getItemViewId(viewType), parent, false);
        VH holder = getViewHolder(viewType, view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mClickLongListener != null) {
                    mClickLongListener.onItemLongClick(v, holder.getLayoutPosition());
                }
                return false;
            }
        });
        return holder;
    }


    public void addItemData(E e, int position) {
        mItems.add(position, e);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public E getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnItemtClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemtLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
