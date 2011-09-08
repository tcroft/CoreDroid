package com.coredroid.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIUtil {

	private static Map<String, Typeface> typefaceMap = new HashMap<String, Typeface>();
	
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
}
