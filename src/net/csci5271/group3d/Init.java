package net.csci5271.group3d;

import android.app.Application;

public class Init extends Application {
	
	@Override
	public void onCreate() {
		H.context = getApplicationContext();
	}

}
