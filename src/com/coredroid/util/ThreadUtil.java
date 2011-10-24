package com.coredroid.util;

/**
 * Miscellaneous methods supporting thread usage
 */
public class ThreadUtil {

	/**
	 * Block the current thread for the given number of milliseconds.  Ignores interrupt exceptions
	 */
	public static void sleep(long millis) {
		synchronized(ThreadUtil.class) {
			try {
				Thread.sleep(millis);
			} catch (Throwable t) {
				// Don't care.
			}
		}
	}
}
