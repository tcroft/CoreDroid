package com.coredroid.data;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.coredroid.core.CoreObject;
import com.coredroid.util.LogIt;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Save the objects in shared preferences
 */
public class PreferencesDataStore implements DataStore {

	private static final String CLASS_SUFFIX = "-Class";
	private static final String PREFS_NAME = "AppState";
	private static final String PERSISTENT_PREFS_NAME = "PersistentPrefs";
	
	private SharedPreferences settings;
	private SharedPreferences persistentSettings;
	private Gson gson;
	
	public PreferencesDataStore(Context context) {
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		persistentSettings = context.getSharedPreferences(PERSISTENT_PREFS_NAME, 0);
		gson = createGson();
	}
	
	@Override
	public void clear() {
		settings.edit().clear().commit();
	}
	
	protected Gson createGson() {
		return new Gson();
	}
	
	@Override
	public void dump(OutputStream out) throws IOException {
		PrintStream writer = new PrintStream(out);
		
		writer.append("Persistent\n");
		for (Entry entry : persistentSettings.getAll().entrySet()) {
			writer.append("\t").append(entry.getKey().toString()).append(":").append(gson.toJson(entry.getValue())).append("\n");
		}
		writer.append("\nSettings\n");
		for (Entry entry : settings.getAll().entrySet()) {
			writer.append("\t").append(entry.getKey().toString()).append(":").append(gson.toJson(entry.getValue())).append("\n");
		}
	}
	
	@Override
	public void save(String key, CoreObject obj) {
		long start = System.currentTimeMillis();
		if (obj == null) {
			SharedPreferences prefs = persistentSettings.contains(key) ? persistentSettings : settings;
			Editor editor = prefs.edit();
			editor.remove(key);
			editor.commit();
		} else {
			SharedPreferences prefs = obj.isPersistent() ? persistentSettings : settings;
			Editor editor = prefs.edit();
			editor.putString(key, gson.toJson(obj));
			editor.putString(key + CLASS_SUFFIX, obj != null ? obj.getClass().getName() : null);
			editor.commit();
		}
		LogIt.d(this, "TIMER " + key + ": " + (System.currentTimeMillis() - start));
	}

	@Override
	public CoreObject get(String key) {
		SharedPreferences prefs = settings;
		String objString = prefs.getString(key, null);
		if (objString == null) {
			prefs = persistentSettings;
			objString = prefs.getString(key, null);
		}
		if (objString != null) {
			String classStr = prefs.getString(key + CLASS_SUFFIX, null);
			if (classStr != null) {
				try {
					Class c = Class.forName(classStr);
					return (CoreObject)(objString != null ? gson.fromJson(objString, c) : null);
				} catch (JsonSyntaxException e) {
					LogIt.d(this, "Could not parse entry: " + key);
				} catch (ClassNotFoundException e) {
					LogIt.d(this, "Could not find class for entry: " + key);
				}
			} else {
				LogIt.d(this, "Could not find class type for entry: " + key);
			}
		}
		return null;
	}

}
