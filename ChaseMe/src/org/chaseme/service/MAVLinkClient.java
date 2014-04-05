package org.chaseme.service;

import java.util.Timer;
import java.util.TimerTask;

import org.chaseme.drone.Drone;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.MAVLink.Messages.ApmModes;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPacket;

// provide a common class for some ease of use functionality
public class MAVLinkClient {
	public static final int MSG_RECEIVED_DATA = 0;
	public static final int MSG_SELF_DESTRY_SERVICE = 1;
	public static final int MSG_TIMEOUT = 2;

	Context parent;
	private Drone drone;
	private OnMavlinkClientListener listener;
	Messenger mService = null;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	private boolean mIsBound;
	private Timer timeOutTimer;
	private int timeOutCount;
	private long timeOut;
	private int timeOutRetry;

	public interface OnMavlinkClientListener {
		public void notifyConnected();

		public void notifyDisconnected();

		public void notifyReceivedData(MAVLinkMessage m);

		void notifyTimeOut(int timeOutCount);
	}

	/**
	 * Setup the parent and listeners
	 * @param context
	 * @param listener
	 */
	public MAVLinkClient(Context context, OnMavlinkClientListener listener) {
		parent = context;
		this.listener = listener;
	}

	/**
	 * Initialize and bind the mavlink connection
	 */
	public void init() {
		parent.bindService(new Intent(parent, MAVLinkService.class),
				mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	/**
	 * Close connection and unbind the service
	 */
	public void close() {
		if (isConnected()) {
			// If we have received the service, and hence registered with
			// it, then now is the time to unregister.
			if (mService != null) {
				try {
					Message msg = Message.obtain(null,
							MAVLinkService.MSG_UNREGISTER_CLIENT);
					msg.replyTo = mMessenger;
					mService.send(msg);

				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				// Unbinding the service.
				parent.unbindService(mConnection);
				onDisconnectService();
			}
		}
	}

	/**
	 * Set timeout value
	 * @param timeout_ms
	 */
	public void setTimeOutValue(long timeout_ms) {
		this.timeOut = timeout_ms;
	}

	/**
	 * Get timeout value
	 * @return
	 */
	public long getTimeOutValue() {
		if (this.timeOut <= 0)
			return 3000; // default value

		return this.timeOut;
	}

	/**
	 * Set number of timeout retries
	 * @param timeout_retry
	 */
	public void setTimeOutRetry(int timeout_retry) {
		this.timeOutRetry = timeout_retry;
	}

	/**
	 * Get number of retries for timeout
	 * @return timeOutRetry
	 */
	public int getTimeOutRetry() {
		if (this.timeOutRetry <= 0)
			return 3; // default value

		return this.timeOutRetry;
	}

	public synchronized void resetTimeOut() {
		if (timeOutTimer != null) {
			timeOutTimer.cancel();
			timeOutTimer = null;
			/*
			 * Log.d("TIMEOUT", "reset " + String.valueOf(timeOutTimer));
			 */
		}
	}

	/**
	 * Set timeout
	 */
	public void setTimeOut() {
		setTimeOut(this.timeOut, true);
	}

	/**
	 * Set timeout to resetTimeOutCount
	 * @param resetTimeOutCount
	 */
	public void setTimeOut(boolean resetTimeOutCount) {
		setTimeOut(this.timeOut, resetTimeOutCount);
	}

	public synchronized void setTimeOut(long timeout_ms,
			boolean resetTimeOutCount) {
		/*
		 * Log.d("TIMEOUT", "set " + String.valueOf(timeout_ms));
		 */
		resetTimeOut();
		if (resetTimeOutCount)
			timeOutCount = 0;

		if (timeOutTimer == null) {
			timeOutTimer = new Timer();
			timeOutTimer.schedule(new TimerTask() {
				public void run() {
					if (timeOutTimer != null) {
						resetTimeOut();
						timeOutCount++;

						/*
						 * Log.d("TIMEOUT", "timed out");
						 */

						listener.notifyTimeOut(timeOutCount);
					}
				}
			}, timeout_ms); // delay in milliseconds
		}
	}

	/**
	 * Handler of incoming messages from service.
	 */
	@SuppressLint("HandlerLeak")
	// TODO fix this error message
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// Received data from... somewhere
			case MSG_RECEIVED_DATA:
				Bundle b = msg.getData();
				MAVLinkMessage m = (MAVLinkMessage) b.getSerializable("msg");
				listener.notifyReceivedData(m);
				break;
			case MSG_SELF_DESTRY_SERVICE:
				close();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			try {
				Message msg = Message.obtain(null,
						MAVLinkService.MSG_REGISTER_CLIENT);
				msg.replyTo = mMessenger;
				mService.send(msg);
				onConnectedService();
			} catch (RemoteException e) {
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			onDisconnectService();
		}
	};

	/**
	 * Send mavlink packets
	 * @param pack
	 */
	public void sendMavPacket(MAVLinkPacket pack) {
		Message msg = Message.obtain(null, MAVLinkService.MSG_SEND_DATA);
		Bundle data = new Bundle();
		data.putSerializable("msg", pack);
		msg.setData(data);
		try {
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Notify listeners when mavlink is connected
	 */
	private void onConnectedService() {
		listener.notifyConnected();
	}

	/**
	 * Notify listeners when mavlink is disconnected
	 */
	private void onDisconnectService() {
		mIsBound = false;
		listener.notifyDisconnected();
	}

	/**
	 * Notify listener if mavlink is connected/disconnected
	 */
	public void queryConnectionState() {
		if (mIsBound) {
			listener.notifyConnected();
		} else {
			listener.notifyDisconnected();
		}

	}

	/**
	 * Returns if mavlink is connected
	 * @return mIsBound
	 */
	public boolean isConnected() {
		return mIsBound;
	}
	
	/**
	 * Toggle connection state to properly disarm and land drone 
	 */
	public void toggleConnectionState() {
		if (isConnected()) {
			// if(drone.state.getMode().getName() != "ROTOR_CHASEME") {
			// 	drone.state.changeFlightMode(ApmModes.ROTOR_LOITER);
			// 	drone.land(drone.getFollowMe().getLocation().getLatitude(), drone.getFollowMe().getLocation().getLongitude());
				Toast.makeText(parent, "Disconnecting", 1).show();
				
				Toast.makeText(parent, "Chase Me disengaged", 1).show();
				Toast.makeText(parent, "Land initialized", 1).show();
			//}
			close();
		} else {
			Toast.makeText(parent, "Connecting", 1).show();
			init();
		}
	}
}
