package org.chaseme.drone;


import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.drone.variables.Altitude;
import org.chaseme.drone.variables.Battery;
import org.chaseme.drone.variables.Calibration;
import org.chaseme.drone.variables.GPS;
import org.chaseme.drone.variables.GuidedPoint;
import org.chaseme.drone.variables.HeartBeat;
import org.chaseme.drone.variables.Home;
import org.chaseme.drone.variables.MissionStats;
import org.chaseme.drone.variables.Navigation;
import org.chaseme.drone.variables.Orientation;
import org.chaseme.drone.variables.Parameters;
import org.chaseme.drone.variables.Profile;
import org.chaseme.drone.variables.RC;
import org.chaseme.drone.variables.Radio;
import org.chaseme.drone.variables.Speed;
import org.chaseme.drone.variables.State;
import org.chaseme.drone.variables.StreamRates;
import org.chaseme.drone.variables.Type;
import org.chaseme.drone.variables.mission.Mission;
import org.chaseme.drone.variables.mission.WaypointMananger;
import org.chaseme.helpers.TTS;
import org.chaseme.service.MAVLinkClient;

import android.content.Context;


public class Drone {
	public DroneEvents events = new DroneEvents(this);
	public Type type = new Type(this);
	public Profile profile = new Profile(this);
	public GPS GPS = new GPS(this);
	public RC RC = new RC(this);
	public Speed speed = new Speed(this);
	public State state = new State(this);
	public Battery battery = new Battery(this);
	public Radio radio = new Radio(this);
	public Home home = new Home(this);
	public Mission mission = new Mission(this);
	public MissionStats missionStats = new MissionStats(this);
	public StreamRates streamRates = new StreamRates(this);
	public HeartBeat heartbeat = new HeartBeat(this);
	public Altitude altitude = new Altitude(this);
	public Orientation orientation = new Orientation(this);
	public Navigation navigation = new Navigation(this);
	public GuidedPoint guidedPoint = new GuidedPoint(this);
	public Parameters parameters = new Parameters(this);
	public Calibration calibrationSetup = new Calibration(this);
	public WaypointMananger waypointMananger = new WaypointMananger(this);

	public TTS tts;
	public MAVLinkClient MavClient;
	public Context context;

	public Drone(TTS tts, MAVLinkClient mavClient, Context context) {
		this.MavClient = mavClient;
		this.context = context;
		this.tts = tts;
		events.addDroneListener(tts);
		
		profile.load();
	}

	public void setAltitudeGroundAndAirSpeeds(double altitude,
			double groundSpeed, double airSpeed, double climb) {
		this.altitude.setAltitude(altitude);
		speed.setGroundAndAirSpeeds(groundSpeed, airSpeed, climb);
		events.notifyDroneEvent(DroneEventsType.SPEED);
	}

	public void setDisttowpAndSpeedAltErrors(double disttowp, double alt_error,
			double aspd_error) {
		missionStats.setDistanceToWp(disttowp);
		altitude.setAltitudeError(alt_error);
		speed.setSpeedError(aspd_error);
		events.notifyDroneEvent(DroneEventsType.ORIENTATION);
	}

}
