package com.coredroid.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Opens the URL in a browser
 */
public class OpenInBrowserListener implements OnClickListener {

	private String url;
	private Context context;
	
	public OpenInBrowserListener(Context context, String url) {
		this.url = url;
		this.context = context;
	}
	
	@Override
	public void onClick(View v) {
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
		context.startActivity(viewIntent);  		
	}

}
