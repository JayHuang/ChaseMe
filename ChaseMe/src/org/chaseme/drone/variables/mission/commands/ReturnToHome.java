package org.chaseme.drone.variables.mission.commands;

import java.util.List;

import org.chaseme.drone.variables.mission.MissionItem;
import org.chaseme.fragments.mission.MissionDetailFragment;
import org.chaseme.fragments.mission.MissionRTLFragment;
import org.chaseme.helpers.units.Altitude;

import com.MAVLink.Messages.ardupilotmega.msg_mission_item;

public class ReturnToHome extends MissionCMD{
	private Altitude returnAltitude;
	
	public ReturnToHome(MissionItem item) {
		super(item);
		returnAltitude = new Altitude(0);
	}

	@Override
	public MissionDetailFragment getDetailFragment() {
		MissionDetailFragment fragment = new MissionRTLFragment();
		fragment.setItem(this);
		return fragment;
	}

	public Altitude  getHeight() {
		return returnAltitude;
	}

	public void setHeight(Altitude altitude) {
		returnAltitude = altitude;
	}

	@Override
	public List<msg_mission_item> packMissionItem() {
		return null;
	}

	@Override
	public void unpackMAVMessage(msg_mission_item mavMessageItem) {
	}
}
