package org.chaseme.drone.variables;

import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneVariable;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;

public class Battery extends DroneVariable {
	private double battVolt = -1;
	private double battRemain = -1;
	private double battCurrent = -1;

	public Battery(Drone myDrone) {
		super(myDrone);
	}

	public double getBattVolt() {
		return battVolt;
	}

	public double getBattRemain() {
		return battRemain;
	}

	public double getBattCurrent() {
		return battCurrent;
	}

	public void setBatteryState(double battVolt, double battRemain,
			double battCurrent) {
		if (this.battVolt != battVolt | this.battRemain != battRemain
				| this.battCurrent != battCurrent) {
			this.battVolt = battVolt;
			this.battRemain = battRemain;
			this.battCurrent = battCurrent;
			myDrone.events.notifyDroneEvent(DroneEventsType.BATTERY);
		}
	}
}