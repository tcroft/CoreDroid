package com.coredroid.core;

/**
 * Extension of CoreObject that is smarter about serializing state.
 * Note that it's not presently smart enough to handle dirty bits from
 * non primitive properties.
 */
public abstract class DirtyableCoreObject extends CoreObject {

	private boolean isDirty;
	
	public boolean isDirty() {
		return isDirty;
	}
	
	public void dirty() {
		isDirty = true;
	}
	
	public void clean() {
		isDirty = false;
	}
}
