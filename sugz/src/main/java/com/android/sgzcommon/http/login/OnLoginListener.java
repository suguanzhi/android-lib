package com.android.sgzcommon.http.login;

import com.android.sgzcommon.http.util.ResultSet;

/**
 * Created by sgz on 2019/4/26 0026.
 */
public interface OnLoginListener {

    void onSuccess(String response);

    void onFail(ResultSet set);

    void onError(String error);
}
