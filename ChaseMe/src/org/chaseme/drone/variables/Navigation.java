package org.chaseme.drone.variables;

import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneVariable;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;

public class Navigation extends DroneVariable {

	private double nav_pitch;
	private double nav_roll;
	private double nav_bearing;

	public Navigation(Drone myDrone) {
		super(myDrone);
	}

	public void setNavPitchRollYaw(float nav_pitch, float nav_roll,
			short nav_bearing) {
		this.nav_pitch = (double) nav_pitch;
		this.nav_roll = (double) nav_roll;
		this.nav_bearing = (double) nav_bearing;
		myDrone.events.notifyDroneEvent(DroneEventsType.NAVIGATION);
	}

	public double getNavPitch() {
		return nav_pitch;
	}

	public double getNavRoll() {
		return nav_roll;
	}

	public double getNavBearing() {
		return nav_bearing;
	}

}
