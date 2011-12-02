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
	private ListPreference list;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        list = (ListPreference) this.findPreference("minimum_danger_level");
    }//onCreate
	
	@Override
	public void onStop() {
		super.onStop();
		H.preferences_editor.putFloat("minimum_danger_level", Float.parseFloat(list.getValue().toString()));
		H.preferences_editor.commit();
	}//onStop
}//Settings
