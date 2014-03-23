package org.chaseme.fragments.helpers;

import org.chaseme.R;
import org.chaseme.calibration.CalParameters;
import org.chaseme.calibration.CalParameters.OnCalibrationEvent;
import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.drone.DroneInterfaces.OnDroneListener;
import org.chaseme.fragments.SetupRadioFragment;
import org.chaseme.fragments.calibration.FragmentSetupProgress;
import org.chaseme.fragments.calibration.FragmentSetupSend;
import org.chaseme.fragments.calibration.SetupMainPanel;
import org.chaseme.fragments.calibration.SetupSidePanel;

import android.os.Bundle;

/**
 * @author Enoch
 *
 */
@SuppressWarnings("unused")
public abstract class SuperSetupMainPanel extends SetupMainPanel implements
		OnCalibrationEvent, OnDroneListener {

	protected Drone drone;
	protected CalParameters parameters;

	protected abstract CalParameters getParameterHandler();

	protected abstract SetupSidePanel getDefaultPanel();

	protected abstract void updatePanelInfo();

	protected abstract void updateCalibrationData();
	
	protected void onInitialize(){};//can be overridden if necessary

	public SuperSetupMainPanel() {
		super();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.drone = parentActivity.drone;
		parameters = getParameterHandler();
		parameters.setOnCalibrationEventListener(this);
		onInitialize();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		doCalibrationStep(0);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		drone.events.addDroneListener(this);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		drone.events.removeDroneListener(this);
	}

	/* (non-Javadoc)
	 * @see org.chaseme.drone.DroneInterfaces.OnDroneListener#onDroneEvent(org.chaseme.drone.DroneInterfaces.DroneEventsType, org.chaseme.drone.Drone)
	 */
	@Override
	public void onDroneEvent(DroneEventsType event, Drone drone) {
		switch (event) {
		case PARAMETER:
			if (parameters != null) {
				parameters.processReceivedParam();
			}
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.chaseme.calibration.CalParameters.OnCalibrationEvent#onReadCalibration(org.chaseme.calibration.CalParameters)
	 */
	@Override
	public void onReadCalibration(CalParameters calParameters) {
		doCalibrationStep(0);
		updatePanelInfo();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.calibration.CalParameters.OnCalibrationEvent#onSentCalibration(org.chaseme.calibration.CalParameters)
	 */
	@Override
	public void onSentCalibration(CalParameters calParameters) {
		doCalibrationStep(0);
	}

	/* (non-Javadoc)
	 * @see org.chaseme.calibration.CalParameters.OnCalibrationEvent#onCalibrationData(org.chaseme.calibration.CalParameters, int, int, boolean)
	 */
	@Override
	public void onCalibrationData(CalParameters calParameters, int index,
			int count, boolean isSending) {
		if (sidePanel != null && parameters != null) {
			String title;
			if (isSending) {
				title = getResources().getString(
						R.string.setup_sf_desc_uploading);
			} else {
				title = getResources().getString(
						R.string.setup_sf_desc_downloading);
			}

			((FragmentSetupProgress) sidePanel).updateProgress(index + 1,
					count, title);
		}
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.calibration.SetupMainPanel#doCalibrationStep(int)
	 */
	@Override
	public void doCalibrationStep(int step) {
		switch (step) {
		case 3:
			uploadCalibrationData();
			break;
		case 0:
		default:
			sidePanel = getInitialPanel();
		}
	}

	
	/**
	 * Get the initial SetupSidepanel panel.
	 * @return Initial panel.
	 */
	protected SetupSidePanel getInitialPanel() {

		if (parameters != null && !parameters.isParameterDownloaded()
				&& drone.MavClient.isConnected()) {
			downloadCalibrationData();
		} else {
			sidePanel = getDefaultPanel();
			((SetupRadioFragment) getParentFragment())
					.changeSidePanel(sidePanel);

		}
		return sidePanel;
	}

	/**
	 * Get process panel of SetupSidePanel
	 * @param isSending boolean variable.
	 * @return Process panel.
	 */
	private SetupSidePanel getProgressPanel(boolean isSending) {
		sidePanel = ((SetupRadioFragment) getParentFragment())
				.changeSidePanel(new FragmentSetupProgress());

		if (isSending) {
			sidePanel.updateTitle(R.string.progress_title_uploading);
			sidePanel.updateDescription(R.string.progress_desc_uploading);
		} else {
			sidePanel.updateTitle(R.string.progress_title_downloading);
			sidePanel.updateDescription(R.string.progress_desc_downloading);
		}

		return sidePanel;
	}

	/**
	 * Upload Calibration Data.
	 */
	private void uploadCalibrationData() {
		if (parameters == null || !drone.MavClient.isConnected())
			return;

		sidePanel = getProgressPanel(true);

		updateCalibrationData();
		parameters.sendCalibrationParameters();
	}

	/**
	 * Download Calibration Data.
	 */
	private void downloadCalibrationData() {
		if (parameters == null || !drone.MavClient.isConnected())
			return;
		sidePanel = getProgressPanel(false);
		parameters.getCalibrationParameters(drone);
	}

	/**
	 * Get spinner index from value
	 * @param value int variable.
	 * @param valueList int array variable.
	 * @return The spinner index from value.
	 */
	protected int getSpinnerIndexFromValue(int value, int[] valueList) {
		for (int i = 0; i < valueList.length; i++) {
			if (valueList[i] == value)
				return i;
		}
		return -1;
	}

}