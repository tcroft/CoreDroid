package com.coredroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.coredroid.core.CoreApplication;
import com.coredroid.util.UIUtil;

public abstract class CoreActivity extends Activity {

	private boolean initFinished;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		if (!initFinished) {
			setFont(getFont());
			
			initFinished = true;
		}
	}
	
	protected void setFont(Typeface font) {
		if (font == null) {
			return;
		}
		
		setFont(getContentPanel(), font);
		
	}
	
	private void setFont(View view, Typeface font) {
		
		if (view == null) {
			return;
		}
		
		if (view instanceof ViewGroup) {
			for (int i = 0, lim = ((ViewGroup)view).getChildCount(); i < lim; i++) {
				setFont(((ViewGroup)view).getChildAt(i), font);
			}
			return;
		}
		
		if (view instanceof TextView) {
			((TextView)view).setTypeface(font);
		}
	}
	
	protected View getContentPanel() {
		
		Window window = getWindow();
		if (window == null) {
			return null;
		}
		
		View view = window.getDecorView(); 
		return view instanceof ViewGroup ? ((ViewGroup)view).getChildAt(0) : view;
	}
	
	/**
	 * The font to recursively set on all text views on the panel, null will use default font.
	 * This implementation returns null
	 */
	protected Typeface getFont() {
		return null;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		long start = System.currentTimeMillis();
		CoreApplication.getState().sync();
	}
	
	protected String getViewText(int label) {
		TextView view = ((TextView)findViewById(label));
		return view != null ? view.getText().toString() : "";
	}
	
	protected void attach(int view, View.OnClickListener listener) {
		findViewById(view).setOnClickListener(listener);
	}

    protected void setClickListener(int viewId, View.OnClickListener listener) {
        ((View)findViewById(viewId)).setOnClickListener(listener);
    }
    
    protected void setClickListener(View root, int viewId, View.OnClickListener listener) {
        ((View)root.findViewById(viewId)).setOnClickListener(listener);
    }
    
	protected void setViewText(int viewId, String text) {
		((TextView) findViewById(viewId)).setText(text != null ? text : "");
	}
	
	protected void setVisible(int viewId, boolean visible) {
		findViewById(viewId).setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	protected void setHidden(int viewId, boolean visible) {
		findViewById(viewId).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void showAlert(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", new CloseClickListener()).create().show();
	}
	
	public ProgressDialog showLoadingDialog(String message) {
		return ProgressDialog.show(this, null, message, true);
	}

	private static class CloseClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (handleBackButton()) {
	    		return true;
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}	

	protected boolean handleBackButton() {
		return true;
	}
	
	public void sendEmail(String recipient, String subject, String body) {
		sendEmail(recipient, null, subject, body, "Sharing ...");
	}
	public void sendEmail(String recipient, String cc, String subject, String body) {
		sendEmail(recipient, cc, subject, body, "Sharing ...");
	}
	public void sendEmail(String recipient, String cc, String subject, String body, String purpose) {
		
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		emailIntent.setType("plain/text");
		if (recipient != null) {
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{recipient});
		}
		if (cc != null) {
			emailIntent.putExtra(android.content.Intent.EXTRA_CC, new String[]{cc});
		}
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

		startActivity(Intent.createChooser(emailIntent, purpose));
	}

	public void setFont(TextView view, String font) {
		Typeface typeface = UIUtil.getTypeface(this, font);
		view.setTypeface(typeface);
	}
	public void setFont(int viewId, String font) {
		TextView view = (TextView)findViewById(viewId);
		setFont(view, font);
	}
	
	/**
	 * Show a message dialog, clicking OK executes the supplied handler
	 */
	public void alert(String message, DialogInterface.OnClickListener onDismiss) {
		new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", onDismiss).create().show();
	}
	
	/**
	 * Show a message dialog with an OK button, clicking OK kills the application
	 */
	public void fail(String message) {
		new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		}).create().show();
	}
}
