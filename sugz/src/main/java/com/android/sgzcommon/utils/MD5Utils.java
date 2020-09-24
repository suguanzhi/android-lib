package com.android.sgzcommon.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * 获取md5加密后的字符串
     * @param s 被加密字符串
     * @return 加密后字符串
     */
    public static String getMD5(String s){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(s.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] m = md5.digest();
        return getString(m);
    }

    /**
     * 把字节数组转成16进制的字符表示
     * @param b 加密后的字节数据
     * @return 转换后的字符串
     */
    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int validByte = b[i] & 0xff;
            String numberStr = Integer.toHexString(validByte);
            if (numberStr.length()==1) {
                sb.append("0");
            }
            sb.append(numberStr);
        }
        return sb.toString();
    }
}
