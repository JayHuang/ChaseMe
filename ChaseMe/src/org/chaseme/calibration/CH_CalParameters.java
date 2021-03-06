package org.chaseme.calibration;

import org.chaseme.drone.Drone;

public class CH_CalParameters extends CalParameters {

	public CH_CalParameters(Drone myDrone) {
		super(myDrone);
			calParameterNames.add("CH7_OPT");
			calParameterNames.add("CH8_OPT");
			calParameterNames.add("TUNE");
			calParameterNames.add("TUNE_LOW");
			calParameterNames.add("TUNE_HIGH");
	}
}
