package net.csci5271.group3d;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

/**
 * User Settings
 * @author Phil Brown
 */
public class Settings extends PreferenceActivity {
	
	/** The list preference used for the minimum danger level setting.*/
	private ListPreference mdl;
	/** The list preference used for the layout ID to use. This is best kept 
	 * for debugging (for now).*/
	private ListPreference layoutID;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        mdl = (ListPreference) this.findPreference("minimum_danger_level");
        layoutID = (ListPreference) this.findPreference("layout_id");
    }//onCreate
	
	@Override
	public void onStop() {
		super.onStop();
		H.preferences_editor.putFloat("minimum_danger_level", Float.parseFloat(mdl.getValue().toString()));
		H.preferences_editor.putInt("layout_id", Integer.parseInt(layoutID.getValue().toString()));
		H.preferences_editor.commit();
	}//onStop
}//Settings
