package com.coredroid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class StartActivityClickListener implements OnClickListener {

	protected Intent intent;
	protected Context context;
	
	public StartActivityClickListener(Context context, Intent intent) {
		this.context = context;
		this.intent = intent;
	}
	
	public StartActivityClickListener(Context context, Class<? extends Activity> activityClass) {
		this.context = context;
		intent = new Intent(context, activityClass);
	}
	
	@Override
	public void onClick(View v) {
		startActivity();
	}

	protected void startActivity() {
		context.startActivity(intent);
	}
}
