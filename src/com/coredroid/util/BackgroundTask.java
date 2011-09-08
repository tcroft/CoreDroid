package com.coredroid.util;

import android.os.Handler;

public abstract class BackgroundTask {

	private Handler handler;
	
	public BackgroundTask() {
		handler = new Handler();
		
		new BackgroundThread().start();
	}

	public abstract void work();
	public abstract void done();
	
	private class BackgroundThread extends Thread {
		
		public void run() {
			
			try {
				work();
			} catch (Throwable t) {
				LogIt.e(this, t);
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
