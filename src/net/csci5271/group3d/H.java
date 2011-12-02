package net.csci5271.group3d;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.util.Log;

/**
 * Contains static variables and methods used commonly throughout the application.
 * @author Phil Brown
 */
public class H {

	/** This should be adjustable in user settings.
	 * TODO what is this based off of and what is the max?*/
	public static float MINIMUM_DANGER_LEVEL = 1;
	/** The base URL of where the server is hosted. */
	public static final String BASE_URL = "http://www-users.cs.umn.edu/~zerbe/droidperm/";
	/** Used for adding extras to {@link Intent}. */
	public static final String RESPONSE = "data", PACKAGE = "package";
	/** The context used for reading package information and launching {@link PermissionsDialog}*/
	public static Context context;
	/** Used for showing notifications in Android */
	public static NotificationManager nManager;
	/** This is used so that no two {@link Notification}s have the same ID (thus they 
	 * won't all cancel when one is clicked). */
	public static int nextNotificationId;
	/** Saves the user preferences across sessions*/
	public static SharedPreferences prefs;
	/** Provides edit access the {@link #prefs}.*/
	public static SharedPreferences.Editor preferences_editor;

	/** This method handles the majority of the work. */
	public static void handleEverything(Intent intent) {
		PackageManager manager = context.getPackageManager();
		H.MINIMUM_DANGER_LEVEL = H.prefs.getFloat("minimum_danger_level", 1);
		nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
        	Log.i("PERMISSION", intent.getDataString());
        	final PackageInfo info = manager.getPackageInfo(intent.getData().getSchemeSpecificPart(), 
        			                                        PackageManager.GET_PERMISSIONS);
			String pkg = info.applicationInfo.packageName;
			PermissionInfo[] permissions = info.permissions;
			String data = H.BASE_URL + "?package=" + pkg;
			if (permissions != null) {
				data += "&perms=";
				for (int i = 0; i < permissions.length; i++) {
					Log.i("PERMISSION " + i, permissions[i].packageName);
					data += permissions[i].packageName + ",";
				}
			}
			new Request(context, Request.GET, data, null, new Request.Callback() {
				
				@Override
				public void invoke(String s) {
					String[]lines = s.split("\\s+");
					for (String line: lines) {
						if (line.contains("danger_level")) {
							float danger = Float.parseFloat(line.split("=")[1]);
							//only open dangerous apps (minimum risk set in user settings).
							if (danger < H.MINIMUM_DANGER_LEVEL) {
								String message = "Danger Level of " + danger + " detected.";
								Notification notification;
								notification = new Notification(R.drawable.warning,
										                        message, 
										                        System.currentTimeMillis());
								notification.flags = Notification.FLAG_AUTO_CANCEL;
								String contentTitle = context.getString(R.string.app_name);
								Intent intent = new Intent(context, PermissionsDialog.class);
								intent.putExtra(H.RESPONSE, s);
								intent.putExtra(H.PACKAGE, info);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								//this fixes strange behavior that occurs when there is no action set.
								intent.setAction("action");
								PendingIntent pIntent;
								pIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
								notification.setLatestEventInfo(context, contentTitle, message, pIntent);
								nManager.notify(nextNotificationId++, notification);
								return;
							}
						}
					}
					Intent intent = new Intent(context, PermissionsDialog.class);
					intent.putExtra(H.RESPONSE, s);
					intent.putExtra(H.PACKAGE, info);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			}).execute();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}//handleEverything
}//H
