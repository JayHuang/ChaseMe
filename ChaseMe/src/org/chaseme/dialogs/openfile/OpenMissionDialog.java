package org.chaseme.dialogs.openfile;

import org.chaseme.drone.Drone;
import org.chaseme.file.IO.MissionReader;

public abstract class OpenMissionDialog extends OpenFileDialog {
	public abstract void waypointFileLoaded(MissionReader reader);

	Drone drone;

	public OpenMissionDialog(Drone drone) {
		super();
		this.drone = drone;
	}

	@Override
	protected FileReader createReader() {
		return new MissionReader();
	}

	@Override
	protected void onDataLoaded(FileReader reader) {
		waypointFileLoaded((MissionReader) reader);
	}
}
