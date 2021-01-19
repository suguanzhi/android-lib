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
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.android.sgzcommon.utils.BitmapUtils;
import com.android.sgzcommon.utils.FileUtils;
import com.android.sgzcommon.utils.UnitUtils;
import com.android.sgzcommon.view.imageview.CornerImageView;
import com.android.sugz.R;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by sgz on 2019/5/6 0006.
 */
public class PictureGridEditAdapter extends BaseRecyclerviewAdapter<PhotoUpload, BaseViewHolder> {

    private OnPhotoClickListener mPhotoClickListener;

    public PictureGridEditAdapter(Context context, List photoUploads, OnItemtClickListener clickListener, OnItemtLongClickListener longClickListener) {
        super(context, photoUploads, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    protected void onBindViewHolder(@NonNull BaseViewHolder holder, int position, final PhotoUpload photoUpload) {
        Log.d("PictureGridEditAdapter", "getView: position = " + position);
        if (0 == getItemViewType(position)) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            photoUpload.setOnProgressListener(new PhotoUpload.OnProgressListener() {
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
                    Log.d("PictureGridEditAdapter", "onClick: ");
                    final String path = photoUpload.getPath();
                    int p = -1;
                    for (int i = 0; i < mItems.size(); i++) {
                        if (path.equals(mItems.get(i).getPath())) {
                            p = i;
                            break;
                        }
                    }
                    if (p >= 0 && p < mItems.size()) {
                        mItems.remove(p);
                        notifyItemRemoved(p);
                    }
                    if (mPhotoClickListener != null) {
                        mPhotoClickListener.onDelete(position, photoUpload);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FileUtils.deleteFile(path);
                        }
                    }).start();
                }
            });
            int progress = photoUpload.getProgress();
            updatePgrogress(viewHolder, progress);
            UploadEntity.STATE state = photoUpload.getState();
            updateState(viewHolder, state);
            String path = photoUpload.getPath();
            Bitmap bitmap = null;
            File file = new File(path);
            if (file.exists()) {
                bitmap = BitmapUtils.getShowBitmap(path, 100, 200);
            }
            viewHolder.mIvImage.setImageBitmap(bitmap);
            viewHolder.mIvImage.setRoundCorner(UnitUtils.dp2px(5));
        } else {
            AddViewHolder viewHolder = (AddViewHolder) holder;
            viewHolder.mLlAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPhotoClickListener != null) {
                        mPhotoClickListener.onClick(v);
                    }
                }
            });
        }
    }

    private void updateState(ViewHolder holder, UploadEntity.STATE state) {
        if (PhotoUpload.STATE.STATE_LOADING == state) {
            holder.mPbLoading.setVisibility(View.VISIBLE);
            holder.mIvDelete.setVisibility(View.GONE);
            holder.mTvProgress.setVisibility(View.GONE);
            holder.mPbProgress.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.GONE);
        } else if (PhotoUpload.STATE.STATE_UPLOAD_SUCCESS == state) {
            holder.mIvDelete.setVisibility(View.GONE);
            holder.mTvProgress.setVisibility(View.GONE);
            holder.mPbProgress.setVisibility(View.GONE);
            holder.mPbLoading.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.VISIBLE);
            holder.mIvUploadState.setImageResource(R.drawable.ic_sgz_success);
        } else if (PhotoUpload.STATE.STATE_UPLOADING == state) {
            holder.mTvProgress.setVisibility(View.VISIBLE);
            holder.mPbProgress.setVisibility(View.VISIBLE);
            holder.mIvDelete.setVisibility(View.GONE);
            holder.mPbLoading.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.GONE);
        } else if (PhotoUpload.STATE.STATE_UPLOAD_FAIL == state) {
            holder.mIvDelete.setVisibility(View.VISIBLE);
            holder.mTvProgress.setVisibility(View.GONE);
            holder.mPbProgress.setVisibility(View.GONE);
            holder.mPbLoading.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.VISIBLE);
            holder.mIvUploadState.setImageResource(R.drawable.ic_sgz_warm_red);
        } else {
            holder.mIvDelete.setVisibility(View.VISIBLE);
            holder.mPbProgress.setVisibility(View.GONE);
            holder.mTvProgress.setVisibility(View.GONE);
            holder.mIvUploadState.setVisibility(View.GONE);
            holder.mPbLoading.setVisibility(View.GONE);
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
        ProgressBar mPbLoading;
        TextView mTvProgress;
        ImageView mIvUploadState;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.civ_image);
            mIvDelete = itemView.findViewById(R.id.iv_delete);
            mPbProgress = itemView.findViewById(R.id.pb_progress);
            mPbLoading = itemView.findViewById(R.id.pb_loading);
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

    public void setOnTakePhotoClickListener(OnPhotoClickListener listener) {
        this.mPhotoClickListener = listener;
    }
}
