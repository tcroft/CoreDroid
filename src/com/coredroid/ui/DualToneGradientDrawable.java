package com.coredroid.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

/**
 * Draws a two tone gradient from top to bottom, split in the middle.
 */
public class DualToneGradientDrawable extends Drawable {

	
	private GradientDrawable topDrawable;
	private GradientDrawable bottomDrawable;
	
	public DualToneGradientDrawable(int topColor, int topMidColor, int bottomMidColor, int bottomColor) {
		
		topDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{topColor, topMidColor});
		bottomDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{bottomMidColor, bottomColor});
	}
	
	@Override
	public void draw(Canvas canvas) {
		Rect bounds = copyBounds();
		
		int height = bounds.bottom;
		int halfHeight = bounds.bottom/2;

		bounds.bottom = halfHeight;
		topDrawable.setBounds(bounds);
		topDrawable.draw(canvas);

		bounds.top = halfHeight;
		bounds.bottom = height;
		bottomDrawable.setBounds(bounds);
		bottomDrawable.draw(canvas);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter filter) {
	}

}
