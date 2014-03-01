package org.chaseme.fragments;

import org.chaseme.R;
import org.chaseme.fragments.calibration.SetupMainPanel;
import org.chaseme.fragments.calibration.imu.FragmentSetupIMU;
import org.chaseme.fragments.calibration.mag.FragmentSetupMAG;
import org.chaseme.fragments.helpers.SuperSetupFragment;

/**
 * This fragment is used to calibrate the drone's compass, and accelerometer.
 */
public class SetupSensorFragment extends SuperSetupFragment {

	@Override
	public SetupMainPanel initMainPanel() {
		return new FragmentSetupIMU();
	}

	@Override
	public int getSpinnerItems() {
		return R.array.Setup_Sensor_Menu;
	}

	@Override
	public SetupMainPanel getMainPanel(int index) {
		switch (index) {
		case 0:
			updateTitle(R.string.setup_imu_title);
			return new FragmentSetupIMU();
		case 1:
			updateTitle(R.string.setup_mag_title);
			return new FragmentSetupMAG();
		}
		return null;
	}

	@Override
	public void updateTitle(int id) {
		super.updateTitle(id);
	}
}
