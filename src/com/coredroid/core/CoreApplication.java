package com.coredroid.core;

import android.app.Application;

import com.coredroid.data.PreferencesDataStore;
import com.coredroid.util.LogIt;

public class CoreApplication extends Application {

	private static AppState state;
	
	{
		LogIt.d(CoreApplication.class, "Creating CoreApplication");
	}
	
	@Override
	public void onCreate() {
		LogIt.d(this, "Creating application state");
		state = new AppState(new PreferencesDataStore(this));
	}
	
	public static AppState getState() {
		return state;
	}
}
