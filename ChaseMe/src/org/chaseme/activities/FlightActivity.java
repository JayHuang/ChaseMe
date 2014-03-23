package org.chaseme.activities;

import org.chaseme.activities.helpers.SuperUI;
import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.drone.DroneInterfaces.OnDroneListener;
import org.chaseme.drone.variables.mission.MissionItem;
import org.chaseme.drone.variables.mission.waypoints.SpatialCoordItem;
import org.chaseme.fragments.FlightActionsFragment;
import org.chaseme.fragments.FlightMapFragment;
import org.chaseme.fragments.RCFragment;
import org.chaseme.fragments.TelemetryFragment;
import org.chaseme.fragments.FlightActionsFragment.OnMissionControlInteraction;
import org.chaseme.fragments.helpers.DroneMap;
import org.chaseme.fragments.helpers.OnMapInteractionListener;
import org.chaseme.fragments.mode.ModeAcroFragment;
import org.chaseme.fragments.mode.ModeAltholdFragment;
import org.chaseme.fragments.mode.ModeAutoFragment;
import org.chaseme.fragments.mode.ModeCircleFragment;
import org.chaseme.fragments.mode.ModeDisconnectedFragment;
import org.chaseme.fragments.mode.ModeDriftFragment;
import org.chaseme.fragments.mode.ModeGuidedFragment;
import org.chaseme.fragments.mode.ModeLandFragment;
import org.chaseme.fragments.mode.ModeLoiterFragment;
import org.chaseme.fragments.mode.ModePositionFragment;
import org.chaseme.fragments.mode.ModeRTLFragment;
import org.chaseme.fragments.mode.ModeStabilizeFragment;
import org.chaseme.polygon.PolygonPoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import org.chaseme.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Flight Activity UI
 */
