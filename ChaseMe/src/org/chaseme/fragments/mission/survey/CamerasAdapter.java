package org.chaseme.fragments.mission.survey;

import org.chaseme.R;
import org.chaseme.file.IO.CameraInfo;
import org.chaseme.file.IO.CameraInfoReader;
import org.chaseme.file.help.CameraInfoLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CamerasAdapter extends ArrayAdapter<String> {

	private CameraInfoLoader loader;
	private Context context;
	private String title = "";

	public CamerasAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
		loader = new CameraInfoLoader(context);
		addAll(loader.getCameraInfoList());
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) super.getView(position, convertView, parent);
		view.setText(title);
		return view;
	}

	public CameraInfo getCamera(int position) {
		try {
			return loader.openFile(getItem(position));
		} catch (Exception e) {
			Toast.makeText(context,
					context.getString(R.string.error_when_opening_file),
					Toast.LENGTH_SHORT).show();
			return loadLastFile(); 
		}
	}

	private CameraInfo loadLastFile() {
		try {
			return loader.openFile(getItem(this.getCount()-1));
		} catch (Exception e) {
			return CameraInfoReader.getNewMockCameraInfo();
		}
	}
}
