package com.android.sgzcommon.utils;

import android.content.res.Resources;
import android.util.TypedValue;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by sgz on 2016/a12/20.
 */

public class UnitUtils {
    /**
     * dp转px
     *
     * @param dp dp值
     * @return
     */
    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param px px值
     * @return
     */
    public static int px2dp(float px) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue px值
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param sp sp值
     * @return
     */
    public static int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

    public static String moneyString(double money) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(money);
    }

    public static String moneyString2(double money) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(money);
    }


    /**
     * 分换算成元，保留2位小数
     *
     * @param moneyFen
     * @return
     */
    public static BigDecimal fen2Yuan(long moneyFen) {
        return MathUtils.aDividerB2(moneyFen, 100d);
    }
}
