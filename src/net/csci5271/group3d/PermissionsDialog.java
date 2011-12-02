package net.csci5271.group3d;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** Handles the string intent received by the HTTP GET request
 * @author Phil Brown*/
public class PermissionsDialog extends ListActivity {

	protected String response;
	protected String app_name;
	protected String app_category;
	protected String[] permissions;
	protected float danger_level;
	protected String danger_about;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.handleIntent(this.getIntent());
		setContentView(R.layout.permissions_layout);
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
				//TODO Log the click, send to server
				finish();
			}
		});
		Button uninstall = (Button) findViewById(R.id.remove_app);
		uninstall.setText("Uninstall " + this.app_name);
		uninstall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO Log the click, send to server
				Uri packageURI = Uri.parse("package:" + app_name);
				Intent rm_pkg = new Intent(Intent.ACTION_DELETE, packageURI);
				startActivity(rm_pkg);
			}
		});
	}
	
	public void handleIntent(Intent intent) {
		if (intent == null) {
			return;
		}
		this.response = intent.getStringExtra(H.RESPONSE);
	}
}
