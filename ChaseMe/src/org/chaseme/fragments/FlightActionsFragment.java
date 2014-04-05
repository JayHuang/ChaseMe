package org.chaseme.fragments;

import org.chaseme.ChaseMeApp;
import org.chaseme.drone.Drone;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.MAVLink.Messages.ApmModes;
import org.chaseme.R;

public class FlightActionsFragment extends Fragment implements	OnClickListener {

	public interface OnMissionControlInteraction {
		public void onJoystickSelected();

		public void onPlanningSelected();
	}

	private Drone drone;
	private OnMissionControlInteraction listener;
	private Button homeBtn;
	private Button missionBtn;
	private Button joystickBtn;
	private Button landBtn;
	private Button loiterBtn;
	private Button takeoffBtn;
	private Button followBtn;
	private boolean toggle = false;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mission_control,
				container, false);
		setupViews(view);
		setupListener();
		drone = ((ChaseMeApp) getActivity().getApplication()).drone;
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnMissionControlInteraction) activity;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Setup button views
	 * @param parentView
	 */
	private void setupViews(View parentView) {
		missionBtn = (Button) parentView.findViewById(R.id.mc_planningBtn);
		joystickBtn = (Button) parentView.findViewById(R.id.mc_joystickBtn);
		homeBtn = (Button) parentView.findViewById(R.id.mc_homeBtn);
		landBtn = (Button) parentView.findViewById(R.id.mc_land);
		takeoffBtn = (Button) parentView.findViewById(R.id.mc_takeoff);
		loiterBtn = (Button) parentView.findViewById(R.id.mc_loiter);
		followBtn = (Button) parentView.findViewById(R.id.mc_chaseme);
	}

	/**
	 * Setup listeners for all the buttons
	 */
	private void setupListener() {
		missionBtn.setOnClickListener(this);
		joystickBtn.setOnClickListener(this);
		homeBtn.setOnClickListener(this);
		landBtn.setOnClickListener(this);
		takeoffBtn.setOnClickListener(this);
		loiterBtn.setOnClickListener(this);
		followBtn.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mc_planningBtn:
			listener.onPlanningSelected();
			break;
		case R.id.mc_joystickBtn:
			listener.onJoystickSelected();
			break;
		case R.id.mc_land:
			drone.state.changeFlightMode(ApmModes.ROTOR_LAND);
			break;
		case R.id.mc_takeoff:
			drone.state.changeFlightMode(ApmModes.ROTOR_TAKEOFF);
			break;
		case R.id.mc_homeBtn:
			drone.state.changeFlightMode(ApmModes.ROTOR_RTL);
			break;
		case R.id.mc_loiter:
			drone.state.changeFlightMode(ApmModes.ROTOR_LOITER);
			break;
		case R.id.mc_chaseme:
			
			// if(drone.state.getMode().getName() != "ROTOR_CHASEME") {
			// 	drone.state.changeFlightMode(ApmModes.ROTOR_CHASEME);
			//	drone.takeOff(20); // Put drone 20 feet into the air
				
			// 	drone.followMe(drone.getFollowMe().getLocation().getLatitude(), drone.getFollowMe().getLocation().getLongitude());
				
			// } else {
			// 	drone.state.changeFlightMode(ApmModes.ROTOR_LOITER);
			// 	drone.land(drone.getFollowMe().getLocation().getLatitude(), drone.getFollowMe().getLocation().getLongitude());
			// }
			if(toggle == false){
				toggle = true;
				followBtn.setText("Don't Chase Me");
				Toast.makeText(getActivity(), "TakeOff initialized", 1).show();
				Toast.makeText(getActivity(), "		Chase Me initialized		", 1).show();
			} else {
				toggle = false;
				followBtn.setText("Chase Me!");
				Toast.makeText(getActivity(), "Chase Me disengaged", 1).show();
				Toast.makeText(getActivity(), "Land initialized", 1).show();
			}
			
			break;
		}
	}

}
