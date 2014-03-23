package org.chaseme.activities.helpers;

import org.chaseme.R;
import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.drone.DroneInterfaces.OnDroneListener;
import org.chaseme.gcs.GCSHeartbeat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public abstract class SuperUI extends SuperActivity implements OnDroneListener {
	private ScreenOrientation screenOrientation = new ScreenOrientation(this);
	private InfoMenu infoMenu;
	private GCSHeartbeat gcsHeartbeat;

	/**
	 * Call constructor for SuperActivity 
	 */
	public SuperUI() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screenOrientation.unlock();
		gcsHeartbeat = new GCSHeartbeat(drone,1);
	}

	@Override
	protected void onStart() {
		super.onStart();
		drone.events.addDroneListener(this);
		drone.MavClient.queryConnectionState();
		drone.events.notifyDroneEvent(DroneEventsType.MISSION_UPDATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		drone.events.removeDroneListener(this);
		infoMenu = null;
	}

	@Override
	public void onDroneEvent(DroneEventsType event, Drone drone) {
		if (infoMenu != null) {
			infoMenu.onDroneEvent(event, drone);
		}
		switch (event) {
		case CONNECTED:
			gcsHeartbeat.setActive(true);
			invalidateOptionsMenu();
			screenOrientation.requestLock();
			break;
		case DISCONNECTED:
			gcsHeartbeat.setActive(false);
			invalidateOptionsMenu();
			screenOrientation.unlock();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		infoMenu = new InfoMenu(drone,this);
		infoMenu.inflateMenu(menu, getMenuInflater());
		infoMenu.setupModeSpinner(this);
		getMenuInflater().inflate(R.menu.menu_super_activiy, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		infoMenu.forceViewsUpdate();
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(infoMenu.onOptionsItemSelected(item))
            return true;
		return super.onOptionsItemSelected(item);
	}
}
