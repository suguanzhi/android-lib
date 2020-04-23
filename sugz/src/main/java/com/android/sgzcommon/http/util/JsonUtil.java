package com.android.sgzcommon.http.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    /**
     * 获取属性
     *
     * @param result
     * @param name
     * @return
     */
    public static Object getAttribute(JSONObject result, String name) {
        try {
            return result.get(name);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取属性
     *
     * @param result
     * @param name
     * @return
     */
    public static JSONObject setAttribute(JSONObject result, String name, Object value) {
        try {
            return result.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON转map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2Map(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator it = json.keys();
        while (it.hasNext()) {
            String name = (String) it.next();
            Object value = null;
            try {
                value = json.get(name);
                map.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<String>();
        JSONObject json = new JSONObject();
        json.put("access_token", "123244");
        json.put("expires_in", 7200 * 3600);
        json.put("array", list);

        Object o = json.get("array");
    }
}
