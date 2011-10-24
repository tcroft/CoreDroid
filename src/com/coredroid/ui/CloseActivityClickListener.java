package com.coredroid.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Simple click listener that calls finish()
 */
public class CloseActivityClickListener implements OnClickListener {

	private Activity activity;
	
	public CloseActivityClickListener(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void onClick(View v) {
		activity.finish();
	}

}
