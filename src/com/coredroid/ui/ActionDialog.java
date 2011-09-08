package com.coredroid.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A dialog that has a vertical list of buttons
 */
public class ActionDialog extends Dialog {

	private LinearLayout panel;
	
	public ActionDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		panel = new LinearLayout(context);
		panel.setOrientation(LinearLayout.VERTICAL);
		setContentView(panel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
	}

	public void addAction(String label, View.OnClickListener listener) {
		
		Button button = new Button(getContext());
		button.setText(label);
		button.setOnClickListener(listener);
		
		panel.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
	}
}
