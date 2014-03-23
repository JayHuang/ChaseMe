package org.chaseme.fragments.helpers;

import java.util.ArrayList;
import java.util.List;

import org.chaseme.helpers.geoTools.Simplify;

import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.chaseme.R;

/**
 * Fragment of Gesture Map.
 */
public class GestureMapFragment extends Fragment implements OnGestureListener {
	private static final int TOLERANCE = 15;
	private static final int STROKE_WIDTH = 3;

	private double toleranceInPixels;

	public interface OnPathFinishedListener {

		void onPathFinished(List<Point> path);
	}

	private GestureOverlayView overlay;
	private OnPathFinishedListener listener;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gesture_map, container,
				false);
		overlay = (GestureOverlayView) view.findViewById(R.id.overlay1);
		overlay.addOnGestureListener(this);
		overlay.setEnabled(false);

		overlay.setGestureStrokeWidth(scaleDpToPixels(STROKE_WIDTH));
		toleranceInPixels = scaleDpToPixels(TOLERANCE);
		return view;
	}

	/**
	 * Scale the Gesture Map to value
	 * @param value Value wanted to scale with.
	 * @return number of pixels for the map to be displayed in.
	 */
	private int scaleDpToPixels(double value) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) Math.round(value*scale);
	}

	/**
	 * Enable the gesture detection in overlay.
	 */
	public void enableGestureDetection() {
		overlay.setEnabled(true);
	}

	/**
	 * Disable the gesture detection in overlay.
	 */
	public void disableGestureDetection() {
		overlay.setEnabled(false);
	}

	/**
	 * Sets the OnPathFinishedListener.
	 * @param listener 
	 */
	public void setOnPathFinishedListener(OnPathFinishedListener listener) {
		this.listener = listener;
	}

	/* (non-Javadoc)
	 * @see android.gesture.GestureOverlayView.OnGestureListener#onGestureEnded(android.gesture.GestureOverlayView, android.view.MotionEvent)
	 */
	@Override
	public void onGestureEnded(GestureOverlayView arg0, MotionEvent arg1) {
		overlay.setEnabled(false);
		List<Point> path = decodeGesture();
		if (path.size() > 1) {
			path = Simplify.simplify(path, toleranceInPixels);
		}
		listener.onPathFinished(path);
	}

	/**
	 * Decode the Gesture
	 * @return path The list of path points.
	 */
	private List<Point> decodeGesture() {
		List<Point> path = new ArrayList<Point>();
		extractPathFromGesture(path);
		return path;
	}

	/**
	 * Extract the path from Gesture
	 * @param path List of points to extract for the Gesture
	 */
	private void extractPathFromGesture(List<Point> path) {
		float[] points = overlay.getGesture().getStrokes().get(0).points;
		for (int i = 0; i < points.length; i += 2) {
			path.add(new Point((int) points[i], (int) points[i + 1]));
		}
	}

	/* (non-Javadoc)
	 * @see android.gesture.GestureOverlayView.OnGestureListener#onGesture(android.gesture.GestureOverlayView, android.view.MotionEvent)
	 */
	@Override
	public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
	}

	/* (non-Javadoc)
	 * @see android.gesture.GestureOverlayView.OnGestureListener#onGestureCancelled(android.gesture.GestureOverlayView, android.view.MotionEvent)
	 */
	@Override
	public void onGestureCancelled(GestureOverlayView arg0, MotionEvent arg1) {
	}

	/* (non-Javadoc)
	 * @see android.gesture.GestureOverlayView.OnGestureListener#onGestureStarted(android.gesture.GestureOverlayView, android.view.MotionEvent)
	 */
	@Override
	public void onGestureStarted(GestureOverlayView arg0, MotionEvent arg1) {
	}


}
