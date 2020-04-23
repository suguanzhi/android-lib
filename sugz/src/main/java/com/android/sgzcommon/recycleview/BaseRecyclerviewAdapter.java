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

public abstract class BaseRecyclerviewAdapter<VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    protected final static int TYPE_NORMAL = 0;//正常内容
    protected final static int TYPE_FOOTER = 1;//下拉刷新

    protected Context mContext;
    protected List<Object> mObjects;
    protected LayoutInflater mInflater;
    protected ImageLoader mImageLoader;
    protected BaseViewHolder.OnItemtClickListener mClickListener;
    protected BaseViewHolder.OnItemtLongClickListener mClickLongListener;

    protected abstract int getItemViewId(int viewType);

    protected abstract VH getViewHolder(int viewType, View itemView);

    public BaseRecyclerviewAdapter(Context context, List<? extends Object> objects, BaseViewHolder.OnItemtClickListener clickListener, BaseViewHolder.OnItemtLongClickListener longClickListener) {
        mContext = context;
        mObjects = (List<Object>) objects;
        mInflater = LayoutInflater.from(context);
        mClickListener = clickListener;
        mClickLongListener = longClickListener;
        mImageLoader = VolleyManager.getInstance(mContext).getImageLoaderInstance();
    }

    public void setOnItemtClickListener(BaseViewHolder.OnItemtClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemtLongClickListener(BaseViewHolder.OnItemtLongClickListener listener) {
        mClickLongListener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(getItemViewId(viewType), parent, false);
        VH holder = getViewHolder(viewType,view);
        holder.setListener(mClickListener);
        holder.setLongListener(mClickLongListener);
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
}
