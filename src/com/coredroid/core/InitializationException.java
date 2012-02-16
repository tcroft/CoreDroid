package com.coredroid.core;

/**
 * Throw this in AppLauncher.init() if you want a more readable error message
 */
public class InitializationException extends RuntimeException {

	public InitializationException(String message) {
		super(message);
	}
}
