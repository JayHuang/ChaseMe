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
import org.chaseme.helpers.FollowMe;
import org.chaseme.helpers.TTS;
import org.chaseme.service.MAVLinkClient;

import com.MAVLink.Messages.MAVLinkPacket;
import com.MAVLink.Messages.ardupilotmega.msg_mission_item;
import com.MAVLink.Messages.enums.MAV_CMD;

import android.content.Context;
import android.location.Location;


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

	private FollowMe fm;
	public TTS tts;
	public MAVLinkClient MavClient;
	public Context context;

	public Drone(TTS tts, MAVLinkClient mavClient, Context context) {
		this.MavClient = mavClient;
		this.context = context;
		this.tts = tts;
		events.addDroneListener(tts);
		fm = new FollowMe(context, this);
		
		
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
	
	private float alt;
	private msg_mission_item item;
		
		
	public void takeOff(float altitude){
		fm.toogleFollowMeState();
		this.alt = altitude;
		item = new msg_mission_item();
		item.autocontinue = 1;
		item.compid = 1;
		item.command = MAV_CMD.MAV_CMD_NAV_TAKEOFF;
		//set minimum pitch
		item.param1 = 0;
		//set yaw angle
		item.param4 = 0;
		
		Location location = fm.getLocation();
		
		//TODO get GPS latitude with google api
		item.x      = (float) location.getLatitude();
		//TODO get GPS longitude with google api
		item.y		= (float) location.getLongitude();
		item.z		= altitude;
		//TODO get GPS latitude with google api
		item.x      = (float) location.getLatitude();
		//TODO get GPS longitude with google api
		item.y		= (float) location.getLongitude();
		item.z		= altitude;
		//TODO send packet with above info
		MavClient.sendMavPacket(item.pack());
	}
	
	public void followMe(double lat, double lon){
		item.command = MAV_CMD.MAV_CMD_DO_SET_HOME;
		
		//use specified location == param1
		item.param1 = 0;
		
		Location location = fm.getLocation();
		
		//TODO get GPS latitude with google api
		item.x      = (float) lat;
		//TODO get GPS longitude with google api
		item.y		= (float) lon;
		item.z		= alt;
		
		//TODO send message to mavlink
		item.command = MAV_CMD.MAV_CMD_NAV_RETURN_TO_LAUNCH;
		//TODO send message to mavlink
		MavClient.sendMavPacket(item.pack());
		//TODO send packet with above info
	}
	
	public void landOrCrashTrying(double lat, double lan){
		item.command = MAV_CMD.MAV_CMD_NAV_LAND;
		//TODO bring the drone back to a set distance to the phone and land.
	}

}
