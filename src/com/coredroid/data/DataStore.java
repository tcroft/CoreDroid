package com.coredroid.data;

import java.io.IOException;
import java.io.OutputStream;

import com.coredroid.core.CoreObject;

/**
 * Abstraction of object storage.  Objects stored as a key-object pair
 */
public interface DataStore {

	public void save(String key, CoreObject obj);
	public CoreObject get(String key);
	public void clear();
	public void dump(OutputStream out) throws IOException;
}
