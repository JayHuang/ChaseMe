package org.chaseme.file;

import android.os.Environment;

public class DirectoryPath {

    static public String getchasemePath() {
		String root = Environment.getExternalStorageDirectory().getPath();
		return (root + "/chaseme/");
	}

	static public String getParametersPath() {
		return getchasemePath() + "/Parameters/";
	}

	static public String getWaypointsPath() {
		return getchasemePath() + "/Waypoints/";
	}

	static public String getGCPPath() {
		return getchasemePath() + "/GCP/";
	}

	static public String getTLogPath() {
		return getchasemePath() + "/Logs/";
	}

	static public String getMapsPath() {
		return getchasemePath() + "/Maps/";
	}

	public static String getCameraInfoPath() {
		return getchasemePath() + "/CameraInfo/";
	}
	
	public static String getLogCatPath() {
		return getchasemePath() + "/LogCat/";
	}

}
