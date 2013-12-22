package com.creakiwi.randroid;

import java.util.ArrayList;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class RandroidService extends Service {
	public static final String NOTIFICATION = "com.creakiwi.randroid";
	public static final String PICKS		= "picks";
	public static final String MAX			= "max";
	public static final String DELAY		= "delay";
	public static final String RESULT		= "result";

	private boolean run;
	private int n;
	private int max;
	private int delay;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!intent.hasExtra(PICKS)
			|| !intent.hasExtra(MAX)
			|| !intent.hasExtra(DELAY))
			this.stopSelf();
		
		this.n 		= intent.getIntExtra(PICKS, 1);
		this.max	= intent.getIntExtra(MAX, 1);
		this.delay	= intent.getIntExtra(DELAY, 10);
		this.run	= true;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (run) {
					try {
						Thread.sleep(delay * 1000);
						inLoop();
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();

		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.run = false;
	}
	
	protected int pick(int max)
	{
		Random r = new Random();
		int rand_number = r.nextInt(max) + 1;
		
		return rand_number;
	}
	
	protected void inLoop() {
		AsyncTask<Void, Void, ArrayList<Integer>> task = new AsyncTask<Void, Void, ArrayList<Integer>>() {
			@Override
			protected ArrayList<Integer> doInBackground(Void... params) {
				ArrayList<Integer> results = new ArrayList<Integer>();

				for (int i = 0 ; i < n ; i++)
				{
					int rand_number = pick(max);

					while (results.contains(rand_number))
						rand_number = pick(max);

					results.add(rand_number);
				}

				return results;
			}

			@Override
			protected void onPostExecute(ArrayList<Integer> result) {
				super.onPostExecute(result);
				Intent i = new Intent(NOTIFICATION);
				i.putExtra(RESULT, result);
				sendBroadcast(i);
			}
		};
		task.execute();
	}
}