public class FlightActivity extends SuperUI implements
		OnMapInteractionListener, OnMissionControlInteraction, OnDroneListener{

    private FragmentManager fragmentManager;
	private RCFragment rcFragment;
	private View failsafeTextView;
	private Fragment modeInfoPanel;
	private Fragment mapFragment;

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperUI#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight);

		fragmentManager = getSupportFragmentManager();
		// modeInfoPanel = fragmentManager.findFragmentById(R.id.modeInfoPanel);
		failsafeTextView = findViewById(R.id.failsafeTextView);

        //Load the activity fragments
        Fragment modeRtl = fragmentManager.findFragmentById(R.id.modeInfoPanel);
        if(modeRtl == null){
            modeRtl = new ModeRTLFragment();
            fragmentManager.beginTransaction().add(R.id.modeInfoPanel, modeRtl).commit();
        }

        mapFragment = fragmentManager.findFragmentById(R.id.mapFragment);
        if(mapFragment == null){
            mapFragment = new FlightMapFragment();
            fragmentManager.beginTransaction().add(R.id.mapFragment, mapFragment).commit();
        }

        Fragment telemetryFragment = fragmentManager.findFragmentById(R.id.telemetryFragment);
        if(telemetryFragment == null){
            telemetryFragment = new TelemetryFragment();
            fragmentManager.beginTransaction().add(R.id.telemetryFragment,
                    telemetryFragment).commit();
        }

        Fragment editorTools = fragmentManager.findFragmentById(R.id.editorToolsFragment);
        if(editorTools == null){
            editorTools = new FlightActionsFragment();
            fragmentManager.beginTransaction().add(R.id.editorToolsFragment, editorTools).commit();
        }
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperUI#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		onModeChanged(drone);	// Update the mode detail panel;
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.helpers.OnMapInteractionListener#onAddPoint(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onAddPoint(LatLng point) {
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.helpers.OnMapInteractionListener#onMoveHome(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMoveHome(LatLng coord) {
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.helpers.OnMapInteractionListener#onMoveWaypoint(org.chaseme.drone.variables.mission.waypoints.SpatialCoordItem, com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMoveWaypoint(SpatialCoordItem waypoint, LatLng latLng) {
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.helpers.OnMapInteractionListener#onMovePolygonPoint(org.chaseme.polygon.PolygonPoint, com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMovePolygonPoint(PolygonPoint source, LatLng newCoord) {
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.helpers.OnMapInteractionListener#onMapClick(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMapClick(LatLng point) {
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.helpers.OnMapInteractionListener#onMarkerClick(org.chaseme.drone.variables.mission.MissionItem)
	 */
	@Override
	public boolean onMarkerClick(MissionItem wp) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.FlightActionsFragment.OnMissionControlInteraction#onJoystickSelected()
	 */
	@Override
	public void onJoystickSelected() {
		toggleRCFragment();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.FlightActionsFragment.OnMissionControlInteraction#onPlanningSelected()
	 */
	@Override
	public void onPlanningSelected() {
		((DroneMap) mapFragment ).saveCameraPosition();
		Intent navigationIntent;
		navigationIntent = new Intent(this, EditorActivity.class);
		startActivity(navigationIntent);
	}

	/**
	 * Toggle the RC fragment
	 */
	private void toggleRCFragment() {
		if (rcFragment == null) {
			rcFragment = new RCFragment();
			fragmentManager.beginTransaction()
					.add(R.id.containerRC, rcFragment).commit();
		} else {
			fragmentManager.beginTransaction().remove(rcFragment).commit();
			rcFragment = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.helpers.OnMapInteractionListener#onMovingWaypoint(org.chaseme.drone.variables.mission.waypoints.SpatialCoordItem, com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMovingWaypoint(SpatialCoordItem source, LatLng latLng) {
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperUI#onDroneEvent(org.chaseme.drone.DroneInterfaces.DroneEventsType, org.chaseme.drone.Drone)
	 */
	@Override
	public void onDroneEvent(DroneEventsType event, Drone drone) {
		super.onDroneEvent(event,drone);
		switch (event) {
		case FAILSAFE:
			onFailsafeChanged(drone);
			break;
		case MODE:
			onModeChanged(drone);
			break;
		default:
			break;
		}

	}

	/**
	 * Update failsafe text visibility of drone on change
	 * @param drone
	 */
	public void onFailsafeChanged(Drone drone) {
		if (drone.state.isFailsafe()) {
			failsafeTextView.setVisibility(View.VISIBLE);
		} else {
			failsafeTextView.setVisibility(View.GONE);
		}
	}

	/**
	 * Change info mode panel when drone mode changes
	 * @param drone
	 */
	public void onModeChanged(Drone drone) {
		switch (drone.state.getMode()) {
		case ROTOR_RTL:
			modeInfoPanel = new ModeRTLFragment();
			break;
		case ROTOR_AUTO:
			modeInfoPanel = new ModeAutoFragment();
			break;
		case ROTOR_LAND:
			modeInfoPanel = new ModeLandFragment();
			break;
		case ROTOR_LOITER:
			modeInfoPanel = new ModeLoiterFragment();
			break;
		case ROTOR_STABILIZE:
			modeInfoPanel = new ModeStabilizeFragment();
			break;
		case ROTOR_ACRO:
			modeInfoPanel = new ModeAcroFragment();
			break;
		case ROTOR_ALT_HOLD:
			modeInfoPanel = new ModeAltholdFragment();
			break;
		case ROTOR_CIRCLE:
			modeInfoPanel = new ModeCircleFragment();
			break;
		case ROTOR_GUIDED:
			modeInfoPanel = new ModeGuidedFragment();
			break;
		case ROTOR_POSITION:
			modeInfoPanel = new ModePositionFragment();
			break;
		case ROTOR_TOY:
			modeInfoPanel = new ModeDriftFragment();
			break;
		case ROTOR_CHASEME:
			break;
		default:
			modeInfoPanel = new ModeDisconnectedFragment();
			break;
		}
		if (!drone.MavClient.isConnected()) {
			modeInfoPanel = new ModeDisconnectedFragment();
		}
		fragmentManager.beginTransaction()
				.replace(R.id.modeInfoPanel, modeInfoPanel).commit();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.HelpActivity#getHelpItems()
	 */
	@Override
	public CharSequence[][] getHelpItems() {
		return new CharSequence[][] {
				{ "Spline", "DP v3" },
				{ "https://www.youtube.com/watch?v=v9ydP-NWoJE",
						"https://www.youtube.com/watch?v=miwWUgX6nwY" } };
	}

}
