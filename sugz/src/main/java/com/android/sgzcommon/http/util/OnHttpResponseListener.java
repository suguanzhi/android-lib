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
     * 操作失败
     */
    void handleFailure(ResultSet resultSet);
}
