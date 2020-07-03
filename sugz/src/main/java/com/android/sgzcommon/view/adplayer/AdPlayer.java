package com.android.sgzcommon.view.adplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.sgzcommon.utils.BitmapUtil;
import com.android.sugz.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgz on 2018/3/1 0001.
 */

public class AdPlayer extends RelativeLayout {

    private int mCurrentPosition;
    private boolean isPlayOnce;
    private Context mContext;
    private ImageView mImageAdIV;
    private ImageView mAudioIconIV;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private LayoutParams lp;

    private AdResource mNextAdResource;
    private AdResource mCurrentAdResource;
    private List<AdResource> mAdResources;
    private OnAdPlayerListener mPlayEndListener;

    private static final int START_PLAY = 349;

    private static final String WEBVIEW = "wv";

    private static final String TAG = "AdPlayer";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_PLAY:
                    mHandler.removeMessages(START_PLAY);
                    mPlayer.stop();
                    allViewsGone();
                    if (mAdResources.size() > 0) {
                        Log.d(TAG, "handleMessage: 开始播放");
                        if (mCurrentAdResource != null && mNextAdResource != null) {
                            if (mCurrentAdResource.getPosition() == mAdResources.size() - 1 && mNextAdResource.getPosition() == 0) {
                                Log.d(TAG, "handleMessage: 完成一次播放！！！");
                                if (mPlayEndListener != null) {
                                    mPlayEndListener.onPlayEnd();
                                }
                            }
                        }
                        mCurrentPosition = -1;
                        mCurrentAdResource = mNextAdResource;
                        mNextAdResource = null;
                        if (mCurrentAdResource != null) {
                            AdResource.TYPE type = mCurrentAdResource.getType();
                            Log.d(TAG, "播放 name = " + mCurrentAdResource.getPath() + " ;position = " + mCurrentAdResource.getPosition() + " ; time = " + mCurrentAdResource.getDuration());
                            mCurrentPosition = mCurrentAdResource.getPosition();
                            //计算获取下一个资源
                            Log.d(TAG, "handleMessage: current position = " + mCurrentPosition);
                            int nextPosition = mCurrentPosition;
                            if (nextPosition < mAdResources.size() - 1) {
                                nextPosition++;
                            } else {
                                nextPosition = 0;
                            }
                            if (nextPosition < mAdResources.size()) {
                                mNextAdResource = mAdResources.get(nextPosition);
                            }
                            Log.d(TAG, "handleMessage: next position = " + nextPosition);
                            if (mCurrentAdResource.isDurationUsed()) {
                                Log.d("AdPlayer", "handleMessage: duration2Used !");
                                mCurrentAdResource.setDuration2(0);
                            }
                            long endDelay = getShowDuration(mCurrentAdResource) * 1000 + 100;
                            Log.d("AdPlayer", "handleMessage: endDelay = " + endDelay);
                            if (!isPlayOnce) {
                                Log.d(TAG, "handleMessage: is not PlayOnce!");
                                sendEmptyMessageDelayed(START_PLAY, endDelay);
                            }
                            mCurrentAdResource.setStartTime(System.currentTimeMillis());
                            mCurrentAdResource.setDurationUsed(true);
                            if (AdResource.TYPE.WEB == type || AdResource.TYPE.LIVE == type) {
                                String url = mCurrentAdResource.getPath();
                                Log.d(TAG, "播放资源！url = " + url);
                                if (AdResource.TYPE.WEB == type) {
                                    Log.d(TAG, "播放网页资源！");
                                    if (!TextUtils.isEmpty(url)) {
                                        addWebView(url);
                                    }
                                    Log.d(TAG, "播放html资源！url = " + url);
                                } else if (AdResource.TYPE.LIVE == type) {
                                    mPlayerView.setVisibility(View.VISIBLE);
                                    Log.d(TAG, "播放直播资源！url = " + url);
                                    setRtmpUri(url);
                                }
                            } else {
                                Log.d(TAG, "source is image or audio!");
                                File file = new File(mCurrentAdResource.getPath());
                                if (file.exists()) {
                                    Log.d(TAG, "资源文件存在！！！");
                                    if (AdResource.TYPE.IMAGE == type) {
                                        Log.d(TAG, "播放图片资源！");
                                        mImageAdIV.setVisibility(View.VISIBLE);
                                        Bitmap bitmap = BitmapUtil.getShowBitmap(file.getAbsolutePath(), 600, 500);
                                        mImageAdIV.setImageBitmap(bitmap);
                                    } else if (AdResource.TYPE.AUDIO == type) {
                                        startAudioIconAnimation();
                                        mPlayerView.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "播放音频资源！");
                                        setDataUri(file.getAbsolutePath());
                                    } else if (AdResource.TYPE.VEDIO == type) {
                                        mPlayerView.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "播放视频资源！");
                                        setDataUri(file.getAbsolutePath());
                                    }
                                }
                            }
                        }
                    } else {
                        mCurrentAdResource = null;
                        mNextAdResource = null;
                        mCurrentPosition = -1;
                    }
                    if (mPlayEndListener != null) {
                        mPlayEndListener.onPosition(mAdResources, mCurrentPosition);
                    }
                    isPlayOnce = false;
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public AdPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        mAdResources = new ArrayList<>();
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutParams lp2 = new LayoutParams(250, 250);
        lp2.addRule(CENTER_IN_PARENT);

        mPlayerView = new PlayerView(context);
        addView(mPlayerView, lp);

        mAudioIconIV = new ImageView(context);
        mAudioIconIV.setVisibility(GONE);
        mAudioIconIV.setScaleType(ImageView.ScaleType.FIT_XY);
        mAudioIconIV.setImageResource(R.drawable.sgz_music);
        addView(mAudioIconIV, lp2);

        mImageAdIV = new ImageView(context);
        mImageAdIV.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(mImageAdIV, lp);

        setupExoplayer();
    }

    /**
     * 初始化Exoplayer播放器
     */
    private void setupExoplayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        mPlayer.addListener(new Player.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                Log.d(TAG, "onTimelineChanged:");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.d(TAG, "onTracksChanged: ---------- " + trackGroups.length);
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.d(TAG, "onLoadingChanged: ***************** isLoading*****************" + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.d(TAG, "onPlayerStateChanged: ---------- playbackState = " + playbackState);
                if (Player.STATE_ENDED == playbackState) {
                    Log.d(TAG, "onPlayerStateChanged: ---------- STATE_ENDED!!!");
                } else if (Player.STATE_IDLE == playbackState) {
                    Log.d(TAG, "onPlayerStateChanged: ---------- STATE_IDLE！！！！");
                } else if (Player.STATE_BUFFERING == playbackState) {
                    Log.d(TAG, "onPlayerStateChanged: ---------- STATE_BUFFERING！！！！");
                } else if (Player.STATE_READY == playbackState) {
                    Log.d(TAG, "duration = " + mPlayer.getDuration());
                    Log.d(TAG, "onPlayerStateChanged: ---------- STATE_READY！！！！");
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.d("ExoPlaerLog", "onRepeatModeChanged: ");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                Log.d("ExoPlaerLog", "onShuffleModeEnabledChanged: ");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d(TAG, "onPlayerError: ---------- error = " + Log.getStackTraceString(error));
                if (mCurrentAdResource != null && AdResource.TYPE.LIVE == mCurrentAdResource.getType()) {
                    startPlay();
                }
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                Log.d("ExoPlaerLog", "onPositionDiscontinuity: reason = " + reason);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.d("ExoPlaerLog", "onPlaybackParametersChanged: ");
            }

            @Override
            public void onSeekProcessed() {
                Log.d("ExoPlaerLog", "onSeekProcessed: ");
            }
        });
        mPlayer.setPlayWhenReady(true);
        mPlayerView.setPlayer(mPlayer);
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        mPlayerView.setUseController(false);
        setVolume(100);
    }

    /**
     *
     */
    private void startAudioIconAnimation() {
        mAudioIconIV.setVisibility(VISIBLE);
        Animation animation = mAudioIconIV.getAnimation();
        if (animation == null) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(3000);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatMode(Animation.RESTART);
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            mAudioIconIV.startAnimation(rotateAnimation);
        }
    }

    /**
     *
     */
    private void stopAudioIconAnimation() {
        mAudioIconIV.setVisibility(GONE);
        mAudioIconIV.clearAnimation();
    }

    /**
     * @param adResuources
     */
    public void addAdResuources(List<? extends AdResource> adResuources) {
        mAdResources.addAll(adResuources);
    }

    /**
     * @param resizeMode
     */
    public void setResizeMode(int resizeMode) {
        mPlayerView.setResizeMode(resizeMode);
    }

    /**
     * @param volume
     */
    public void setVolume(int volume) {
        mPlayer.setVolume(volume);
    }

    /**
     * 播放视频资源
     *
     * @param uri
     */
    private void setDataUri(String uri) {
        FileDataSourceFactory dataSourceFactory = new FileDataSourceFactory(new TransferListener<FileDataSource>() {
            @Override
            public void onTransferStart(FileDataSource source, DataSpec dataSpec) {
                Log.d(TAG, "onTransferStart: ");
            }

            @Override
            public void onBytesTransferred(FileDataSource source, int bytesTransferred) {

            }

            @Override
            public void onTransferEnd(FileDataSource source) {
                Log.d(TAG, "onTransferEnd: ");
            }
        });
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(uri));
        playResource(videoSource);

    }

    /**
     * 直播
     *
     * @param uri
     */
    private void setRtmpUri(String uri) {
        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
        ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(rtmpDataSourceFactory);
        MediaSource videoSource = factory.createMediaSource(Uri.parse(uri));
        Log.d(TAG, "setRtmpUri: uri = >>>>>>>>>>>>> 2");
        playResource(videoSource);
        Log.d(TAG, "setRtmpUri: uri = >>>>>>>>>>>>> 3");
    }

    /**
     * @param mediaSource
     */
    private void playResource(MediaSource mediaSource) {
        if (mPlayer != null) {
            mPlayer.prepare(mediaSource);
        }
    }

    /**
     * @param url
     */
    private void addWebView(String url) {
        removeWebView();

        MyWebView webView = new MyWebView(mContext);
        webView.setTag(WEBVIEW);
        addView(webView, lp);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("utf-8");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldOverrideUrlLoading: ");
                if (Build.VERSION.SDK_INT >= 21) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.d(TAG, "onLoadResource: url = " + url);
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted: ");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished: ");
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d(TAG, "onReceivedError: error = " + errorCode + " ;description = " + description);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                Log.d(TAG, "onReceivedSslError: error = " + error.toString());
                super.onReceivedSslError(view, handler, error);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d(TAG, "onProgressChanged: " + newProgress + "%");
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }
        });
        webView.loadUrl(url);
    }

    /**
     *
     */
    private void removeWebView() {
        WebView wv = findViewWithTag(WEBVIEW);
        removeView(wv);
        if (wv != null) {
            wv.clearHistory();
            wv.clearCache(true);
            wv.destroy();
        }
    }

    /**
     * 释放所有播放控件及资源，将播放器还原到初始化状态
     */
    public void releaseResource() {
        stopPlay();
        allViewsGone();

        mCurrentPosition = -1;
        mNextAdResource = null;
        mCurrentAdResource = null;
        mAdResources.clear();

        mImageAdIV.setImageBitmap(null);
        if (mPlayEndListener != null) {
            mPlayEndListener.onPosition(mAdResources, mCurrentPosition);
        }
    }

    /**
     * 调用后player将不可用
     */
    public void releasePlayer() {
        releaseResource();
        mPlayer.release();
    }

    /**
     *
     */
    private void allViewsGone() {
        removeWebView();
        stopAudioIconAnimation();
        mImageAdIV.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.GONE);
    }

    /**
     * 开始播放
     */
    public void startPlay() {
        Log.d(TAG, "startPlay: ");
        if (!isQueueing()) {
            Log.d(TAG, "startPlay: !isPlaying");
            if (mAdResources.size() > 0) {
                Log.d(TAG, "startPlay: adResource.size > 0");
                if (mCurrentAdResource == null) {
                    mNextAdResource = mAdResources.get(0);
                } else {
                    mNextAdResource = mCurrentAdResource;
                }
            }
            mHandler.obtainMessage(START_PLAY).sendToTarget();
        } else {
            Log.d(TAG, "startPlay: isPlaying");
        }
    }

    /**
     * @return
     */
    public boolean isQueueing() {
        return mHandler != null && mHandler.hasMessages(START_PLAY);
    }

    /**
     * 暂停播放
     */
    public void stopPlay() {
        Log.d(TAG, "stopPlay: *********************************");
        stopQueen();
        mPlayer.stop();
    }

    /**
     * 暂停列表循环，当前播放资源播放不受影响，但是播完后不会播放下一资源
     */
    public void stopQueen() {
        Log.d(TAG, "stopQueenRecycle: ====================================");
        mHandler.removeMessages(START_PLAY);
    }

    public void startQueen() {
        isPlayOnce = false;
        if (mCurrentAdResource != null) {
            Log.d("AdPlayer", "startQueenRecycle: ==================================== ");
            long startTime = mCurrentAdResource.getStartTime();
            if (startTime > 0) {
                long duration = getShowDuration(mCurrentAdResource);
                long playedTime = System.currentTimeMillis() - startTime;
                long remainTime = duration * 1000 - playedTime;
                if (remainTime < 0) {
                    remainTime = 0;
                }
                Log.d(TAG, "startQueenRecycle: duration == " + duration);
                if (playedTime <= duration * 1000) {
                    mHandler.sendEmptyMessageDelayed(START_PLAY, remainTime);
                    return;
                }
            }
            mHandler.obtainMessage(START_PLAY).sendToTarget();
        } else {
            startPlay();
        }
    }

    /**
     * 播放指定位置资源
     *
     * @param position 待播放资源位置
     */
    public void playAdResource(int position) {
        playAdResource(position, false);
    }

    /**
     * 播放指定位置资源
     *
     * @param position 待播放资源位置
     * @param playOnce 播放一次，不进行列表循环
     */
    public void playAdResource(int position, boolean playOnce) {
        if (position < mAdResources.size()) {
            playAdResource(mAdResources.get(position), playOnce);
        }
    }

    /**
     * 播放指定资源
     *
     * @param adResource 待播放资源
     */
    public void playAdResource(AdResource adResource) {
        playAdResource(adResource, false);
    }

    /**
     * 播放指定资源
     *
     * @param adResource 待播放资源
     * @param playOnce   播放一次，不进行列表循环
     */
    private void playAdResource(AdResource adResource, boolean playOnce) {
        if (hasAdResource(adResource)) {
            stopPlay();
            isPlayOnce = playOnce;
            for (int i = 0; i < mAdResources.size(); i++) {
                mAdResources.get(i).setDuration2(0);
                mAdResources.get(i).setDurationUsed(false);
            }
            if (adResource.getType() == AdResource.TYPE.IMAGE || adResource.getType() == AdResource.TYPE.WEB) {
                adResource.setDuration2(20);
            }
            mCurrentAdResource = adResource;
            mNextAdResource = null;
            startPlay();
        }
    }

    private void initAdResourcesDuration2() {

    }

    /**
     * 是否存在资源
     *
     * @param adResource
     * @return
     */
    private boolean hasAdResource(AdResource adResource) {
        if (adResource != null) {
            for (int i = 0; i < mAdResources.size(); i++) {
                if (adResource == mAdResources.get(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取显示时长
     *
     * @param adResource
     * @return
     */
    private long getShowDuration(AdResource adResource) {
        long duration = 0;
        if (adResource != null) {
            duration = adResource.getDuration();
            if (0 != adResource.getDuration2()) {
                duration = adResource.getDuration2();
            }
        }
        return duration;
    }

    public void setOnAdPlayerListener(OnAdPlayerListener listener) {
        mPlayEndListener = listener;
    }

    public interface OnAdPlayerListener {

        void onPosition(List<AdResource> adResources, int position);

        void onPlayEnd();
    }
}
