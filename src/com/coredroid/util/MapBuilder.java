package com.coredroid.util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Simplified map building for String, String pairs.
 * Typical syntax:
 * 
 * Map<String, String> map = new MapBuilder<String, String>().put(new Object[][] {
 * 		{"one", "1"},
 * 		{"two", "2"}
 * }).toMap();
 */
public class MapBuilder<T, K> {
	private Map<T, K> map = new HashMap<T, K>();

	public Map<T, K> toMap() {
		return map;
	}

	public MapBuilder<T, K> put(T key, K value) {
		map.put(key, value);
		return this;
	}

	public MapBuilder<T, K> put(Object[]... pairs) {

		for (Object[] pair : pairs) {
			if (pair == null) {
				continue;
			}
			if (pair.length != 2) {
				throw new IllegalStateException("Invalid pair found: " + Arrays.deepToString(pair));
			}
			map.put((T)pair[0], (K)pair[1]);
		}

		return this;
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
	
}