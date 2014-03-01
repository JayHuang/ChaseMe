package org.chaseme.polygon;

import java.util.ArrayList;
import java.util.List;

import org.chaseme.fragments.helpers.MapPath.PathSource;
import org.chaseme.helpers.geoTools.GeoTools;
import org.chaseme.helpers.geoTools.LineLatLng;
import org.chaseme.helpers.units.Area;

import android.content.Context;
import org.chaseme.R;
import com.google.android.gms.maps.model.LatLng;

public class Polygon implements PathSource {

	private List<PolygonPoint> points = new ArrayList<PolygonPoint>();

	public void addPoints(List<LatLng> pointList) {
		for (LatLng point : pointList) {
			addPoint(point);
		}
	}

	public void addPoint(LatLng coord) {
		points.add(new PolygonPoint(coord));
	}

	public void clearPolygon() {
		points.clear();
	}

	public List<LatLng> getLatLngList() {
		List<LatLng> list = new ArrayList<LatLng>();
		for (PolygonPoint point : points) {
			list.add(point.coord);
		}
		return list;
	}

	public List<LineLatLng> getLines() {
		List<LineLatLng> list = new ArrayList<LineLatLng>();
		for (int i = 0; i < points.size(); i++) {
			int endIndex = (i==0)? points.size()-1: i-1;
			list.add(new LineLatLng(points.get(i).coord,points.get(endIndex).coord));
		}
		return list;
	}

	public List<PolygonPoint> getPolygonPoints() {
		return points;
	}

	public void movePoint(LatLng coord, int number) {
		points.get(number).coord = coord;

	}

	public Area getArea() {
		return GeoTools.getArea(this);
	}

	@Override
	public List<LatLng> getPathPoints() {
		List<LatLng> path = getLatLngList();
		if (getLatLngList().size() > 2) {
			path.add(path.get(0));
		}
		return path;
	}

	public void checkIfValid(Context currContext) throws Exception {
		switch (points.size()) {
		case 0:
			throw new Exception(currContext.getString(R.string.exception_draw_polygon));
		case 1:
			throw new Exception(currContext.getString(R.string.exception_draw_2_more_polygon_points));
		case 2:
			throw new Exception(currContext.getString(R.string.exception_draw_1_more_polygon_points));
		default:
			return;
		}
		
	}

}
