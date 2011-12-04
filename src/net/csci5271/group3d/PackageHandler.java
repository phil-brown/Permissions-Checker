package net.csci5271.group3d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is called when a new application is downloaded to the Android
 * device. Next, it passes off the intent it received to {@link H#handleEverything(Intent)},
 * which does everything else.
 * @author Phil Brown
 */
public class PackageHandler extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		H.handleEverything(intent);
	}//onReceive
}//PermissionChecker