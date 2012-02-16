package com.coredroid.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coredroid.R;
import com.coredroid.core.CoreApplication;
import com.coredroid.util.MailUtil;
import com.coredroid.util.UIUtil;

public abstract class CoreActivity extends Activity {

	private boolean initFinished;
	protected Handler handler = new Handler();

	private boolean isActive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (hideTitlebar()) {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        
		int viewId = getContentViewId();
		if (viewId >= 0) {
			setContentView(viewId);
		}
        
	}

	/**
	 * Whether this activity is in the front
	 */
	public boolean isActive() {
		return isActive;
	}
	
	public void setEnabled(int viewId, boolean enabled) {
		findViewById(viewId).setEnabled(enabled);
	}
	
	protected ProgressDialog showProgressDialog(String message) {
		return UIUtil.showProgressDialog(this, message);		
	}

	protected ProgressDialog showProgressDialog(int stringId) {
		return showProgressDialog(getString(stringId));		
	}

	/**
	 * If the subclass would like to use any auto config classes (e.g. actionbar) 
	 * they may override this with the resource ID of the layout to use.  This implementation
	 * returns -1 (no layout).
	 */
	protected int getContentViewId() {
		return -1;
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
	 * Show a short lengthed toast
	 */
	protected void toast(int stringId) {
		toast(getString(stringId), Toast.LENGTH_SHORT);
	}
	
	protected void toast(int stringId, int duration) {
		toast(getString(stringId), duration);
	}

	/**
	 * Show a short lengthed toast
	 */
	protected void toast(String string) {
		toast(string, Toast.LENGTH_SHORT);
	}

	protected void toast(String string, int duration) {
		Toast.makeText(this, string, duration).show();
	}
	
	/**
	 * 
	 * @param activity
	 */
	protected void startActivity(Class<? extends Activity> activity) {
		startActivity(new Intent(this, activity));
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
	protected void onResume() {
		super.onResume();
		isActive = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		CoreApplication.getState().sync();
		isActive = false;
	}
	
	protected String getViewText(int label) {
		TextView view = ((TextView)findViewById(label));
		return view != null ? view.getText().toString() : "";
	}
	
	protected void setOnEnterListener(int viewId, final View.OnKeyListener listener) {
		View view = findViewById(viewId);
        view.setOnKeyListener(new OnKeyListener(){
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction()==KeyEvent.ACTION_DOWN){
					if(keyCode==KeyEvent.KEYCODE_ENTER){
						return listener.onKey(v, keyCode, event);
					}
				}

				return false;
			}
        });
	}
	
    /**
     * Looks for a CheckBox with the given id and sets the checked state
     */
    protected void setChecked(int viewId, boolean checked) {
    	View view = findViewById(viewId);
    	if (view instanceof CheckBox) {
    		((CheckBox)view).setChecked(checked);
    	}
    }
    
    protected void setChecked(View root, int viewId, boolean checked) {
    	View view = root.findViewById(viewId);
    	if (view instanceof CheckBox) {
    		((CheckBox)view).setChecked(checked);
    	}
    }
    
    /**
     * Given an id to a CheckBox will return if the state is checked
     */
    protected boolean isChecked(int viewId) {
    	View view = findViewById(viewId);
    	if (view instanceof CheckBox) {
    		return ((CheckBox)view).isChecked();
    	}
    	return false;
    }
    
    protected boolean isChecked(View root, int viewId) {
    	View view = root.findViewById(viewId);
    	if (view instanceof CompoundButton) {
    		return ((CompoundButton)view).isChecked();
    	}
    	return false;
    }
    
	public void confirm(String title, String message, DialogInterface.OnClickListener onOK) {
		UIUtil.confirm(this, title, message, onOK);
	}
	
	public void confirmYesNo(String title, String message, DialogInterface.OnClickListener onYes, DialogInterface.OnClickListener onNo) {
		UIUtil.confirmYesNo(this, title, message, onYes, onNo);
	}
	
    protected View setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return view;
    }
    
    protected void setClickListener(View root, int viewId, View.OnClickListener listener) {
        ((View)root.findViewById(viewId)).setOnClickListener(listener);
    }
    
	protected void setViewText(int viewId, String text) {
		((TextView) findViewById(viewId)).setText(text != null ? text : "");
	}
	
	protected void setViewText(View root, int viewId, String text) {
		((TextView) root.findViewById(viewId)).setText(text != null ? text : "");
	}
	
	protected void setVisible(int viewId, boolean visible) {
		findViewById(viewId).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}
	
	protected void setHidden(int viewId, boolean visible) {
		findViewById(viewId).setVisibility(visible ? View.GONE : View.VISIBLE);
	}
	
	protected void setHidden(View root, int viewId, boolean visible) {
		root.findViewById(viewId).setVisibility(visible ? View.GONE : View.VISIBLE);
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
	
	public void sendEmail(String recipient, String subject, String body) {
		MailUtil.sendEmail(this, recipient, subject, body);
	}
	public void sendEmail(String recipient, String cc, String subject, String body) {
		MailUtil.sendEmail(this, recipient, cc, subject, body);
	}
	public void sendEmail(String recipient, String cc, String subject, String body, String purpose) {
		MailUtil.sendEmail(this, recipient, cc, subject, body, purpose);
	}

	public void alert(String title, String message) {
		UIUtil.alert(this, title, message);
	}
	
	/**
	 * Show a message dialog, clicking OK executes the supplied handler
	 */
	public void alert(String title, String message, DialogInterface.OnClickListener onDismiss) {
		UIUtil.alert(this, title, message, onDismiss);
	}
	
	/**
	 * Show a message dialog, clicking OK executes the supplied handler
	 */
	public void alert(String message, DialogInterface.OnClickListener onDismiss) {
		UIUtil.alert(this, message, onDismiss);
	}
	
	/**
	 * Show a message dialog with an OK button, clicking OK kills the application
	 */
	public void fail(String message) {
		UIUtil.fail(this, message);
	}
	
	/**
	 * Hide initial page focus by using a hidden text field.
	 * Pages should call this in onCreate, and their pages require a EditText named focusHack
	 */
	protected void hideFocus() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				EditText focusHack = (EditText)findViewById(R.id.focusHack);
				focusHack.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
				focusHack.requestFocus();
				focusHack.setInputType(InputType.TYPE_NULL);
			}
		});
	}
	
}
