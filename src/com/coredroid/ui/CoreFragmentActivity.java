package com.coredroid.ui;

import com.coredroid.core.CoreApplication;
import com.coredroid.util.UIUtil;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class CoreFragmentActivity extends FragmentActivity {

	private boolean initFinished;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (hideTitlebar()) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	}
	
	/**
	 * Allow sub classes to determine if the titlebar (or actionbar in honeycomb+) should show.  The default is true
	 * @return
	 */
	protected boolean hideTitlebar() {
		return true;
	}
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		if (!initFinished) {
			setFont(getFont());
			
			initFinished = true;
		}
	}

	/**
	 * Sets the font on all text views for the current activity
	 */
	protected void setFont(Typeface font) {
		if (font == null) {
			return;
		}
		
		UIUtil.setFont(getContentPanel(), font);
	}

	/**
	 * Gets the top level panel container for this activity
	 */
	protected View getContentPanel() {

		return UIUtil.getContentPanel(this);
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
		
		CoreApplication.getState().sync();
	}
	
	protected String getViewText(int label) {
		TextView view = ((TextView)findViewById(label));
		return view != null ? view.getText().toString() : "";
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (handleBackButton()) {
	    		return true;
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}	

	/**
	 * Define what to do when the back button is pushed.  Convenience method to override, default behavior is no-op
	 */
	protected boolean handleBackButton() {
		return true;
	}	
}
