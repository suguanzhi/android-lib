package com.android.sgzcommon.http.okhttp;

/**
 * Created by sgz on 2017/2/27.
 */

public class OKResponse {
    private int code;
    private String result;
    private Exception exception;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
