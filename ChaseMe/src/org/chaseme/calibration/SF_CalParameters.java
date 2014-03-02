package org.chaseme.calibration;

import org.chaseme.drone.Drone;

public class SF_CalParameters extends CalParameters {

	public SF_CalParameters(Drone myDrone) {
		super(myDrone);
		calParameterNames.add("RC5_FUNCTION");
		calParameterNames.add("RC6_FUNCTION");
		calParameterNames.add("RC7_FUNCTION");
		calParameterNames.add("RC8_FUNCTION");
		calParameterNames.add("RC10_FUNCTION");
		calParameterNames.add("RC11_FUNCTION");
	}
}