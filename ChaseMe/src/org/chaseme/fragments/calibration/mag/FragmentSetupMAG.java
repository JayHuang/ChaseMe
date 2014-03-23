package org.chaseme.fragments.calibration.mag;

import org.chaseme.fragments.calibration.SetupMainPanel;
import org.chaseme.fragments.calibration.SetupSidePanel;

import org.chaseme.R;
import android.view.View;

public class FragmentSetupMAG extends SetupMainPanel {

	@Override
	public int getPanelLayout() {
		return R.layout.fragment_setup_mag_main;
	}

	@Override
	public SetupSidePanel getSidePanel() {
				return null;
	}

	@Override
	public void setupLocalViews(View v) {
				
	}

	@Override
	public void doCalibrationStep(int step) {
				
	}
}
