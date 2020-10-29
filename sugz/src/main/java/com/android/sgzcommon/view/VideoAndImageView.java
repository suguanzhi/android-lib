package com.android.sgzcommon.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.android.sgzcommon.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgz on 2016/a12/2.
 */

public class VideoAndImageView extends RelativeLayout {

    private int mWidth;
    private int mHeight;
    private int mFrontViewType;
    private int mCurrentItem;
    private ImageView mImageView;
    private VideoView mVideoView;
    private ItemInfo mCurrentItemInfo;
    private List<ItemInfo> mItemInfos;
    private static final int SHOW = 1;
    private static final int FRONT_VIEW_IV = 0;
    private static final int FRONT_VIEW_VV = 1;
    private static final String BACKGROUND_COLOR = "#008001";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            removeCallbacksAndMessages(null);
            switch (msg.what) {
                case SHOW:
                    synchronized (VideoAndImageView.class) {
                        if (mItemInfos.size() > mCurrentItem) {
                            mCurrentItemInfo = mItemInfos.get(mCurrentItem);
                            showCurrentAdInfo();
                            mCurrentItem++;
                            if (mCurrentItem >= mItemInfos.size()) {
                                mCurrentItem = 0;
                            }
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public VideoAndImageView(Context context) {
        super(context);
        init(context);
    }

    public VideoAndImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoAndImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mItemInfos = new ArrayList<ItemInfo>();
        mVideoView = new VideoView(getContext());
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                next(0);
                return true;
            }
        });
        LayoutParams videoParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        videoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        videoParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        videoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mVideoView, videoParams);
        mImageView = new ImageView(getContext());
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mImageView, imageParams);
        mVideoView.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
        setBackgroundColor(Color.parseColor(BACKGROUND_COLOR));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setItemInfos(List<? extends ItemInfo> itemInfos) {
        mItemInfos.clear();
        mItemInfos.addAll(itemInfos);
    }

    public void start() {
        next(0);
    }

    public void release() {
        mCurrentItem = 0;
        mFrontViewType = 0;
        mImageView.setImageBitmap(null);
        mVideoView.suspend();
        mItemInfos.clear();
        if (View.VISIBLE == mImageView.getVisibility()) {
            imageViewInvisible(null);
        }
        if (View.VISIBLE == mVideoView.getVisibility()) {
            videoViewInvisible(null);
        }
    }

    private void next(int delay) {
        mHandler.sendEmptyMessageDelayed(1, delay * 1000);
    }

    private void showCurrentAdInfo() {
        if (ItemInfo.TYPE_IMG == mCurrentItemInfo.getAdType()) {
            showImageview(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mImageView.setImageBitmap(BitmapUtils.getWindowFitBitmap(getContext(), mCurrentItemInfo.getPath()));
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mItemInfos.size() > 1) {
                        next(mCurrentItemInfo.getDuration());
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            showVideoView(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mVideoView.setVideoPath(mCurrentItemInfo.getPath());
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (mItemInfos.size() > 1) {
                                next(0);
                            } else {
                                mVideoView.seekTo(0);
                                mVideoView.start();
                            }
                        }
                    });
                    mVideoView.start();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private Point getPivot() {
        int pivotX = mWidth / 2;
        int pivotY = mHeight / 2;
        return new Point(pivotX, pivotY);
    }

    private void showImageview(final Animation.AnimationListener showListener) {
        if (FRONT_VIEW_VV == mFrontViewType) {
            videoViewInvisible(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mVideoView.resume();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imageViewVisible(showListener);
        } else {
            imageViewInvisible(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageViewVisible(showListener);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        mFrontViewType = FRONT_VIEW_IV;
    }

    private void imageViewVisible(Animation.AnimationListener listener) {
        mImageView.setVisibility(View.VISIBLE);
        mImageView.startAnimation(getAlphaAnimation(0.05f, 1f, listener));
    }

    private void imageViewInvisible(Animation.AnimationListener listener) {
        mImageView.setVisibility(View.INVISIBLE);
        mImageView.startAnimation(getAlphaAnimation(1f, 0.05f, listener));
    }

    private AlphaAnimation getAlphaAnimation(float fromX, float toX, Animation.AnimationListener listener) {
        AlphaAnimation alphaAni = new AlphaAnimation(fromX, toX);
        setAnimationConfig(alphaAni, listener);
        return alphaAni;
    }

    private void showVideoView(final Animation.AnimationListener showListener) {
        if (FRONT_VIEW_IV == mFrontViewType) {
            imageViewInvisible(null);
            videoViewVisible(showListener);
        } else {
            videoViewInvisible(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mVideoView.resume();
                    videoViewVisible(showListener);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        mFrontViewType = FRONT_VIEW_VV;
    }


    private void videoViewVisible(Animation.AnimationListener listener) {
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.startAnimation(getScaleAnimation(0f, 1f, 0f, 1f, listener));
    }

    private void videoViewInvisible(Animation.AnimationListener listener) {
        mVideoView.setVisibility(View.INVISIBLE);
        mVideoView.startAnimation(getScaleAnimation(1f, 0f, 1f, 0f, listener));
    }

    private ScaleAnimation getScaleAnimation(float fromX, float toX, float fromY, float toY, Animation.AnimationListener listener) {
        ScaleAnimation scaleAni = new ScaleAnimation(fromX, toX, fromY, toY, getPivot().x, getPivot().y);
        setAnimationConfig(scaleAni, listener);
        return scaleAni;
    }

    private void setAnimationConfig(Animation animation, Animation.AnimationListener listener) {
        animation.setDuration(1000);
        animation.setAnimationListener(listener);
        animation.setFillAfter(true);
    }

    public interface ItemInfo {

        int TYPE_IMG = 1;
        int TYPE_VIDEO = 3;

        int getAdType();

        int getDuration();

        String getPath();
    }
}
