package org.chaseme.fragments.mission;

import org.chaseme.drone.variables.mission.waypoints.RegionOfInterest;
import org.chaseme.helpers.units.Altitude;
import org.chaseme.widgets.SeekBarWithText.SeekBarWithText;
import org.chaseme.widgets.SeekBarWithText.SeekBarWithText.OnTextSeekBarChangedListener;

import android.view.View;

import org.chaseme.R;

public class MissionRegionOfInterestFragment extends MissionDetailFragment
		implements OnTextSeekBarChangedListener {

	private SeekBarWithText altitudeSeekBar;

	@Override
	protected int getResource() {
		return R.layout.fragment_editor_detail_roi;
	}

	@Override
	protected void setupViews(View view) {
		super.setupViews(view);
		typeSpinner.setSelection(commandAdapter
				.getPosition(MissionItemTypes.ROI));

		altitudeSeekBar = (SeekBarWithText) view.findViewById(R.id.altitudeView);
		altitudeSeekBar.setValue(((RegionOfInterest) item).getAltitude().valueInMeters());
		altitudeSeekBar.setOnChangedListener(this);
	}

	@Override
	public void onSeekBarChanged() {
		((RegionOfInterest) item).setAltitude(new Altitude(altitudeSeekBar
				.getValue()));
	}

}
