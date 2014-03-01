package org.chaseme.drone.variables.mission.waypoints;

import java.util.List;

import org.chaseme.drone.variables.mission.MissionItem;
import org.chaseme.fragments.markers.MarkerManager.MarkerSource;
import org.chaseme.fragments.mission.MissionDetailFragment;
import org.chaseme.fragments.mission.MissionLoiterFragment;

import com.MAVLink.Messages.ardupilotmega.msg_mission_item;

public class LoiterInfinite extends Loiter implements MarkerSource {
	
	public LoiterInfinite(MissionItem item) {
		super(item);
	}

	@Override
	public MissionDetailFragment getDetailFragment() {
		MissionDetailFragment fragment = new MissionLoiterFragment();
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

}