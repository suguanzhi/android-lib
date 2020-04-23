package com.android.sgzcommon.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.ImageView;

/**
 * Created by sgz on 2017/4/25.
 */

public class DrawableUtil {

    /**
     * 设置底部tab图标
     *
     * @paramradioButton控件
     * @paramdrawableNormal常态时的图片
     * @paramdrawableSelect选中时的图片
     */

    public static void setSelectorDrawable(ImageView imageView, Drawable drawableNormal, Drawable drawableSelect) {
        StateListDrawable drawable = new StateListDrawable();
        //选中
        drawable.addState(new int[]{android.R.attr.state_pressed}, drawableSelect);
        //未选中
        drawable.addState(new int[]{}, drawableNormal);
        imageView.setImageDrawable(drawable);
    }
}
