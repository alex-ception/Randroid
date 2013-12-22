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

	/*
	 * Launches the service, using parameters given by the activity (or defaults, if not found)
	 * Launches a new thread, where a loop is launched, and sleeped every *delay* seconds to simulate multiple lottery picks
	 */
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
	
	/*
	 * Stops the service
	 * When the service is stopped, the loop is stopped too
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.run = false;
	}

	/*
	 * Pick a number between 1 and *max*
	 */
	protected int pick(int max)
	{
		Random r = new Random();
		int rand_number = r.nextInt(max) + 1;
		
		return rand_number;
	}

	/*
	 * Create an AsyncTask to retrieve results, and broadcast them once they are computed
	 */
	protected void inLoop() {
		AsyncTask<Void, Void, ArrayList<Integer>> task = new AsyncTask<Void, Void, ArrayList<Integer>>() {
			/*
			 * Computing the results of the lottery pick
			 * Knowing that results have to be unique
			 */
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

			/*
			 * The results of this lottery pick are computed and are broadcasted
			 */
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
