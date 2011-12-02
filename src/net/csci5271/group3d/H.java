package net.csci5271.group3d;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.util.Log;
import android.widget.Toast;

public class H {

	/** This should be adjustable in user settings.*/
	public static long MINIMUM_DANGER_LEVEL = 1;//TODO what is this based off of and what is the max?
	
	public static final String BASE_URL = "http://www-users.cs.umn.edu/~zerbe/droidperm/";
	//public static final int REQUEST_TIMEOUT = 9000;//60000;
	public static final String RESPONSE = "data";
	public static final String PACKAGE = "package";
	public static Intent info;
	public static Context context;
	
	public static void openDialog() {
		Intent intent = new Intent(context, PermissionsDialog.class);
		context.startActivity(intent);
	}
	
	public static void handleEverything(Intent intent) {
		PackageManager manager = context.getPackageManager();
        try {
        	Log.i("PERMISSION", intent.getDataString());
        	final PackageInfo info = manager.getPackageInfo(intent.getData().getSchemeSpecificPart(), PackageManager.GET_PERMISSIONS);
        	//Log.i("PERMISSION", "FOUND " + info.packageName);
			//String name = info.packageName;
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
			//info.applicationInfo.
			//GET is the only thing needed:
			new Request(context, Request.GET, data, null, new Request.Callback() {
				
				@Override
				public void invoke(String s) {
					String[]lines = s.split("\\s+");
					for (String line: lines) {
						if (line.contains("danger_level")) {
							float danger = Float.parseFloat(line.split("=")[1]);
							//only open dangerous apps (minimum risk set in user settings).
							if (danger < H.MINIMUM_DANGER_LEVEL) {
								//TODO move to notification
								Toast.makeText(context, "Danger Level of " + danger + " detected.", Toast.LENGTH_SHORT).show();
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
	}
}
