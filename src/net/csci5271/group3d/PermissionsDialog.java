package net.csci5271.group3d;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** Handles the string intent received by the HTTP GET request
 * @author Phil Brown*/
public class PermissionsDialog extends ListActivity {

	/** The response string retrieved from the server*/
	protected String response;
	/** The package name of the app*/
	protected String app_name;
	/** The category the app belongs to*/
	protected String app_category;
	/** The permissions that the app is requesting*/
	protected String[] permissions;
	/** The danger level that has been calculated for this app*/
	protected float danger_level;
	/** A description about what the danger level means.*/
	protected String danger_about;
	/** Layout reference IDs*/
	protected int[] layouts = new int[]{R.layout.permissions_layout, 
			                            R.layout.perms2,
			                            R.layout.perms3};
	/** These are used to log the ID. Each integer is associtaed with the index
	 * of the layout in {@link layouts}.*/
	protected int[] layout_ids = new int[]{0, 1, 2};
	/** The selected layout id. Used for logging. 
	 * @see #logSelection(boolean) */
	protected int final_layout_id;
	/** Used to notify this activity when it is being opened after the app has
	 * has been uninstalled. */
	public static final int REQUEST_CODE_DELETE = 0;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == REQUEST_CODE_DELETE) {
			finish();
		}
	}//onActivityResult
	
	@Override
	public void onNewIntent(Intent intent) {
		if (intent.getAction().equals(H.SHOW_DIALOG)) {
			int n_id = intent.getIntExtra(H.NOTIFICATION_ID, -1);
			//if the notification id is valid, remove it from the saved ids list.
			if (n_id != -1) {
				H.notificationIDs.remove(n_id);
				H.nManager.cancel(n_id);
			}
		}
	}//onNewIntent
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.handleIntent(this.getIntent());
		int layout_id = H.prefs.getInt("layout_id", -1);
		if (layout_id == -1) {
			final_layout_id = (int) (Math.random() * layouts.length);
			setContentView(layouts[final_layout_id]);
		}
		else {
			final_layout_id = layout_id;
			setContentView(layouts[layout_id]);
		}
		TextView tv = (TextView) findViewById(R.id.message);
		String body = "The application ";
		if (this.response != null) {
			String[] lines = response.split("\\s+");
			for (String line: lines) {
				Log.i("PERMISSION", line);
				String[] s = line.split("=");
				String key = s[0];
				String value = null;
				if (s.length == 2) {
					value = s[1];
				}
				if (key.equals("name")) {
					this.app_name = value;
				}
				else if (key.equals("category")) {
					this.app_category = value;
				}
				else if (key.equals("perms")) {
					this.permissions = value.split(",");
				}
				else if (key.equals("danger_level")) {
					this.danger_level = Float.parseFloat(value);
				}
				else if (key.equals("danger_about")) {
					this.danger_about = value;
				}
			}
			body += this.app_name + " has requested dangerous " +
					"permissions. Permissions Checker suggests a danger rating of " + 
					this.danger_level + ". Details below.";
		}
		else {
			body = "Permissions Checker was unable to verify the package. " +
					"you have downloaded. It may be dangerous. Be sure to read" +
					"over all of its permissions before use.";
		}
		tv.setText(body);
		setListAdapter(new PermissionsAdapter(this, permissions));
		TextView dRating = (TextView) findViewById(R.id.danger_rating);
		dRating.setText("Danger Level: " + this.danger_level);
		Button soWhat = (Button) findViewById(R.id.i_dont_care);
		soWhat.setText("Keep " + this.app_name);
		soWhat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logSelection(false);
				finish();
			}
		});
		Button uninstall = (Button) findViewById(R.id.remove_app);
		uninstall.setText("Uninstall " + this.app_name);
		uninstall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logSelection(true);
				Uri packageURI = Uri.parse("package:" + app_name);
				Intent rm_pkg = new Intent(Intent.ACTION_DELETE, packageURI);
				startActivityForResult(rm_pkg, REQUEST_CODE_DELETE);
			}
		});
	}//onCreate
	
	public void handleIntent(Intent intent) {
		if (intent == null) {
			return;
		}
		this.response = intent.getStringExtra(H.RESPONSE);
	}//handleIntent
	
	/** Registers the user's selction if he or she simply exits the app without
	 * selecting a button.*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.logSelection(false);
		}
		return super.onKeyDown(keyCode, event);
	}//onKeyDown
	
	/** Post the user's decision (keep or uninstall) to the server<br>
	 * POST arguments:  interface_id (int), package (name), installed (yes/no)*/
	public void logSelection(boolean heededWarning) {
		String data = H.BASE_URL + "?interface_id=" + final_layout_id + 
		                           "&package=" + app_name + 
		                           "&installed=" + (heededWarning? 1 : 0);
		new Request(this, Request.POST, data, null, new Request.Callback() {
			@Override
			public void invoke(String s) {
				return;
			}
		}).execute();
	}//logSelection
	
}//PermissionsDialog
