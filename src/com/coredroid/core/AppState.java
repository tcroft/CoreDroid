package com.coredroid.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.coredroid.data.DataStore;
import com.coredroid.util.LogIt;

/**
 * Shared state space for the entire application.  Uses serialization to 
 * keep state over app restarts
 */
public class AppState {

	private DataStore storage;
	private Map<String, CoreObject> stateMap = new HashMap<String, CoreObject>();
	
	{
		LogIt.d(AppState.class, "Creating AppState");
	}
	
	public AppState(DataStore storage) {
		if (storage == null) {
			throw new IllegalArgumentException("DataStore cannot be null");
		}
		this.storage = storage;
	}

	public void dump() {
		File file = new File("/sdcard/dump.txt");
		try {
			if (file.exists()) {
				file.delete();
			}
			
			OutputStream out = new FileOutputStream(file);
			storage.dump(out);
			out.close();
		} catch (IOException e) {
			LogIt.e(this, e);
		}
	}
	
	public synchronized void clear() {
		storage.clear();
		stateMap.clear();
	}
	
	public synchronized <E extends CoreObject> E get(Class<E> clazz) {
		return (E)get(clazz.getSimpleName());
	}
	
	public synchronized CoreObject get(String key) {
		// Lazy load
		if (!stateMap.containsKey(key)) {
			LogIt.d(this, "Cache miss: " + key);
			stateMap.put(key, storage.get(key));
		} else {
			LogIt.d(this, "Cache hit: " + key);
		}
		return stateMap.get(key);
	}

	public synchronized void set(Class clazz, CoreObject obj) {
		set(clazz.getSimpleName(), obj);
	}
	
	public synchronized void set(String key, CoreObject obj) {
		stateMap.put(key, obj);
	}
	
	/**
	 * Copy the current state to the provided data store
	 */
	public synchronized void sync() {
		LogIt.d(this, "Sync");
		for (HashMap.Entry<String, CoreObject> entry : stateMap.entrySet()) {
			long start = System.currentTimeMillis();
			CoreObject obj = entry.getValue();
			if (obj instanceof DirtyableCoreObject) {
				if (!((DirtyableCoreObject)obj).isDirty()) {
					continue;
				}
				((DirtyableCoreObject)obj).clean();
			}
			storage.save(entry.getKey(), entry.getValue());
			LogIt.d(this, "Saved: " + entry.getKey() + " - " + (System.currentTimeMillis() - start));
		}
	}
}
