package com.android.sgzcommon.take_photo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sgzcommon.http.okhttp.upload.UploadEntity;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnDeletePhotoListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoClickListener;
import com.android.sgzcommon.utils.BitmapUtil;
import com.android.sgzcommon.utils.FileUtil;
import com.android.sgzcommon.utils.UnitUtil;
import com.android.sgzcommon.view.imageview.CornerImageView;
import com.android.sugz.R;

import java.util.List;

/**
 * Created by sgz on 2019/5/6 0006.
 */
public class PictureGridEditAdapter extends BaseRecyclerviewAdapter<PhotoUpload,BaseViewHolder> {

    private OnDeletePhotoListener mDeletePhotoListener;
    private OnTakePhotoClickListener mTakePhotoClickListener;

    public PictureGridEditAdapter(Context context, List photoUploads, OnItemtClickListener clickListener, OnItemtLongClickListener longClickListener) {
        super(context, photoUploads, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        Log.d("PictureGridEditAdapter", "getView: position = " + position);
        if (0 == getItemViewType(position)) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            final PhotoUpload upload = mItems.get(position);
            upload.setOnProgressListener(new PhotoUpload.OnProgressListener() {
                @Override
                public void onProgress(int progress) {
                    Log.d("PictureGridEditAdapter", "onProgress: id = " + Thread.currentThread().getId());
                    updatePgrogress(viewHolder, progress);
                    Log.d("PictureGridEditAdapter", "onProgress: position == " + position + "; progress == " + progress);
                }

                @Override
                public void onState(UploadEntity.STATE state) {
                    updateState(viewHolder, state);
                }
            });
            viewHolder.mIvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //聚焦，防止自动滚动
                    //                    holder.itemView.setFocusable(true);
                    //                    holder.itemView.setFocusableInTouchMode(true);
                    //                    holder.itemView.requestFocus();
                    try {
                        mItems.remove(position);
                        String path = upload.getPath();
                        FileUtil.deleteFile(path);
                        if (mDeletePhotoListener != null) {
                            mDeletePhotoListener.onDelete(position, upload);
                        }
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            int progress = upload.getProgress();
            updatePgrogress(viewHolder, progress);
            UploadEntity.STATE state = upload.getState();
            updateState(viewHolder, state);
            String path = upload.getPath();
            Bitmap bitmap = BitmapUtil.getShowBitmap(path, 100, 200);
            viewHolder.mIvImage.setImageBitmap(bitmap);
            viewHolder.mIvImage.setRoundCorner(UnitUtil.dp2px(5));
        } else {
            AddViewHolder viewHolder = (AddViewHolder) holder;
            viewHolder.mLlAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTakePhotoClickListener != null) {
                        mTakePhotoClickListener.onClick(v);
                    }
                }
            });
        }
    }

    private void updateState(ViewHolder holder, UploadEntity.STATE state) {
        if (PhotoUpload.STATE.STATE_SUCCESS == state) {
            holder.mIvDelete.setVisibility(View.GONE);
            holder.mTvProgress.setVisibility(View.GONE);
            holder.mPbProgress.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.VISIBLE);
            holder.mIvUploadState.setImageResource(R.drawable.ic_sgz_success);
        } else if (PhotoUpload.STATE.STATE_UPLOADING == state) {
            holder.mTvProgress.setVisibility(View.VISIBLE);
            holder.mPbProgress.setVisibility(View.VISIBLE);
            holder.mIvDelete.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.GONE);
        } else if (PhotoUpload.STATE.STATE_FAIL == state) {
            holder.mIvDelete.setVisibility(View.VISIBLE);
            holder.mTvProgress.setVisibility(View.GONE);
            holder.mPbProgress.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.VISIBLE);
            holder.mIvUploadState.setImageResource(R.drawable.ic_sgz_warm_red);
        } else {
            holder.mIvDelete.setVisibility(View.VISIBLE);
            holder.mPbProgress.setVisibility(View.GONE);
            holder.mTvProgress.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.GONE);
        }
    }

    private void updatePgrogress(ViewHolder holder, int progress) {
        holder.mTvProgress.setText(progress + "%");
        holder.mPbProgress.setProgress(progress);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mItems.size()) {
            return 1;
        }
        return 0;
    }

    @Override
    protected int getItemViewId(int viewType) {
        if (0 == viewType) {
            return R.layout.adapter_sgz_picture_grid_edit;
        }
        return R.layout.adapter_sgz_add_icon;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View itemView) {
        if (0 == viewType) {
            return new ViewHolder(itemView);
        }
        return new AddViewHolder(itemView);
    }

    public class ViewHolder extends BaseViewHolder {
        CornerImageView mIvImage;
        ImageView mIvDelete;
        ProgressBar mPbProgress;
        TextView mTvProgress;
        ImageView mIvUploadState;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.civ_image);
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

    public void setOnDeletePhotoListener(OnDeletePhotoListener listener) {
        this.mDeletePhotoListener = listener;
    }

    public void setOnTakePhotoClickListener(OnTakePhotoClickListener listener) {
        this.mTakePhotoClickListener = listener;
    }
}
