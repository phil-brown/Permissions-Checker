package net.csci5271.group3d;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	public static TextView tv;
	public static Context context;
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		context = this;
		tv = new TextView(this);
		tv.setText("Welcome to PermissionsChecker. PermissionsChecker is an app " +
				"that simply notifies you, the user, of strange or dangerous permissions requests " +
				"that you may have overlooked during download. This feature is meant to provide " +
				"you with a better knowledge of what permissions to be wary of, as well as a better " +
				"awareness of what you are downloading. ");
		tv.setSingleLine(false);
		setContentView(tv);
	}
	
	public static void setText(String text) {
		tv.setText(text);
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
}
