package net.csci5271.group3d;

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
		//H.nManager.cancelAll();
		H.nextNotificationId = 0;
		
		H.prefs = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		H.preferences_editor = H.prefs.edit();
	}//onCreate
}//Init
