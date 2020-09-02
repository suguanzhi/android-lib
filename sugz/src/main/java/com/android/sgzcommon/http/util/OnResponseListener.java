package com.android.sgzcommon.http.util;

public interface OnResponseListener<V> {
    /**
     * 操作成功
     */
    void onSuccess(String response, V v);

    /**
     * 操作失败
     */
    void onFailure(ResultSet resultSet);

}