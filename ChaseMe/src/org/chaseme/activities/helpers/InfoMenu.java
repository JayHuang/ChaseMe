package org.chaseme.activities.helpers;

import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.drone.DroneInterfaces.OnDroneListener;
import org.chaseme.widgets.TimerView;
import org.chaseme.widgets.spinners.SelectModeSpinner;

import android.content.Context;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.chaseme.R;

public class InfoMenu implements OnDroneListener {
	private Drone drone;
	private MenuItem battery;
	private MenuItem gps;
	private MenuItem propeler;
	private MenuItem home;
	private MenuItem signal;
	private MenuItem signalRSSI;
	private MenuItem signalRemRSSI;
	private MenuItem signalNoise;
	private MenuItem signalRemNoise;
	private MenuItem signalRemFade;
	private MenuItem signalFade;

	public SelectModeSpinner mode;

	private TimerView timer;

	public InfoMenu(Drone drone, Context context) {
		this.drone = drone;
	}

	public void inflateMenu(Menu menu, MenuInflater menuInflater) {
		if (drone.MavClient.isConnected()) {
			menuInflater.inflate(R.menu.menu_newui_connected, menu);
			findViews(menu);
		} else {
			menuInflater.inflate(R.menu.menu_newui_disconnected, menu);
		}
	}

	private void findViews(Menu menu) {
	}

	public void forceViewsUpdate() {
		onDroneEvent(DroneEventsType.BATTERY, drone);
		onDroneEvent(DroneEventsType.GPS_FIX, drone);
		onDroneEvent(DroneEventsType.RADIO, drone);
		onDroneEvent(DroneEventsType.STATE, drone);
		onDroneEvent(DroneEventsType.HOME, drone);
		onDroneEvent(DroneEventsType.MODE, drone);
	}

	@Override
	public void onDroneEvent(DroneEventsType event, Drone drone) {
		try {
			switch (event) {
			case BATTERY:
				updateBatteryInfo(drone);
				break;
			case GPS_FIX:
			case GPS_COUNT:
				updateGpsInfo(drone);
				break;
			case RADIO:
				updateRadioInfo(drone);
				break;
			case HOME:
				updateHomeInfo(drone);
				break;
			default:
				break;
			}
			mode.onDroneEvent(event, drone);
		} catch (NullPointerException e) {
			// Can fail saftly with null pointer if the layout's have not been
			// inflated yet
		}
	}

	private void updateBatteryInfo(Drone drone) {
	}

	private void updateGpsInfo(Drone drone) {
	}

	private void updateRadioInfo(Drone drone) {
	}

	public void updateHomeInfo(Drone drone) {
	}

	public boolean onOptionsItemSelected(MenuItem item) {
        return false;
	}

	public void setupModeSpinner(Context context) {
		if (mode != null) {
			mode.buildSpinner(context, drone);
		}
	}
}
