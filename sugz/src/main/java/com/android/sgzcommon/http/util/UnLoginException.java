package com.android.sgzcommon.http.util;

/**
 * @author sgz
 * @date 2020/9/2
 */
public class UnLoginException extends Exception {

    private String message;

    public UnLoginException() {
    }

    public UnLoginException(String message) {
        this.message = message;
    }
}
