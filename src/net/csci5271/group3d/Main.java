package net.csci5271.group3d;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This class contains the welcome message. It may become something more later.
 * @author Phil Brown
 */
public class Main extends Activity {

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.main);
		TextView descr = (TextView) findViewById(R.id.main_text);
		descr.setText("Welcome to Permissions Checker. Permissions Checker is an app " +
				"that simply notifies you, the user, of strange or dangerous permissions requests " +
				"that you may have overlooked during download. This feature is meant to provide " +
				"you with a better knowledge of what permissions to be wary of, as well as a better " +
				"awareness of what you are downloading. ");
		Button settings = (Button) findViewById(R.id.settings_btn);
		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, Settings.class);
				startActivity(intent);
			}
		});
		Button about = (Button) findViewById(R.id.about_btn);
		about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
				builder.setTitle("Permissions Checker")
				       .setMessage("Android: Written by Phil Brown. Source is available at " +
				       		"https://github.com/phil-brown.\n\n" +
				       		"PHP Server: Written  by Jason Zerbe. Code is available at " +
				       		"https://github.com/jzerbe/droidperm_web.\n\n" +
				       		"Permissions Checker is part of a group project at the University of Minnesota." +
				       		" Project collaborators include Edward Stanley, Nathan Hammernik, David Salm, " +
				       		"& Michael O'keefe.")
				        .setNeutralButton("Cool!", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();
			}
		});
	}//onCreate
}//Main
