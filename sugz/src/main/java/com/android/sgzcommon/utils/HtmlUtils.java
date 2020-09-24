package com.android.sgzcommon.utils;

/**
 * Created by sgz on 2019/10/29 0029.
 */
public class HtmlUtils {

    /**
     * 富文本适配
     */
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta chartset=\"UTF-8\"/>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\">" +
                " <style>" +
                "img{" +
                "max-width:100% !important;" +
                "padding:0px !important;" +
                "margin:0 !important;" +
                "width:auto !important; " +
                "height:auto !important;" +
                "vertical-align:top !important;" +
                "}" +
                "</style>" +
                "</head>";
        return "<!DOCTYPE html><html>" + head + "<body style='margin:0;padding:0'>" + bodyHTML + "</body></html>";
    }
}
