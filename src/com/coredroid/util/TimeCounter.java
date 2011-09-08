package com.coredroid.util;

public class TimeCounter {

    private long ellapsed;
    private long start = -1;
    
    public void start() {
    	LogIt.d(this, "START");
        start = System.currentTimeMillis();
    }
    
    public void stop() {
    	if (start < 0) {
    		return;
    	}
    	LogIt.d(this, "STOP");
        ellapsed = System.currentTimeMillis() - start;
        start = -1;
    }
    
    public long getEllapsed() {
        return ellapsed + (start > 0 ? System.currentTimeMillis() - start : 0);
    }
}
