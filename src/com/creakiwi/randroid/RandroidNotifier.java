package com.creakiwi.randroid;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class RandroidNotifier extends Service {
	private Receiver receiver;
	private static final int NOTIFIER_ID = 24011990;
	
	/*
	 * Starts the service, and the broadcast receiver
	 * It uses a filter to get only what we need (fiy : RandroidService.NOTIFICATION)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.receiver = new Receiver();
		this.registerReceiver(this.receiver,
				new IntentFilter(RandroidService.NOTIFICATION));
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * This is the main internal class of the service
	 * It retrieves the broadcasted data from RandroidService
	 * And it adds notification to the notification tray
	 */
	public class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String date	= arg1.getExtras().getString(RandroidService.DATE);
			ArrayList<Integer> result = (ArrayList<Integer>) arg1.getExtras().get(RandroidService.RESULT);
			StringBuilder sb = new StringBuilder();
			
			for (int n: result)
				sb.append("" + n + " ");
			
			NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
			builder
				.setSmallIcon(android.R.drawable.ic_popup_reminder)
				.setContentTitle("Randroid")
				.setContentText(date + " : " + sb.toString())
				.setAutoCancel(true)
			;
			
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(NOTIFIER_ID, builder.build());
		}
		
	}
}