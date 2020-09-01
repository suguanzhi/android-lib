package com.android.sgzcommon.http.util;

public interface OnResponseListener<V> {
    /**
     * 操作成功
     */
    void onSuccess(String response, V v);

    /**
     * 操作失败
     */
    void onFailed(ResultSet resultSet);

    /**
     * 操作出错（处理网络错误）
     */
    void onError(Exception error);
}