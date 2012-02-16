package com.coredroid.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Misc functions to help interact with the device
 */
public class DeviceUtil {

	private static final String EMULATOR_UDID = "000000000000000";
	private static String udid;
	
	/** 
	 * This implementation uses the telephonymanager and thus requires permission READ_PHONE_STATE
	 */
	public synchronized static String getUDID(Context context) {
		if (udid != null) {
			return udid;
		}
		
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		udid = tManager.getDeviceId();		
		if (StringUtil.isEmpty(udid) || EMULATOR_UDID.equals(udid)) {
			udid = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		}
        LogIt.d(DeviceUtil.class, "UDID: " + udid);
		return udid;
	}

	public static String getAppVersion(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
