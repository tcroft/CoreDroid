package com.coredroid.util;

import android.os.Handler;

public abstract class BackgroundTask {

	protected Handler handler;

	private Throwable error;
	private String errorMessage;
	
	public BackgroundTask() {
		handler = new Handler();
		
		new BackgroundThread().start();
	}

	public abstract void work();
	public abstract void done();

	/**
	 * Whether there was an error while working, typically checked in done()
	 */
	protected boolean failed() {
		return error != null || errorMessage != null;
	}

	protected void fail(Throwable t) {
		fail(t, null);
	}
	
	protected void fail(Throwable t, String message) {
		error = t;
		errorMessage = message;
		LogIt.e(this, t, message);
	}
	
	protected Throwable getException() {
		return error;
	}
	
	protected String getExceptionMessage() {
		return errorMessage;
	}
	
	private class BackgroundThread extends Thread {
		
		public void run() {
			
			try {
				work();
			} catch (Throwable t) {
				fail(t, null);
			}
			
			handler.post(new Runnable() {
				
				public void run() {
					try {
						done();
					} catch (Throwable t) {
						LogIt.e(this, t);
					}
				}
			});
		}
	}
}
