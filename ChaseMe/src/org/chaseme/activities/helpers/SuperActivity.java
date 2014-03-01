package org.chaseme.activities.helpers;

import org.chaseme.ChaseMeApp;
import org.chaseme.R;
import org.chaseme.activities.ConfigurationActivity;
import org.chaseme.activities.SettingsActivity;
import org.chaseme.dialogs.AltitudeDialog;
import org.chaseme.dialogs.AltitudeDialog.OnAltitudeChangedListener;
import org.chaseme.drone.Drone;
import org.chaseme.fragments.helpers.BTDeviceListFragment;
import org.chaseme.fragments.helpers.OfflineMapFragment;
import org.chaseme.helpers.units.Altitude;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import org.chaseme.utils.Constants;
import org.chaseme.utils.Utils;

public abstract class SuperActivity extends HelpActivity implements
		OnAltitudeChangedListener {

	public ChaseMeApp app;
	public Drone drone;

	public SuperActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		app = (ChaseMeApp) getApplication();
		this.drone = app.drone;

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.menu_connect:
            toggleDroneConnection();
			return true;
		case R.id.menu_map_type_hybrid:
		case R.id.menu_map_type_normal:
		case R.id.menu_map_type_terrain:
		case R.id.menu_map_type_satellite:
			setMapTypeFromItemId(item.getItemId());
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

    protected void toggleDroneConnection(){
        if (!drone.MavClient.isConnected()) {
            final String connectionType = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext())
                    .getString(Constants.PREF_CONNECTION_TYPE,
                            Constants.DEFAULT_CONNECTION_TYPE);

            if (Utils.ConnectionType.BLUETOOTH.name().equals(connectionType)) {
                //Launch a bluetooth device selection screen for the user
                new BTDeviceListFragment().show(getSupportFragmentManager(), "Device selection dialog");
                return;
            }
        }
        drone.MavClient.toggleConnectionState();
    }

	private void setMapTypeFromItemId(int itemId) {
		final String mapType;
		switch (itemId) {
		case R.id.menu_map_type_hybrid:
			mapType = OfflineMapFragment.MAP_TYPE_HYBRID;
			break;
		case R.id.menu_map_type_normal:
			mapType = OfflineMapFragment.MAP_TYPE_NORMAL;
			break;
		case R.id.menu_map_type_terrain:
			mapType = OfflineMapFragment.MAP_TYPE_TERRAIN;
			break;
		default:
			mapType = OfflineMapFragment.MAP_TYPE_SATELLITE;
			break;
		}

		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putString(OfflineMapFragment.PREF_MAP_TYPE, mapType).commit();

		//drone.notifyMapTypeChanged();
	}

	public void changeDefaultAlt() {
		AltitudeDialog dialog = new AltitudeDialog(this);
		dialog.build(drone.mission.getDefaultAlt(), this);
	}

	@Override
	public void onAltitudeChanged(Altitude newAltitude) {
		drone.mission.setDefaultAlt(newAltitude);
	}
}
