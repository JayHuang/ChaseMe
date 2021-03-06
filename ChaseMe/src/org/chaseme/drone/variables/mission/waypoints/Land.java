package org.chaseme.drone.variables.mission.waypoints;

import java.util.List;

import org.chaseme.drone.variables.mission.MissionItem;
import org.chaseme.fragments.markers.MarkerManager.MarkerSource;
import org.chaseme.fragments.mission.MissionDetailFragment;
import org.chaseme.fragments.mission.MissionLandFragment;

import com.MAVLink.Messages.ardupilotmega.msg_mission_item;
import com.MAVLink.Messages.enums.MAV_CMD;
import org.chaseme.R;

public class Land extends SpatialCoordItem implements MarkerSource {

	private double yawAngle;

	public Land(MissionItem item) {
		super(item);
	}

	@Override
	public MissionDetailFragment getDetailFragment() {
		MissionDetailFragment fragment = new MissionLandFragment();
		fragment.setItem(this);
		return fragment;
	}

	@Override
	public List<msg_mission_item> packMissionItem() {
		List<msg_mission_item> list = super.packMissionItem();
		msg_mission_item mavMsg = list.get(0);
		mavMsg.command = MAV_CMD.MAV_CMD_NAV_LAND;
		mavMsg.param4 = (float) getYawAngle();
		return list;
	} 

	@Override
	public void unpackMAVMessage(msg_mission_item mavMsg) {
		super.unpackMAVMessage(mavMsg);
		setYawAngle(mavMsg.param4);
	}

	public double getYawAngle() {
		return yawAngle;
	}

	public void setYawAngle(double yawAngle) {
		this.yawAngle = yawAngle;
	}

	@Override
	protected int getIconDrawable() {
		return R.drawable.ic_wp_land;
	}
	
	@Override
	protected int getIconDrawableSelected() {
		return R.drawable.ic_wp_lan_selected;
	}

}