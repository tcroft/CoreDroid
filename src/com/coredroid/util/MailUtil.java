package com.coredroid.util;

import android.app.Activity;
import android.content.Intent;

/**
 * Utility methods to help facilitate mailing
 */
public class MailUtil {

	public static void sendEmail(Activity activity, String recipient, String subject, String body) {
		sendEmail(activity, recipient, null, subject, body, "Sharing ...");
	}

	public static void sendEmail(Activity activity, String recipient, String cc, String subject, String body) {
		sendEmail(activity, recipient, cc, subject, body, "Sharing ...");
	}
	
	public static void sendEmail(Activity activity, String recipient, String cc, String subject, String body, String purpose) {
		
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

		activity.startActivity(Intent.createChooser(emailIntent, purpose));
	}
}
