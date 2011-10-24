package com.coredroid.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wrap the calls to a JSONObject to handle null cases
 */
public class SafeJSONObject {

	private JSONObject obj;
	
	public SafeJSONObject(JSONObject obj) {
		this.obj = obj;
	}

	public String getString(String key) {
		try {
			return obj.has(key) ? obj.getString(key) : null;
		} catch (JSONException e) {
			return null;
		}
	}

	public String getString(String key, String defaultValue) {
		try {
			return obj.has(key) ? obj.getString(key) : defaultValue;
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public int getInt(String key) {
		try {
			return obj.has(key) ? obj.getInt(key) : 0;
		} catch (JSONException e) {
			return 0;
		}
	}

	public int getInt(String key, int defaultValue) {
		try {
			return obj.has(key) ? obj.getInt(key) : defaultValue;
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public SafeJSONObject getJSONObject(String key) {
		try {
			return obj.has(key) ? new SafeJSONObject(obj.getJSONObject(key)) : null;
		} catch (JSONException e) {
			return null;
		}
		
	}
}
