package com.android.sgzcommon.http.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by sgz on 2019/4/26 0026.
 */
public class LoginUtil {

    protected static final String NAME = "login";
    private static final String TOKEN = "token";
    private static final String LOGIN_NAME = "loginname";
    private static final String PASSWORD = "password";

    public static void saveLoginToken(Context context, String token) {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(TOKEN, token).apply();
    }

    public static String getLoginToken(Context context) {
        SharedPreferences sf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String token = sf.getString(TOKEN, "");
        if (TextUtils.isEmpty(token)){
            token = "EC196802B7FB44CEDA0FEFE0EBCF1B71";
        }
        return token;
    }

    public static void saveLoginName(Context context, String username) {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(LOGIN_NAME, username).apply();
    }

    public static String getLoginName(Context context) {
        SharedPreferences sf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sf.getString(LOGIN_NAME, "");
    }

    public static void savePassword(Context context, String password) {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(PASSWORD, password).apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences sf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sf.getString(PASSWORD, "");
    }
}
