package com.android.sgzcommon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sgzcommon.adapter.utils.CircleTransform;
import com.android.sgzcommon.adapter.utils.ImageObject;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sgzcommon.utils.UnitUtil;
import com.android.sugz.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by sgz on 2019/5/6 0006.
 */
public class PictureGridEditAdapter extends BaseRecyclerviewAdapter<BaseViewHolder> {

    private OnClickListener listener;

    public PictureGridEditAdapter(Context context, List<? extends ImageObject> entities, BaseViewHolder.OnItemtClickListener clickListener, BaseViewHolder.OnItemtLongClickListener longClickListener) {
        super(context, entities, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Log.d("PictureGridEditAdapter", "getView: position = " + position);
        if (0 == getItemViewType(position)) {
            final ImageObject entity = (ImageObject) mObjects.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mIvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //聚焦，防止自动滚动
                    //                    holder.itemView.setFocusable(true);
                    //                    holder.itemView.setFocusableInTouchMode(true);
                    //                    holder.itemView.requestFocus();
                    try {
                        mObjects.remove(entity);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            int state = entity.getState();
            int progress = entity.getProgress();
            viewHolder.mTvProgress.setText(progress + "%");
            viewHolder.mPbProgress.setProgress(progress);
            if (ImageObject.STATE_SUCCESS == state) {
                viewHolder.mIvDelete.setVisibility(View.GONE);
                viewHolder.mTvProgress.setVisibility(View.GONE);
                viewHolder.mPbProgress.setVisibility(View.GONE);
                viewHolder.mIvUploadState.setVisibility(View.VISIBLE);
                viewHolder.mIvUploadState.setImageResource(R.drawable.ic_success);
            } else if (ImageObject.STATE_UPLOADING == state) {
                viewHolder.mTvProgress.setVisibility(View.VISIBLE);
                viewHolder.mPbProgress.setVisibility(View.VISIBLE);
                viewHolder.mIvDelete.setVisibility(View.GONE);
                viewHolder.mIvUploadState.setVisibility(View.GONE);
            } else if (ImageObject.STATE_FAIL == state) {
                viewHolder.mIvDelete.setVisibility(View.VISIBLE);
                viewHolder.mTvProgress.setVisibility(View.GONE);
                viewHolder.mPbProgress.setVisibility(View.GONE);
                viewHolder.mIvUploadState.setVisibility(View.VISIBLE);
                viewHolder.mIvUploadState.setImageResource(R.drawable.ic_warm_red);
            } else {
                viewHolder.mIvDelete.setVisibility(View.VISIBLE);
                viewHolder.mPbProgress.setVisibility(View.GONE);
                viewHolder.mTvProgress.setVisibility(View.GONE);
                viewHolder.mIvUploadState.setVisibility(View.GONE);
            }
            String path = entity.getPath();
            Picasso.with(mContext).load(new File(path)).fit().centerCrop().transform(new CircleTransform(300, 300, UnitUtil.dp2px(6))).into(viewHolder.mIvImage);
        } else {
            AddViewHolder viewHolder = (AddViewHolder) holder;
            viewHolder.mLlAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCameraClick(v);
                    }
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mObjects.size()) {
            return 1;
        }
        return 0;
    }

    @Override
    protected int getItemViewId(int viewType) {
        if (0 == viewType) {
            return R.layout.adapter_picture_grid_edit;
        }
        return R.layout.adapter_add_icon;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View itemView) {
        if (0 == viewType) {
            return new ViewHolder(itemView);
        }
        return new AddViewHolder(itemView);
    }

    public class ViewHolder extends BaseViewHolder {
        ImageView mIvImage;
        ImageView mIvDelete;
        ProgressBar mPbProgress;
        TextView mTvProgress;
        ImageView mIvUploadState;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mIvDelete = itemView.findViewById(R.id.iv_delete);
            mPbProgress = itemView.findViewById(R.id.pb_progress);
            mTvProgress = itemView.findViewById(R.id.tv_progress_tip);
            mIvUploadState = itemView.findViewById(R.id.iv_upload_state);
        }
    }

    public class AddViewHolder extends BaseViewHolder {
        LinearLayout mLlAdd;

        public AddViewHolder(View itemView) {
            super(itemView);
            mLlAdd = itemView.findViewById(R.id.ll_add);
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {

        void onCameraClick(View view);
    }


}
