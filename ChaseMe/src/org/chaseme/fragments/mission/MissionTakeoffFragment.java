package org.chaseme.fragments.mission;

import org.chaseme.drone.variables.mission.waypoints.Takeoff;
import org.chaseme.helpers.units.Altitude;
import org.chaseme.widgets.SeekBarWithText.SeekBarWithText;
import org.chaseme.widgets.SeekBarWithText.SeekBarWithText.OnTextSeekBarChangedListener;

import android.view.View;

import org.chaseme.R;

public class MissionTakeoffFragment extends MissionDetailFragment implements
		OnTextSeekBarChangedListener {
	private SeekBarWithText altitudeSeekBar;

	@Override
	protected int getResource() {
		return R.layout.fragment_editor_detail_takeoff;
	}

	@Override
	protected void setupViews(View view) {
		super.setupViews(view);
		typeSpinner.setSelection(commandAdapter.getPosition(MissionItemTypes.TAKEOFF));

		Takeoff item = (Takeoff) this.item;

		altitudeSeekBar = (SeekBarWithText) view.findViewById(R.id.altitudeView);
		altitudeSeekBar.setValue(item.getAltitude().valueInMeters());
		altitudeSeekBar.setOnChangedListener(this);

	}

	@Override
	public void onSeekBarChanged() {
		Takeoff item = (Takeoff) this.item;
		item.setAltitude(new Altitude(altitudeSeekBar.getValue()));
	}

}
