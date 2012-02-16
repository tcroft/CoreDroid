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
		return getString(key, null);
	}

	public String getString(String key, String defaultValue) {
		try {
			return obj.has(key) ? obj.getString(key) : defaultValue;
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public float getFloat(String key) {
		return getFloat(key, 0);
	}
	
	public float getFloat(String key, float defaultValue) {
		try {
			return obj.has(key) ? (float)obj.getDouble(key) : defaultValue;
		} catch (JSONException e) {
			return defaultValue;
		}
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
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
	
	public boolean getBool(String key) {
		try {
			return obj.has(key) ? obj.getBoolean(key) : false;
		} catch (JSONException e) {
			return false;
		}
	}
}
