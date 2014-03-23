package org.chaseme.drone.variables.mission.waypoints;

import java.util.List;

import org.chaseme.drone.variables.mission.MissionItem;
import org.chaseme.fragments.markers.MarkerManager.MarkerSource;
import org.chaseme.fragments.mission.MissionDetailFragment;
import org.chaseme.fragments.mission.MissionRegionOfInterestFragment;

import com.MAVLink.Messages.ardupilotmega.msg_mission_item;
import org.chaseme.R;
import com.google.android.gms.maps.model.LatLng;

public class RegionOfInterest extends SpatialCoordItem implements MarkerSource{
	
	public RegionOfInterest(MissionItem item) {
		super(item);
	}
	
	@Override
	public List<LatLng> getPath() throws Exception {
		throw new Exception();
	}

	@Override
	public MissionDetailFragment getDetailFragment() {
		MissionDetailFragment fragment = new MissionRegionOfInterestFragment();
		fragment.setItem(this);
		return fragment;
	}

	@Override
	public List<msg_mission_item> packMissionItem() {
				return super.packMissionItem();
	}

	@Override
	public void unpackMAVMessage(msg_mission_item mavMsg) {
				super.unpackMAVMessage(mavMsg);
	}

	@Override
	protected int getIconDrawable() {
		return R.drawable.ic_wp_map;
	}
	
	@Override
	protected int getIconDrawableSelected() {
		return R.drawable.ic_wp_map_selected;
	}
	
	/*
	private static String getRoiDetail(GenericWaypoint wp, Context context) {
		if (wp.getParam1() == MAV_ROI.MAV_ROI_WPNEXT)
			return context.getString(R.string.next);
		else if (wp.getParam1() == MAV_ROI.MAV_ROI_TARGET)
			return String.format(Locale.US,"wp#%.0f", wp.getParam2());
		else if (wp.getParam1() == MAV_ROI.MAV_ROI_TARGET)
			return String.format(Locale.US,"tg#%.0f", wp.getParam2());
		else
			return "";
	}
	*/
}
