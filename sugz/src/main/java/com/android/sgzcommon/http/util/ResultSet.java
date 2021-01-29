package com.android.sgzcommon.http.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public abstract class ResultSet {

    /**
     * 网络连接状态code，200，500，,404等
     */
    private int netCode;
    private boolean success;
    private String message;
    private String data;
    private String dataName;
    private String successCodeValue;
    private String response;
    private JSONObject result;
    private Exception error;

    /**
     * 状态码name，如："errorCode"
     *
     * @return
     */
    protected abstract String successCodeName();

    /**
     * 成功状态码，如："000000"
     *
     * @return
     */
    protected abstract String successCodeValue();

    /**
     * 返回数据(result)的name
     *
     * @return
     */
    protected abstract String dataName();

    /**
     * 状态信息
     *
     * @return
     */
    protected abstract String getMessageName();

    public ResultSet() {
    }

    public ResultSet(String dataName) {
        this.dataName = dataName;
    }

    private void parseResult(String response) {
        this.response = response;
        if (TextUtils.isEmpty(dataName)) {
            dataName = dataName();
        }
        try {
            this.result = new JSONObject(response);
            try {
                Log.d("ResultSet", "parseResult: successCodeValue");
                successCodeValue = result.getString(successCodeName());
                if (!TextUtils.isEmpty(successCodeValue()) && successCodeValue().equals(successCodeValue)) {
                    Log.d("ResultSet", "parseResult: ");
                    success = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.d("ResultSet", "parseResult: message");
                message = result.getString(getMessageName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.d("ResultSet", "parseResult: data");
                data = result.getString(dataName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            message = "result JSONException:" + Log.getStackTraceString(e);
        } catch (Exception e) {
            e.printStackTrace();
            message = Log.getStackTraceString(e);
        }
    }

    public int getNetCode() {
        return netCode;
    }

    public void setNetCode(int netCode) {
        this.netCode = netCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getSuccessCodeValue() {
        return successCodeValue;
    }

    public void setSuccessCodeValue(String successCodeValue) {
        this.successCodeValue = successCodeValue;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
        parseResult(response);
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }
}
