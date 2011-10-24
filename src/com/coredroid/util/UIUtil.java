package com.coredroid.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class UIUtil {

	private static Map<String, Typeface> typefaceMap = new HashMap<String, Typeface>();

	/**
	 * Get the rough physical diagonal size of the device
	 */
	public static float getScreenInches(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        double dimX = metrics.widthPixels / metrics.xdpi;
        double dimY = metrics.heightPixels / metrics.ydpi;
        return (float)Math.sqrt(dimX*dimX + dimY*dimY);
	}
	
	public static void showKeyboard(View view) {
		InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
	
	/**
	 * Close the soft keyboard associated with this view
	 */
	public static void hideKeyboard(View view) {
		InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);		
	}

	public static Typeface getTypeface(Context context, String typeface) {
		Typeface font = typefaceMap.get(typeface);
		if (font == null) {
			font = Typeface.createFromAsset(context.getAssets(), typeface);
			typefaceMap.put(typeface, font);
		}
        return font; 
	}
	
	/**
	 * Changes the size dimension to fit within the width/height ratio
	 */
	public static void contain(Dimension size, int width, int height) {
		
		if (width == 0 || height == 0) {
			size.setWidth(0);
			size.setHeight(0);
			return;
		}

		int dim = Math.min(width, height);
		
		if (size.getWidth() < size.getHeight()) {
			double ratio = size.getWidth()/(double)size.getHeight(); 
			size.setHeight(dim);
			size.setWidth((int)(dim * ratio));
		} else {
			double ratio = size.getHeight()/(double)size.getWidth();
			size.setWidth(dim);
			size.setHeight((int)(dim * ratio));
		}
	}

	/**
	 * Recursively set the font on all text views on this root
	 */
	public static void setFont(View view, Typeface font) {
		
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

	/**
	 * Convenience method to show a loading dialog with message
	 */
	public static ProgressDialog showProgressDialog(Context context, String message) {
		return ProgressDialog.show(context, null, message, true);
	}

	/**
	 * Set the font on this specific view
	 */
	public void setFont(Context context, TextView view, String font) {
		Typeface typeface = UIUtil.getTypeface(context, font);
		view.setTypeface(typeface);
	}

	/**
	 * Set the font on the specific textview with the given ID
	 */
	public void setFont(Activity activity, int viewId, String font) {
		TextView view = (TextView)activity.findViewById(viewId);
		setFont(activity, view, font);
	}
	
	/**
	 * Get the top level content panel for the activity
	 */
	public static View getContentPanel(Activity activity) {
		
		Window window = activity.getWindow();
		if (window == null) {
			return null;
		}
		
		View view = window.getDecorView(); 
		return view instanceof ViewGroup ? ((ViewGroup)view).getChildAt(0) : view;
	}

	public static void confirm(Context context, String title, String message, DialogInterface.OnClickListener onOK) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}
		if (!TextUtils.isEmpty(message)) {
			builder.setMessage(message);
		}
		builder.setPositiveButton("OK", onOK); // TODO: make 'OK' configurable
		builder.setNegativeButton("Cancel", null);
		builder.create().show();
	}
	
	public static void alert(Context context, String title, String message) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create().show();
	}
	
	/**
	 * Show a message dialog, clicking OK executes the supplied handler
	 */
	public static void alert(Context context, String message, DialogInterface.OnClickListener onDismiss) {
		// TODO: i18n 
		new AlertDialog.Builder(context).setMessage(message).setPositiveButton("OK", onDismiss).create().show();
	}

	/**
	 * Show a message dialog with an OK button, clicking OK kills the application
	 */
	public static void fail(Context context, String message) {
		new AlertDialog.Builder(context).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		}).create().show();
	}

	/**
	 * Prevent keyboard from popping up when field gets focus
	 */
	public static void suppressKeyboard(EditText text) {
		text.setInputType(InputType.TYPE_NULL);
	}	
}
