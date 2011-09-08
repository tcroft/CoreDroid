package com.coredroid.bo;

import com.coredroid.core.DirtyableCoreObject;

/**
 * Simple business object to encapsulate a user
 */
public class CoreUser extends DirtyableCoreObject {

	private String name;
	private String password;

	/**
	 * This object should not be flushed with the volatile application state.  This allows the user's credentials
	 * to be reused on startup
	 */
	@Override
	public boolean isPersistent() {
		return true;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		dirty();
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		dirty();
		this.password = password;
	}
	
	
	
}
