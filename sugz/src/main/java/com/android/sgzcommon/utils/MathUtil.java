package com.android.sgzcommon.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by sgz on 2019/11/5 0005.
 */
public class MathUtil {

    /**
     * @param a     除数
     * @param b     被除数
     * @param scale 保留几位小数(四舍五入)
     * @return
     */
    public static BigDecimal aDividerB(double a, double b, int scale) {
        BigDecimal aDecimal = new BigDecimal(String.valueOf(a));
        BigDecimal bDecimal = new BigDecimal(String.valueOf(b));
        return aDecimal.divide(bDecimal, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留2位小数
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal aDividerB2(double a, double b) {
        return aDividerB(a, b, 2);
    }
}
