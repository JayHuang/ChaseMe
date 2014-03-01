package org.chaseme.activities;

import org.chaseme.activities.helpers.SuperUI;
import org.chaseme.fragments.SettingsFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import org.chaseme.R;

/**
 * This activity holds the SettingsFragment.
 */
public class SettingsActivity extends SuperUI {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentManager fm = getFragmentManager();
        Fragment settingsFragment = fm.findFragmentById(R.id.fragment_settings_layout);
        if(settingsFragment == null){
            settingsFragment = new SettingsFragment();
            fm.beginTransaction().add(R.id.fragment_settings_layout, settingsFragment).commit();
        }
    }

	@Override
	public CharSequence[][] getHelpItems() {
		return new CharSequence[][] { {}, {} };
	}
}
