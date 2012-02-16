package com.coredroid.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

/**
 * Simple click listener that calls finish()
 */
public class CloseActivityClickListener implements View.OnClickListener, DialogInterface.OnClickListener {

	private Activity activity;
	
	public CloseActivityClickListener(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void onClick(View v) {
		activity.finish();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		activity.finish();
	}
	
}
