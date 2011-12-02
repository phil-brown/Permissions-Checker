package net.csci5271.group3d;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * List Item Adapter
 * @author Phil Brown
 *
 */
public class PermissionsAdapter extends ArrayAdapter<String>{

	private String[] perms;
	
	private Context c;
	
	public PermissionsAdapter(Context context, String[] permissions) {
		super(context, 0, permissions);
		this.perms = permissions;
		this.c = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = 
				(LayoutInflater)c.getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item, null);
		}
		
		PackageManager pm = c.getPackageManager();
		String description = null;
		try {
			PermissionInfo p = pm.getPermissionInfo(perms[position], 0);
			description = p.loadDescription(pm).toString();
		} catch (NameNotFoundException e) {
			Log.e("PERMISSION", "Permission not found");
		}
		TextView name = (TextView) v.findViewById(R.id.permission_name);
		name.setText("¥ " + perms[position].replace("android.permission.", ""));
		TextView descr = (TextView) v.findViewById(R.id.permission_descr);
		descr.setText(description);
		return v;
	}
}
