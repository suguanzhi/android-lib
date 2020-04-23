package com.android.sgzcommon.activity.utils;

import com.android.sgzcommon.http.okhttp.upload.UploadResultSet;

public class MUploadResultSet extends UploadResultSet {

        @Override
        protected String successCodeName() {
            return "success";
        }

        @Override
        protected String successCodeValue() {
            return "true";
        }

        @Override
        protected String dataName() {
            return "";
        }

        @Override
        protected String getMessageName() {
            return "message";
        }
    }