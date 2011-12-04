package net.csci5271.group3d;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;

/**
 * This class is run as soon as the app is launched, and initializes some static
 * variables
 * @author Phil Brown
 */
public class Init extends Application {
	
	@Override
	public void onCreate() {
		H.context = getApplicationContext();
		
		H.prefs = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		H.preferences_editor = H.prefs.edit();
		String notificationIDs = H.prefs.getString("notificationIDs", null);
		H.notificationIDs = new ArrayList<Integer>();
		if (notificationIDs != null) {
			String[] ids = notificationIDs.split(",");
			//ids in format 1,2,3,4,5,
			for (int i = 0; i < ids.length - 1; i++) {
				H.notificationIDs.add(Integer.parseInt(ids[i]));
			}
		}
		
	}//onCreate
}//Init
