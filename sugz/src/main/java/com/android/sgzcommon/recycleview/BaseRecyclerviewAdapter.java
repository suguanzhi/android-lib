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

public abstract class BaseRecyclerviewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final static int TYPE_NORMAL = 0;//正常内容
    protected final static int TYPE_FOOTER = 1;//下拉刷新

    protected Context mContext;
    protected List<Object> mObjects;
    protected LayoutInflater mInflater;
    protected ImageLoader mImageLoader;
    protected OnItemtClickListener mClickListener;
    protected OnItemtLongClickListener mClickLongListener;

    protected abstract int getItemViewId(int viewType);

    protected abstract VH getViewHolder(int viewType, View itemView);

    public BaseRecyclerviewAdapter(Context context, List<? extends Object> objects) {
        this(context, objects, null, null);
    }

    public BaseRecyclerviewAdapter(Context context, List<? extends Object> objects, OnItemtClickListener clickListener, OnItemtLongClickListener longClickListener) {
        mContext = context;
        mObjects = (List<Object>) objects;
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


    public void addItemData(Object obj, int position) {
        mObjects.add(position, obj);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mObjects.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public interface OnItemtClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemtLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
