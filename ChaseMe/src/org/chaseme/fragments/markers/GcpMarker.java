package org.chaseme.fragments.markers;

import org.chaseme.gcp.Gcp;

import org.chaseme.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * GcpMarker class.
 */
public class GcpMarker {

	/**
	 * Marker Options.
	 * @param gcp Gcp variable.
	 * @return Marker Options.
	 */
	public static MarkerOptions build(Gcp gcp) {
		return new MarkerOptions().position(gcp.coord).title(String.valueOf(0))
				.icon(getIcon(gcp)).anchor((float) 0.5, (float) 0.5);
	}

	/**
	 * Update Marker position, title, icon.
	 * @param marker Marker variable.
	 * @param gcp Gcp variable.
	 */
	public static void update(Marker marker, Gcp gcp) {
		marker.setPosition(gcp.coord);
		marker.setTitle(String.valueOf(0));
		marker.setIcon(getIcon(gcp));
	}

	/**
	 * Bitmap Descriptor.
	 * @param gcp Gcp variable.
	 * @return BitmapDescriptorFactory
	 */
	private static BitmapDescriptor getIcon(Gcp gcp) {
		if (gcp.isMarked) {
			return BitmapDescriptorFactory
					.fromResource(R.drawable.placemark_circle_red);
		} else {
			return BitmapDescriptorFactory
					.fromResource(R.drawable.placemark_circle_blue);
		}
	}
}