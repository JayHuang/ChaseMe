package org.chaseme.activities;

import org.chaseme.R;
import org.chaseme.activities.helpers.SuperUI;
import org.chaseme.drone.Drone;
import org.chaseme.drone.DroneInterfaces.DroneEventsType;
import org.chaseme.fragments.ChecklistFragment;
import org.chaseme.fragments.ParamsFragment;
import org.chaseme.fragments.SetupRadioFragment;
import org.chaseme.fragments.SetupSensorFragment;
import org.chaseme.fragments.TuningFragment;
import org.chaseme.widgets.viewPager.TabPageIndicator;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

/**
 * Configuration activity
 */
public class ConfigurationActivity extends SuperUI {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);

		final ConfigurationPagerAdapter pagerAdapter = new ConfigurationPagerAdapter(
				getApplicationContext(), getSupportFragmentManager());

		final ViewPager viewPager = (ViewPager) findViewById(R.id.configuration_pager);
		viewPager.setAdapter(pagerAdapter);

		final TabPageIndicator tabIndicator = (TabPageIndicator) findViewById(R.id.configuration_tab_strip);
		tabIndicator.setViewPager(viewPager);

		final ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onDroneEvent(DroneEventsType event, Drone drone) {
		super.onDroneEvent(event, drone);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This is the fragment pager adapter to handle the tabs of the
	 * Configuration activity.
	 *
	 * @since 1.2.0
	 */
	private static class ConfigurationPagerAdapter extends FragmentPagerAdapter {

		/**
		 * Application context object used to retrieve the tabs' title.
		 *
		 * @since 1.2.0
		 */
		private final Context mContext;
		
		
		public ConfigurationPagerAdapter(Context context, FragmentManager fm) {
			super(fm);
			mContext = context;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new TuningFragment();
			case 1:
				return new SetupRadioFragment();
			case 2:
				return new SetupSensorFragment();
			case 3:
				return new ChecklistFragment();
			case 4:
				return new ParamsFragment();
			default:
				return null;
			}
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return 5;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return mContext.getString(R.string.screen_tuning);
			case 1:
				return mContext.getText(R.string.screen_rc);
			case 2:
				return mContext.getString(R.string.screen_cal);
			case 3:
				return mContext.getString(R.string.screen_checklist);
			case 4:
				return mContext.getText(R.string.screen_parameters);
			default:
				return null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.chaseme.activities.helpers.HelpActivity#getHelpItems()
	 */
	@Override
	public CharSequence[][] getHelpItems() {
		return new CharSequence[][] { {}, {} };
	}
}
