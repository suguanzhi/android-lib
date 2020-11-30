package com.android.sgzcommon.view.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.android.sgzcommon.utils.UnitUtils;
import com.android.sgzcommon.view.provider.RoundViewOutlineProvider;
import com.android.sugz.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

public class CornerImageView extends AppCompatImageView {
    private float mRadius;

    public CornerImageView(Context context) {
        this(context, null);
    }

    public CornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("CornerImageView", "CornerImageView: ");
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CornerImageView);
        mRadius = array.getDimensionPixelSize(R.styleable.CornerImageView_radius, UnitUtils.dp2px(5));
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setRoundCorner(mRadius);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setRoundCorner(mRadius);
    }

    /**
     * 为View设置圆角效果
     *
     * @param radius 圆角半径
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRoundCorner(float radius) {
        mRadius = radius;
        setClipToOutline(true);
        setOutlineProvider(new RoundViewOutlineProvider(mRadius));
    }

    /**
     * 清除View的圆角效果
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clearCornner() {
        setClipToOutline(false);
    }
}