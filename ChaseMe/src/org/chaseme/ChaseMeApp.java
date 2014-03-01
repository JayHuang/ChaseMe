package org.chaseme;

import org.chaseme.MAVLink.MavLinkMsgHandler;
import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.helpers.FollowMe;
import org.chaseme.helpers.RecordMe;
import org.chaseme.helpers.TTS;
import org.chaseme.service.MAVLinkClient;
import org.chaseme.service.MAVLinkClient.OnMavlinkClientListener;

import com.MAVLink.Messages.MAVLinkMessage;

public class ChaseMeApp extends ErrorReportApp implements
		OnMavlinkClientListener {
	public Drone drone;
	private MavLinkMsgHandler mavLinkMsgHandler;
	public FollowMe followMe;
	public RecordMe recordMe;
	private TTS tts;

	@Override
	public void onCreate() {
		super.onCreate();

		tts = new TTS(this);
		MAVLinkClient MAVClient = new MAVLinkClient(this, this);
		drone = new Drone(tts, MAVClient, getApplicationContext());
		followMe = new FollowMe(this, drone);
		recordMe = new RecordMe(this, drone);
		mavLinkMsgHandler = new org.chaseme.MAVLink.MavLinkMsgHandler(
				drone);
	}

	@Override
	public void notifyReceivedData(MAVLinkMessage msg) {
		mavLinkMsgHandler.receiveData(msg);
	}

	@Override
	public void notifyTimeOut(int timeOutCount) {
		if (drone.waypointMananger.processTimeOut(timeOutCount)) {
			tts.speak("Retrying - " + String.valueOf(timeOutCount));
		} else {
			tts.speak("MAVLink has timed out");
		}
	}

	@Override
	public void notifyConnected() {
		drone.events.notifyDroneEvent(DroneEventsType.CONNECTED);
	}

	@Override
	public void notifyDisconnected() {
		drone.events.notifyDroneEvent(DroneEventsType.DISCONNECTED);
	}
}
