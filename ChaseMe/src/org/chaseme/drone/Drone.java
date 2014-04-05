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

	public FollowMe getFollowMe() {
		return fm;
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
	private msg_mission_item itemReturn;
		
	/**
	 * This method sends a takeoff command in Mavlink that flies the drone up to the specified altitude.
	 * @param altitude 
	 */
	public void takeOff(float altitude){
		
		fm.toogleFollowMeState();
		this.alt = altitude;
		item = new msg_mission_item();
		item.autocontinue = 1;
		item.compid = 1;
		//Set the mavlink command to takeoff.
		item.command = MAV_CMD.MAV_CMD_NAV_TAKEOFF;
		//set minimum pitch
		item.param1 = 0;
		//set yaw angle
		item.param4 = 0;
		
		Location location = fm.getLocation();
		
		item.x      = (float) location.getLatitude();
		item.y		= (float) location.getLongitude();
		item.z		= altitude;
		//Send the mavlink command to the drone.
		MavClient.sendMavPacket(item.pack());	
	}
	/**
	 * Set the phone's gps location to home and have the drone return to launch (home).
	 * @param lat The latitude of the phone's location
	 * @param lon The longitude of the phone's location
	 */
	public void followMe(double lat, double lon){
		//Set the mavlink command to set home destination.
		item.command = MAV_CMD.MAV_CMD_DO_SET_HOME;
		
		//use specified location == param1
		item.param1 = 0;
		item.x      = (float) lat;
		item.y		= (float) lon;
		item.z		= alt;
		//Send the mavlink command to the drone.
		MavClient.sendMavPacket(item.pack());
		
		//Set the mavlink comand to return to launch(home).
		itemReturn.command = MAV_CMD.MAV_CMD_NAV_RETURN_TO_LAUNCH;
		//Send the mavlink command to the drone.
		MavClient.sendMavPacket(itemReturn.pack());
	}
	/**
	 * Send a mavlink command that tells the drone to land at the phone's gps location.
	 * @param lat The latitude of the phone's location
	 * @param lon The longitude of the phone's location
	 */
	public void land(double lat, double lon){
		//Set the mavlink command to land.
		item.command = MAV_CMD.MAV_CMD_NAV_LAND;
		item.param4 = 0;
		item.x = (float) lat;
		item.y = (float) lon;
		item.z = 0;
		//Send the mavlink command to land.
		MavClient.sendMavPacket(item.pack());
	}

}
