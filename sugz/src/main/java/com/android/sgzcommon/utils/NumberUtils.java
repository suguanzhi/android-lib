package com.android.sgzcommon.utils;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by sgz on 2016/a12/20.
 */

public class NumberUtils {
    /**
     * 把double类型的小数转换成只保留两位小数的字符数串返回
     *
     * @param value double类型的小数
     * @return
     */
    public static String getFormatString(double value) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(value);
    }

    /**
     * 把一个字节数组转换成二进制字符串返回
     *
     * @param bytes 待转换字节数组
     * @return
     */
    public static String bytes2BinaryString(byte[] bytes) {
        String binaryString = "";
        for (int i = 0; i < bytes.length; i++) {
            binaryString = getBinaryString(bytes[i]) + binaryString;
        }
        return binaryString;
    }

    /**
     * 把十六进制字符串转换成二进制字符串返回
     *
     * @param hexs 待转换字节数组
     * @return
     */
    public static String hexs2BinaryString(String hexs) {
        String binaryString = "";
        int len = hexs.length() / 2;
        for (int i = 0; i < len; i++) {
            int start = i * 2;
            String hex = hexs.substring(start, start + 2);
            int hexInt = Integer.parseInt(hex, 16);
            String hexBinary = getBinaryString(hexInt);
            binaryString = binaryString + hexBinary;
        }
        return binaryString;
    }

    /**
     * 把一个字节转换成二进制字符串返回
     *
     * @param in 待转换字节
     * @return
     */
    public static String getBinaryString(int in) {
        String binaryString = Integer.toBinaryString(in);
        Log.i("MethodTest", "binaryString=" + binaryString);
        int len = binaryString.length();
        if (len <= 8) {
            for (int i = 0; i < 8 - len; i++) {
                binaryString = "0" + binaryString;
            }
        } else {
            binaryString = binaryString.substring(len - 8, len);
        }
        return binaryString;
    }

    /**
     * 获取一个字节的十六进制字符串
     *
     * @param i 待转换参数
     * @return
     */
    public static String getHexString(int i) {
        String result = null;
        String hex = Integer.toHexString(i);
        if (i <= 127 && i >= 0) {
            if (hex.length() == 1) {
                result = "0" + hex;
            } else {
                result = hex;
            }
        } else if (i >= -128 && i < 0) {
            result = hex.substring(hex.length() - 2, hex.length());
        }
        return result;
    }

    /**
     * 判断字符串是否都是数字
     *
     * @param text 待判断字符串
     * @return
     */
    public static boolean isDigits(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        char[] chs = text.toCharArray();
        for (char c : chs) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
