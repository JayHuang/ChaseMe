package org.chaseme.drone.variables;

import org.chaseme.MAVLink.MavLinkStreamRates;
import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.drone.DroneInterfaces.OnDroneListener;
import org.chaseme.drone.DroneVariable;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StreamRates extends DroneVariable implements OnDroneListener {

	public StreamRates(Drone myDrone) {
		super(myDrone);
		myDrone.events.addDroneListener(this);
	}

	@Override
	public void onDroneEvent(DroneEventsType event, Drone drone) {
		switch (event) {
		case HEARTBEAT_FIRST:
		case HEARTBEAT_RESTORED:
			setupStreamRatesFromPref();
			break;
		default:
			break;
		}
	}

	public void setupStreamRatesFromPref() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(myDrone.context);

		int extendedStatus = Integer.parseInt(prefs.getString(
				"pref_mavlink_stream_rate_ext_stat", "0"));
		int extra1 = Integer.parseInt(prefs.getString(
				"pref_mavlink_stream_rate_extra1", "0"));
		int extra2 = Integer.parseInt(prefs.getString(
				"pref_mavlink_stream_rate_extra2", "0"));
		int extra3 = Integer.parseInt(prefs.getString(
				"pref_mavlink_stream_rate_extra3", "0"));
		int position = Integer.parseInt(prefs.getString(
				"pref_mavlink_stream_rate_position", "0"));
		int rcChannels = Integer.parseInt(prefs.getString(
				"pref_mavlink_stream_rate_rc_channels", "0"));
		int rawSensors = Integer.parseInt(prefs.getString(
				"pref_mavlink_stream_rate_raw_sensors", "0"));
		int rawController = Integer.parseInt(prefs.getString(
				"pref_mavlink_stream_rate_raw_controller", "0"));

		MavLinkStreamRates.setupStreamRates(myDrone.MavClient, extendedStatus,
				extra1, extra2, extra3, position, rcChannels, rawSensors,
				rawController);
	}

}
