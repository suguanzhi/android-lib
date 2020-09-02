package com.android.sgzcommon.http.util;

/**
 *
 */
public interface OnHttpResponseListener {

    /**
     * 请求响应结果
     */
    void onResponse(String response);

    /**
     * 操作成功
     */
    void handleSuccess(String response, ResultSet resultSet);

    /**
     * 操作出错（处理网络错误）
     */
    void handleError(ResultSet resultSet);
}
