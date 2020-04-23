package com.android.sgzcommon.http.util;

import java.util.Map;

public class UrlUtil {

	public static String autowired(String url, Map<String, Object> data) {
		
		if (null == data || 0 == data.size()) return url;
		
		StringBuilder builder = new StringBuilder(url);
		builder.append((url.indexOf('?') == -1)? '?' : '&');
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			builder.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}
