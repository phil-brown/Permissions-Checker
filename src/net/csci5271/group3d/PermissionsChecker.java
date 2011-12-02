package net.csci5271.group3d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class does several things<br>
 * 1. It opens as soon as the user downloads a new app.<br>
 * 2. It starts a non-UI blocking HTTP POST & GET to get info about the permissions used<br>
 * 3. It launches an intent call to the PermissionsDialog class to show our message to the user
 * @author Phil Brown
 */
public class PermissionsChecker extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		H.handleEverything(intent);
	}//onReceive
}