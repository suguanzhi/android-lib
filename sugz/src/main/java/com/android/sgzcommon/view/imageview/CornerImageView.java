package com.android.sgzcommon.view.imageview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.android.sgzcommon.view.provider.CircleViewOutlineProvider;
import com.android.sgzcommon.view.provider.RoundViewOutlineProvider;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

public class CornerImageView extends AppCompatImageView {
    public CornerImageView(Context context) {
        this(context, null);
    }

    public CornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 为View设置圆角效果
     *
     * @param radius 圆角半径
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRoundCorner(float radius) {
        setClipToOutline(true);// 用outline裁剪内容区域
        setOutlineProvider(new RoundViewOutlineProvider(radius));
    }

    /**
     * 设置View为圆形
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setCircle() {
        setClipToOutline(true);
        setOutlineProvider(new CircleViewOutlineProvider());
    }

    /**
     * 清除View的圆角效果
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clearCornner() {
        setClipToOutline(false);
    }
}