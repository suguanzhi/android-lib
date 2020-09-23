package com.android.sgzcommon.recycleview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sugz.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by sgz on 2019/11/25.
 */
public abstract class BaseRefreshRecycleviewAdapter<E,V extends BaseViewHolder> extends BaseRecyclerviewAdapter<E,BaseViewHolder> {

    private long mTotalItemCount;
    private RecyclerView mRecyclerView;

    protected abstract int getNormalViewId(int viewType);

    protected abstract V getNormalViewHolder(int viewType, View itemView);

    protected abstract void onBindNormalViewHolder(V v, int position);


    @Override
    protected int getItemViewId(int viewType) {
        if (TYPE_FOOTER == viewType) {
            return R.layout.adapter_sgz_foot;
        }
        return getNormalViewId(viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mItems.size()) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public BaseRefreshRecycleviewAdapter(Context context, List list, OnItemtClickListener clickListener, OnItemtLongClickListener longClickListener) {
        super(context, list, clickListener, longClickListener);
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View itemView) {
        if (TYPE_FOOTER == viewType) {
            return new FootViewHolder(itemView);
        }
        return getNormalViewHolder(viewType, itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (TYPE_FOOTER == getItemViewType(position)) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
        } else {
            onBindNormalViewHolder((V) holder, position);
        }
    }

    public void setRecycleView(RecyclerView recycleView, OnEndListener listener) {
        mRecyclerView = recycleView;
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
                    } else {
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    Log.d("BaseRefreshRecycleviewAdapter", "onScrolled: 1");
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int countItem = layoutManager.getItemCount();
                    int firstCompItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastItem = layoutManager.findLastVisibleItemPosition();
                    int lastCompItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    View lastView = layoutManager.findViewByPosition(countItem - 1);
                    //项数不超过屏幕
                    if ((lastCompItem + 1 - firstCompItem) == countItem) {
                        if (lastView != null) {
                            lastView.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (lastView != null) {
                            lastView.setVisibility(View.VISIBLE);
                            TextView tipView = lastView.findViewById(R.id.tv_tip);
                            ProgressBar progressView = lastView.findViewById(R.id.pb_loading);
                            //最后一项不完全显露
                            if (lastItem == countItem - 1) {
                                if (isAllDataLoad()) {
                                    tipView.setVisibility(View.VISIBLE);
                                    progressView.setVisibility(View.GONE);
                                } else {
                                    tipView.setVisibility(View.GONE);
                                }
                            }
                            //最后一项完全显露出来
                            if (lastCompItem == countItem - 1) {
                                if (listener != null) {
                                    listener.onViewEnd();
                                    if (isAllDataLoad()) {
                                        listener.onDataEnd();
                                    } else {
                                        progressView.setVisibility(View.VISIBLE);
                                        listener.onLoad();
                                    }
                                }
                            }
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
        notifyDataSetChanged();
    }

    private boolean isAllDataLoad() {
        return mTotalItemCount != 0 && mItems.size() >= mTotalItemCount;
    }


    public void notifyDataSetChanged(long total) {
        mTotalItemCount = total;
        notifyDataSetChanged();
    }

    public interface OnEndListener {
        void onViewEnd();

        void onDataEnd();

        void onLoad();
    }

    class FootViewHolder extends BaseViewHolder {

        ProgressBar mPbLoading;
        TextView mTvTip;

        public FootViewHolder(View itemView) {
            super(itemView);
            mPbLoading = itemView.findViewById(R.id.pb_loading);
            mTvTip = itemView.findViewById(R.id.tv_tip);
        }
    }

}
