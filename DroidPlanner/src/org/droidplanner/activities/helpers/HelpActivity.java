package org.droidplanner.activities.helpers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import org.droidplanner.R;

public abstract class HelpActivity extends FragmentActivity implements OnClickListener {

	public HelpActivity() {
		super();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            default:
                return false;
        }
	}

	/**
	 * Get help items to be populated
	 *
	 * @return A matrix with pars of help guides names, with the associated
	 *         video url
	 */
	public abstract CharSequence[][] getHelpItems();

	@Override
	public void onClick(DialogInterface dialog, int which) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getHelpItems()[1][which].toString())));
	}

}
