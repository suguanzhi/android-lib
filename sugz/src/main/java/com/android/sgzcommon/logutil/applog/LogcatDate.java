package com.android.sgzcommon.logutil.applog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dongsheng.wu on 2016/7/19.
 */
public class LogcatDate {
    public static String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH");
        String date = format.format(new Date(System.currentTimeMillis()));
        return date;// 2012年10月03日 23:41:31  
    }

    public static String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012-10-03 23:41:31  
    }
}  