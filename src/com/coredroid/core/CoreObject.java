package com.coredroid.core;

/**
 * Common base class for model objects.
 * By default this object will live as long as the application doesn't do a full restart.
 * If full persistence is desired the subclass needs to override isPersistent() and return true
 */
public abstract class CoreObject {

	/** 
	 * A "persistent" object (class really) is one that is not deleted on a clear() call
	 */
	public boolean isPersistent() {
		return false;
	}
	
}
