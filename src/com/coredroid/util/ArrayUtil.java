package com.coredroid.util;

public class ArrayUtil {

	public static int indexOf(Object[] array, Object item) {
		if (item == null || array == null) {
			return -1;
		}
		
		for (int i = 0; i < array.length; i++) {
			Object o = array[i];
			if (o == item) {
				return i;
			}
			
			if (o != null) {
				if (o.equals(item)) {
					return i;
				}
			}
		}
		return -1;
	}
}
