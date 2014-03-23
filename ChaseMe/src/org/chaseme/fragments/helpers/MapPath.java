package org.chaseme.fragments.helpers;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * The path of the Map.
 */
public class MapPath {
	private static final int DEFAULT_COLOR = Color.WHITE;
	private static final int DEFAULT_WITDH = 2;

	public interface PathSource {
		public List<LatLng> getPathPoints();
	}

	public Polyline missionPath;
	private GoogleMap mMap;
	private float width;
	private int color;


	/**
	 * Constructor that takes in 2 variables: GoogleMap variable and Resources variable
	 * @param mMap GoogleMap variable.
	 * @param resources Resources variable.
	 */
	public MapPath(GoogleMap mMap, Resources resources) {
		this(mMap, DEFAULT_COLOR, resources);
	}
	
	/**
	 * Constructor that takes in 3 variables: GoogleMap variable, int variable, and Resources.
	 * @param mMap GoogleMap variable.
	 * @param color int variable for color.
	 * @param resources Resource variable.
	 */
	public MapPath(GoogleMap mMap, int color, Resources resources) {
		this(mMap, color, DEFAULT_WITDH, resources);
	}
	
	/**
	 * Constructor that take in 4 variables: GoogleMap variable, int variable, float variable, and Resrouces.
	 * @param mMap GoogleMap variable.
	 * @param color int variable for color.
	 * @param width float variable for width.
	 * @param resources Resource variable.
	 */
	private MapPath(GoogleMap mMap, int color, float width, Resources resources) {
		this.mMap = mMap;
		this.color = color;
		this.width = scaleDpToPixels(width, resources);
	}	

	/** 
	 * Scale the Map
	 * @param value Double variable.
	 * @param res Resource variable.
	 * @return Scaled value for the map.
	 */
	private int scaleDpToPixels(double value, Resources res) {
		final float scale = res.getDisplayMetrics().density;
		return (int) Math.round(value*scale);
	}

	
	/**
	 * Update the path.
	 * @param pathSource PathSource variable.
	 */
	public void update(PathSource pathSource) {
		addToMapIfNeeded();
		List<LatLng> newPath = pathSource.getPathPoints();
		missionPath.setPoints(newPath);
	}

	/**
	 * Add missionPath if missionpath isn't initialized.
	 */
	private void addToMapIfNeeded() {
		if (missionPath == null) {
			PolylineOptions flightPath = new PolylineOptions();
			flightPath.color(color).width(width);
			missionPath = mMap.addPolyline(flightPath);
		}
	}
}