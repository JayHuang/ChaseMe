package org.chaseme.fragments.helpers;

import org.chaseme.ChaseMeApp;
import org.chaseme.activities.ConfigurationActivity;
import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.drone.DroneInterfaces.OnDroneListener;
import org.chaseme.fragments.calibration.SetupMainPanel;
import org.chaseme.fragments.calibration.SetupSidePanel;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.chaseme.R;

/**
 * This fragment is used to calibrate the drone's compass, and accelerometer.
 */
public abstract class SuperSetupFragment extends Fragment implements OnDroneListener, OnItemSelectedListener {

	private Drone drone;

	protected ConfigurationActivity parentActivity;
	private Spinner spinnerSetup;
	private TextView textViewTitle;

	private FragmentManager fragmentManager;
	private SetupMainPanel setupPanel;
    private SetupSidePanel sidePanel;

	public abstract int getSpinnerItems();
	public abstract SetupMainPanel getMainPanel(int index);
	public abstract SetupMainPanel initMainPanel();

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        if(!(activity instanceof ConfigurationActivity)){
            throw new IllegalStateException("Parent activity must be " + ConfigurationActivity
                    .class.getName());
        }

        parentActivity = (ConfigurationActivity)activity;
	}

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDetach()
     */
    @Override
    public void onDetach(){
        super.onDetach();
        parentActivity = null;
    }

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

  		final View view = inflater.inflate(R.layout.fragment_setup, container, false);

        setupLocalViews(view);

        fragmentManager = getChildFragmentManager();
        setupPanel = (SetupMainPanel) fragmentManager.findFragmentById(R.id
                .fragment_setup_mainpanel);

        if (setupPanel == null) {
            setupPanel = initMainPanel();

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_setup_mainpanel, setupPanel)
                    .commit();
        }

        sidePanel =  (SetupSidePanel) fragmentManager.findFragmentById(R.id.fragment_setup_sidepanel);
        if (sidePanel == null) {
            sidePanel = setupPanel.getSidePanel();
            if (sidePanel != null) {
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_setup_sidepanel, sidePanel)
                        .commit();
            }
        }

		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
		this.drone = ((ChaseMeApp) getActivity().getApplication()).drone;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		drone.events.addDroneListener(this);
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		drone.events.removeDroneListener(this);
		super.onStop();
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		changeMainPanel(arg2);
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	/* (non-Javadoc)
	 * @see org.chaseme.drone.DroneInterfaces.OnDroneListener#onDroneEvent(org.chaseme.drone.DroneInterfaces.DroneEventsType, org.chaseme.drone.Drone)
	 */
	@Override
	public void onDroneEvent(DroneEventsType event, Drone drone) {
		
	}

	/**
	 * Update the Title
	 * @param id Id for the Title.
	 */
	public void updateTitle(int id){
		textViewTitle.setText(id);
	}

	/**
	 * Set up Local views
	 * @param view View variable.
	 */
	private void setupLocalViews(View view) {
		textViewTitle = (TextView)view.findViewById(R.id.textViewSetupTitle);
		spinnerSetup = (Spinner)view.findViewById(R.id.spinnerSetupType);
		spinnerSetup.setOnItemSelectedListener(this);

		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(parentActivity,
		        getSpinnerItems(), R.layout.spinner_setup);

		if(adapter!=null)
			spinnerSetup.setAdapter(adapter);
	}

	/**
	 * Set up main panel
	 * @param step int variable.
	 * @return setupPanel
	 */
	public SetupMainPanel changeMainPanel(int step) {
		setupPanel = getMainPanel(step);
		sidePanel = setupPanel.getSidePanel();

		final FragmentTransaction ft = fragmentManager.beginTransaction();
        if(setupPanel != null){
            ft.replace(R.id.fragment_setup_mainpanel, setupPanel);
        }

        if(sidePanel != null){
            ft.replace(R.id.fragment_setup_sidepanel, sidePanel);
        }

		ft.commit();
		return setupPanel;
	}

	/**
	 * Setup side panel
	 * @param sPanel SetupSidePanel variable.
	 * @return The side panel.
	 */
	public SetupSidePanel changeSidePanel(SetupSidePanel sPanel) {
		sidePanel = sPanel;

		if(setupPanel != null && sidePanel != null)
			setupPanel.setSidePanel(sidePanel);

		final FragmentTransaction ft = fragmentManager.beginTransaction();
        if(sidePanel != null){
            ft.replace(R.id.fragment_setup_sidepanel, sidePanel);
        }

		ft.commit();

		return sidePanel;
	}

    /**
     * Calibrate Step
     * @param step int variable.
     */
    public void doCalibrationStep(int step){
        if(setupPanel != null){
            setupPanel.doCalibrationStep(step);
        }
    }

    /**
     * Update the side panel title.
     * @param calibrationStep int variable calibration step.
     */
    public void updateSidePanelTitle(int calibrationStep){
        if(sidePanel != null){
            sidePanel.updateDescription(calibrationStep);
        }
    }

}
