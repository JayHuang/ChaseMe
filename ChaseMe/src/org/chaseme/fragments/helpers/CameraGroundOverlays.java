package org.chaseme.fragments.helpers;

import java.util.ArrayList;
import java.util.List;

import org.chaseme.drone.variables.mission.survey.SurveyData;
import org.chaseme.helpers.geoTools.GeoTools;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Takes care of the overlays over the GoogleMap map.
 */
public class CameraGroundOverlays {
	public ArrayList<Polygon> cameraOverlays = new ArrayList<com.google.android.gms.maps.model.Polygon>();
	private GoogleMap mMap;

	/**
	 * Constructor that takes in GoogleMap as a param
	 * @param mMap GoogleMap variable.
	 */
	public CameraGroundOverlays(GoogleMap mMap) {
		this.mMap = mMap;
	}

	/**
	 * Adds the overlays over the map.
	 * @param cameraLocations Camera locations.
	 * @param surveyData Data of the survey.
	 */
	public void addOverlays(List<LatLng> cameraLocations,
			SurveyData surveyData) {
		for (LatLng latLng : cameraLocations) {
			addOneFootprint(latLng, surveyData);
		}
	}


	/**
	 * Removes all the overlays
	 */
	public void removeAll() {
		for (com.google.android.gms.maps.model.Polygon overlay : cameraOverlays) {
			overlay.remove();
		}
		cameraOverlays.clear();
	}

	/**
	 * Adds a Footprint
	 * @param latLng Latitude and longitude variable.
	 * @param surveyData Data of survey.
	 */
	private void addOneFootprint(LatLng latLng, SurveyData surveyData) {
		double lng = surveyData.getLateralFootPrint().valueInMeters();
		double lateral = surveyData.getLongitudinalFootPrint().valueInMeters();
		double halfDiag = Math.hypot(lng, lateral) / 2;
		double angle = Math.toDegrees(Math.atan(lng / lateral));
		Double orientation = surveyData.getAngle();
		addRectangleOverlay(latLng, halfDiag, angle, orientation);
		
	}

	/**
	 * Adds Rectangle overlay over the map.
	 * @param center Latitude and longitude variable.
	 * @param halfDiagonal Half of the diagonal length of the overlay.
	 * @param centerAngle Angle in relation to the center of the overlay.
	 * @param orientation Orientation of the overlay.
	 */
	private void addRectangleOverlay(LatLng center, double halfDiagonal,
			double centerAngle, Double orientation) {
		cameraOverlays.add(mMap.addPolygon(new PolygonOptions()
		.add(GeoTools.newCoordFromBearingAndDistance(center,
				orientation - centerAngle, halfDiagonal),
				GeoTools.newCoordFromBearingAndDistance(center,
						orientation + centerAngle, halfDiagonal),
						GeoTools.newCoordFromBearingAndDistance(center,
								orientation + 180 - centerAngle, halfDiagonal),
								GeoTools.newCoordFromBearingAndDistance(center,
										orientation + 180 + centerAngle, halfDiagonal))
										.fillColor(Color.argb(40, 0, 0, 127)).strokeWidth(1)
										.strokeColor(Color.argb(127, 0, 0, 255))));
	}

}