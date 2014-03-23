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

/**
 *	Infomenu for dynamic drone info
 */
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

	/**
	 * Set target drone for info menu
	 * @param drone - Drone object
	 * @param context
	 */
	public InfoMenu(Drone drone, Context context) {
		this.drone = drone;
	}
	
	
	/**
	 * Set menus depending on connection status
	 * @param menu
	 * @param menuInflater
	 */
	public void inflateMenu(Menu menu, MenuInflater menuInflater) {
		if (drone.MavClient.isConnected()) {
			menuInflater.inflate(R.menu.menu_newui_connected, menu);
			findViews(menu);
		} else {
			menuInflater.inflate(R.menu.menu_newui_disconnected, menu);
		}
	}

	
	/**
	 * Find views for the menu
	 * @param menu
	 */
	private void findViews(Menu menu) {
		
	}

	
	/**
	 * Force views to update
	 */
	public void forceViewsUpdate() {
		onDroneEvent(DroneEventsType.BATTERY, drone);
		onDroneEvent(DroneEventsType.GPS_FIX, drone);
		onDroneEvent(DroneEventsType.RADIO, drone);
		onDroneEvent(DroneEventsType.STATE, drone);
		onDroneEvent(DroneEventsType.HOME, drone);
		onDroneEvent(DroneEventsType.MODE, drone);
	}

	/* (non-Javadoc)
	 * @see org.chaseme.drone.DroneInterfaces.OnDroneListener#onDroneEvent(org.chaseme.drone.DroneInterfaces.DroneEventsType, org.chaseme.drone.Drone)
	 */
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

	/**
	 * Update battery info
	 * @param drone
	 */
	private void updateBatteryInfo(Drone drone) {
	}

	/**
	 * Update GPS info
	 * @param drone
	 */
	private void updateGpsInfo(Drone drone) {
	}

	/**
	 * Update radio info
	 * @param drone
	 */
	private void updateRadioInfo(Drone drone) {
	}

	/**
	 * Update home info
	 * @param drone
	 */
	public void updateHomeInfo(Drone drone) {
	}

	/**
	 * Set menu item to false if it's already selected
	 * @return false
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
        return false;
	}

	/**
	 * Setup mode spinner
	 * @param context
	 */
	public void setupModeSpinner(Context context) {
		if (mode != null) {
			mode.buildSpinner(context, drone);
		}
	}
}
