package org.chaseme.activities;

import java.util.List;

import org.chaseme.activities.helpers.OnEditorInteraction;
import org.chaseme.activities.helpers.SuperUI;
import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.drone.variables.mission.Mission;
import org.chaseme.drone.variables.mission.MissionItem;
import org.chaseme.fragments.EditorListFragment;
import org.chaseme.fragments.EditorMapFragment;
import org.chaseme.fragments.EditorToolsFragment;
import org.chaseme.fragments.EditorToolsFragment.EditorTools;
import org.chaseme.fragments.EditorToolsFragment.OnEditorToolSelected;
import org.chaseme.fragments.helpers.GestureMapFragment;
import org.chaseme.fragments.helpers.MapProjection;
import org.chaseme.fragments.helpers.GestureMapFragment.OnPathFinishedListener;
import org.chaseme.fragments.mission.MissionDetailFragment;
import org.chaseme.fragments.mission.MissionDetailFragment.OnWayPointTypeChangeListener;

import android.app.ActionBar;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.chaseme.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Editor UI Activity
 */
public class EditorActivity extends SuperUI implements OnPathFinishedListener,
		OnEditorToolSelected, OnWayPointTypeChangeListener,
		OnEditorInteraction, Callback {

	private EditorMapFragment planningMapFragment;
	private GestureMapFragment gestureMapFragment;
	private Mission mission;
	private EditorToolsFragment editorToolsFragment;
	private MissionDetailFragment itemDetailFragment;
	private FragmentManager fragmentManager;
	private EditorListFragment missionListFragment;
	private TextView infoView;

	private ActionMode contextualActionBar;

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperUI#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		fragmentManager = getSupportFragmentManager();

		planningMapFragment = ((EditorMapFragment) fragmentManager
				.findFragmentById(R.id.mapFragment));
		gestureMapFragment = ((GestureMapFragment) fragmentManager
				.findFragmentById(R.id.gestureMapFragment));
		editorToolsFragment = (EditorToolsFragment) fragmentManager
				.findFragmentById(R.id.editorToolsFragment);
		missionListFragment = (EditorListFragment) fragmentManager
				.findFragmentById(R.id.missionFragment1);
		infoView = (TextView) findViewById(R.id.editorInfoWindow);

		removeItemDetail(); // When doing things like screen rotation remove the
							// detail window

		mission = drone.mission;
		gestureMapFragment.setOnPathFinishedListener(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		updateMapPadding();
	}

	/**
	 * Updates padding for Map fragment on UI
	 */
	private void updateMapPadding() {
		int topPadding = infoView.getBottom();
		int rightPadding = 0,bottomPadding = 0;
		if (mission.getItems().size()>0) {
			rightPadding = editorToolsFragment.getView().getRight();
			bottomPadding = missionListFragment.getView().getHeight();
		}
		planningMapFragment.mMap.setPadding(rightPadding, topPadding, 0, bottomPadding);
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperUI#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperUI#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		mission.clearSelection();
		drone.events.notifyDroneEvent(DroneEventsType.MISSION_UPDATE);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		planningMapFragment.saveCameraPosition();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperUI#onDroneEvent(org.chaseme.drone.DroneInterfaces.DroneEventsType, org.chaseme.drone.Drone)
	 */
	@Override
	public void onDroneEvent(DroneEventsType event, Drone drone) {
		super.onDroneEvent(event,drone);
		switch (event) {
		case MISSION_UPDATE:
			// Remove detail window if item is removed
			if (itemDetailFragment != null) {
				if (!drone.mission.hasItem(itemDetailFragment.getItem())) {
					removeItemDetail();
				}
			}
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.SuperUI#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			planningMapFragment.saveCameraPosition();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.OnEditorInteraction#onMapClick(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMapClick(LatLng point) {
        //If an mission item is selected, unselect it.
        mission.clearSelection();
        removeItemDetail();
        notifySelectionChanged();

		switch (getTool()) {
		case MARKER:
			mission.addWaypoint(point);
			break;
		case DRAW:
			break;
		case POLY:
			break;
		case TRASH:
			break;
		case NONE:
			break;
		}
	}

	
	/**
	 * Get tool from editor tools fragment
	 * @return tool
	 */
	public EditorTools getTool() {
		return editorToolsFragment.getTool();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.EditorToolsFragment.OnEditorToolSelected#editorToolChanged(org.chaseme.fragments.EditorToolsFragment.EditorTools)
	 */
	@Override
	public void editorToolChanged(EditorTools tools) {
		removeItemDetail();
		mission.clearSelection();
		notifySelectionChanged();

		switch (tools) {
		case DRAW:
		case POLY:
			gestureMapFragment.enableGestureDetection();
			break;
		case MARKER:
		case TRASH:
		case NONE:
			gestureMapFragment.disableGestureDetection();
			break;
		}
	}
	
	
	/**
	 * Sets up item detail for the item
	 * @param item - MissionItem to show detail on
	 */
	private void showItemDetail(MissionItem item) {
		if (itemDetailFragment == null) {
			addItemDetail(item);
		} else {
			switchItemDetail(item);
		}
	}

	
	/**
	 * Add item detail for item
	 * @param item - MissionItem to add
	 */
	private void addItemDetail(MissionItem item) {
		itemDetailFragment = item.getDetailFragment();
		fragmentManager.beginTransaction()
				.add(R.id.containerItemDetail, itemDetailFragment).commit();
	}

	/**
	 * Switch the item detail for item
	 * @param item MissionItem
	 */
	private void switchItemDetail(MissionItem item) {
		itemDetailFragment = item.getDetailFragment();
		fragmentManager.beginTransaction()
				.replace(R.id.containerItemDetail, itemDetailFragment).commit();
	}

	/**
	 * Remove item detail
	 */
	private void removeItemDetail() {
		if (itemDetailFragment != null) {
			fragmentManager.beginTransaction().remove(itemDetailFragment)
					.commit();
			itemDetailFragment = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.helpers.GestureMapFragment.OnPathFinishedListener#onPathFinished(java.util.List)
	 */
	@Override
	public void onPathFinished(List<Point> path) {
		List<LatLng> points = MapProjection.projectPathIntoMap(path,
				planningMapFragment.mMap);
		switch (getTool()) {
		case DRAW:
			drone.mission.addWaypoints(points);
			break;
		case POLY:
			drone.mission.addSurveyPolygon(points);
			break;
		default:
			break;
		}
		editorToolsFragment.setTool(EditorTools.NONE);
	}

	/* (non-Javadoc)
	 * @see org.chaseme.fragments.mission.MissionDetailFragment.OnWayPointTypeChangeListener#onWaypointTypeChanged(org.chaseme.drone.variables.mission.MissionItem, org.chaseme.drone.variables.mission.MissionItem)
	 */
	@Override
	public void onWaypointTypeChanged(MissionItem newItem, MissionItem oldItem) {
		mission.replace(oldItem, newItem);
		showItemDetail(newItem);
	}

	private static final int MENU_DELETE = 1;
	private static final int MENU_REVERSE = 2;

	/* (non-Javadoc)
	 * @see android.view.ActionMode.Callback#onActionItemClicked(android.view.ActionMode, android.view.MenuItem)
	 */
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch(item.getItemId()){
		case MENU_DELETE:
			mission.removeWaypoints(mission.getSelected());
			notifySelectionChanged();
			mode.finish();
			return true;
		case MENU_REVERSE:
			mission.reverse();
			notifySelectionChanged();
			return true;
		default:
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see android.view.ActionMode.Callback#onCreateActionMode(android.view.ActionMode, android.view.Menu)
	 */
	@Override
	public boolean onCreateActionMode(ActionMode arg0, Menu menu) {
		menu.add(0, MENU_DELETE, 0, "Delete");
		menu.add(0, MENU_REVERSE, 0, "Reverse");
		editorToolsFragment.getView().setVisibility(View.INVISIBLE);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.view.ActionMode.Callback#onDestroyActionMode(android.view.ActionMode)
	 */
	@Override
	public void onDestroyActionMode(ActionMode arg0) {
		missionListFragment.updateChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mission.clearSelection();
		notifySelectionChanged();
		contextualActionBar = null;
		editorToolsFragment.getView().setVisibility(View.VISIBLE);
	}

	/* (non-Javadoc)
	 * @see android.view.ActionMode.Callback#onPrepareActionMode(android.view.ActionMode, android.view.Menu)
	 */
	@Override
	public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.OnEditorInteraction#onItemLongClick(org.chaseme.drone.variables.mission.MissionItem)
	 */
	@Override
	public boolean onItemLongClick(MissionItem item) {
		if (contextualActionBar != null) {
			if (mission.selectionContains(item)) {
				mission.clearSelection();
			} else {
				mission.clearSelection();
				mission.addToSelection(mission.getItems());
			}
			notifySelectionChanged();
		} else {
			removeItemDetail();
			missionListFragment.updateChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			contextualActionBar = startActionMode(this);
			editorToolsFragment.setTool(EditorTools.NONE);
			mission.clearSelection();
			mission.addToSelection(item);
			notifySelectionChanged();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.OnEditorInteraction#onItemClick(org.chaseme.drone.variables.mission.MissionItem)
	 */
	@Override
	public void onItemClick(MissionItem item) {
		switch (editorToolsFragment.getTool()) {
		default:
			if (contextualActionBar != null) {
				if (mission.selectionContains(item)) {
					mission.removeItemFromSelection(item);
				} else {
					mission.addToSelection(item);
				}
			} else {
				if (mission.selectionContains(item)) {
					mission.clearSelection();
					removeItemDetail();
				} else {
					mission.setSelectionTo(item);
					showItemDetail(item);
				}
			}
			break;
		case TRASH:
			mission.removeWaypoint(item);
			mission.clearSelection();
			if (mission.getItems().size() <= 0) {
				editorToolsFragment.setTool(EditorTools.NONE);
			}
			break;
		}
		notifySelectionChanged();
	}

	
	/**
	 * Update based on change in selection
	 */
	private void notifySelectionChanged() {
        List<MissionItem> selectedItems = mission.getSelected();
        missionListFragment.updateMissionItemSelection(selectedItems);

		if (selectedItems.size() == 0) {
			missionListFragment.setArrowsVisibility(false);
		} else {
			missionListFragment.setArrowsVisibility(true);
		}
		planningMapFragment.update();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.OnEditorInteraction#onListVisibilityChanged()
	 */
	@Override
	public void onListVisibilityChanged() {
		updateMapPadding();
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.HelpActivity#getHelpItems()
	 */
	@Override
	public CharSequence[][] getHelpItems() {
		return new CharSequence[][] { {}, {} };
	}
}
