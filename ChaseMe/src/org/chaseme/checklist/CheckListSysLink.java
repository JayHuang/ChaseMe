package org.chaseme.checklist;

import org.chaseme.MAVLink.MavLinkArm;
import org.chaseme.drone.Drone;
import org.chaseme.helpers.units.Altitude;

public class CheckListSysLink {
	private Drone drone;
	public CheckListSysLink(Drone mDrone) {
		this.drone = mDrone;
	}

	public void getSystemData(CheckListItem mListItem, String mSysTag) {
		if(mSysTag==null)
			return;
		
		if (mSysTag.equalsIgnoreCase("SYS_BATTREM_LVL")) {
			mListItem.setSys_value(drone.battery.getBattRemain());
		} else if (mSysTag.equalsIgnoreCase("SYS_BATTVOL_LVL")) {
			mListItem.setSys_value(drone.battery.getBattVolt());
		} else if (mSysTag.equalsIgnoreCase("SYS_BATTCUR_LVL")) {
			mListItem.setSys_value(drone.battery.getBattCurrent());
		} else if (mSysTag.equalsIgnoreCase("SYS_GPS3D_LVL")) {
			mListItem.setSys_value(drone.GPS.getSatCount());
		} else if (mSysTag.equalsIgnoreCase("SYS_DEF_ALT")) {
			mListItem.setSys_value(drone.mission.getDefaultAlt().valueInMeters());
		} else if (mSysTag.equalsIgnoreCase("SYS_ARM_STATE")) {
			mListItem.setSys_activated(drone.state.isArmed());
		} else if (mSysTag.equalsIgnoreCase("SYS_FAILSAFE_STATE")) {
			mListItem.setSys_activated(drone.state.isFailsafe());
		} else if (mSysTag.equalsIgnoreCase("SYS_CONNECTION_STATE")) {
			mListItem.setSys_activated(drone.MavClient.isConnected());
		}	
	}

	public void setSystemData(CheckListItem checkListItem) {
		
		if (checkListItem.getSys_tag()==null)
			return;
		
		if (checkListItem.getSys_tag().equalsIgnoreCase("SYS_CONNECTION_STATE")) {
			doSysConnect(checkListItem, checkListItem.isSys_activated());

		} else if (checkListItem.getSys_tag().equalsIgnoreCase("SYS_ARM_STATE")) {
			doSysArm(checkListItem, checkListItem.isSys_activated());
			
		} else if (checkListItem.getSys_tag().equalsIgnoreCase("SYS_DEF_ALT")) {
			doDefAlt(checkListItem);
			
		}
	}

	private void doDefAlt(CheckListItem checkListItem) {
		drone.mission.setDefaultAlt(new Altitude(checkListItem.getFloatValue()));		
	}

	private void doSysArm(CheckListItem checkListItem, boolean arm) {
		if (drone.MavClient.isConnected()) {
			if (checkListItem.isSys_activated() && !drone.state.isArmed()) {
				drone.tts.speak("Arming the vehicle, please standby");
				MavLinkArm.sendArmMessage(drone, true);
			} else {
				MavLinkArm.sendArmMessage(drone, false);
			}
		}
	}

	private void doSysConnect(CheckListItem checkListItem, boolean connect) {
		boolean activated = checkListItem.isSys_activated();
		boolean connected = drone.MavClient.isConnected();
		if (activated != connected){
			drone.MavClient.toggleConnectionState();
		}
	}

}
