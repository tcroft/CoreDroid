package com.coredroid.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coredroid.R;

/**
 * Helper class for managing the actionbar on a page.  Page must include the 'actionbar.xml' layout.
 */
public class ActionBar {
	private Activity activity;
	private TextView titleView;
	private Button leftButton;
	private Button rightButton;
	private LinearLayout leftButtonBar;
	private LinearLayout rightButtonBar;

	public ActionBar(Activity activity) {
		this(activity, null);
	}

	public ActionBar(Activity activity, String title) {
		this.activity = activity;

		titleView = (TextView) activity.findViewById(R.id.pageTitle);
		leftButtonBar = (LinearLayout) activity.findViewById(R.id.leftButtonBar);
		rightButtonBar = (LinearLayout) activity.findViewById(R.id.rightButtonBar);

		if (title != null) {
			setTitle(title);
		}

		Resources resources = activity.getResources();
		int topColor = resources.getColor(R.color.actionbar_topColor);
		int topMidColor = resources.getColor(R.color.actionbar_topMidColor);
		int bottomMidColor = resources.getColor(R.color.actionbar_bottomMidColor);
		int bottomColor = resources.getColor(R.color.actionbar_bottomColor);

		activity.findViewById(R.id.actionBar).setBackgroundDrawable(new DualToneGradientDrawable(topColor, topMidColor, bottomMidColor, bottomColor));
	}

	public void setTitle(String title) {
		if (titleView != null) {
			titleView.setText(title);
		}
	}

	public void setTitle(int resourceTitle) {
		titleView.setText(resourceTitle);
	}

	/**
	 * Special case button that finishes the current activity, button in the left bar
	 */
	public void addDoneAction() {
		addLeftAction("Done", new View.OnClickListener() { // TODO: i18n
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}

	public Button getLeftButton() {
		return leftButton;
	}

	public Button getRightButton() {
		return rightButton;
	}

	public void addLeftAction(String label, View.OnClickListener listener) {
		leftButton = createButton(label, listener);
		leftButtonBar.addView(leftButton, getLayoutParams());
	}

	public void addLeftAction(int stringId, View.OnClickListener listener) {
		addLeftAction(activity.getString(stringId), listener);
	}

	public void addRightAction(String label, View.OnClickListener listener) {
		rightButton = createButton(label, listener);
		rightButtonBar.addView(rightButton, getLayoutParams());
	}

	public void addRightAction(int stringId, View.OnClickListener listener) {
		addRightAction(activity.getString(stringId), listener);
	}

	private LinearLayout.LayoutParams getLayoutParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		return params;
	}

	private Button createButton(String label, View.OnClickListener listener) {
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, activity.getResources().getDisplayMetrics());

		Button button = new Button(activity);
		button.setText(label);
		button.setTextColor(Color.WHITE);
		button.setShadowLayer(1, 0, -1, Color.BLACK);
		button.setOnClickListener(listener);
		button.setBackgroundResource(R.drawable.actionbar_button);
		button.setPadding(padding*2, padding, padding*2, padding);

		return button;
	}
}
