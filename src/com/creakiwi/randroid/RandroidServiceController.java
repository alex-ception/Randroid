package com.creakiwi.randroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RandroidServiceController extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_randroid_service_controller);
		
		Switch service_switcher = (Switch) this.findViewById(R.id.status);

		/*
		 *  The switch in the activity is used to start or stop the service
		 *  Due to threads, there is a delay between setting the switch to off, and the real stop of the service
		 */
		service_switcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					start();
				else
					stop();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.randroid_service_controller, menu);
		return true;
	}

	/*
	 * Called when the switch is ON
	 * Tries to start the service
	 * Application is killed when n, max, or delay aren't filled
	 * (I have to handle this bug)
	 */
	protected void start()
	{
		int n 		= Integer.valueOf(((EditText) this.findViewById(R.id.n_picker)).getText().toString());
		int max 	= Integer.valueOf(((EditText) this.findViewById(R.id.max_picker)).getText().toString());
		int delay 	= Integer.valueOf(((EditText) this.findViewById(R.id.delay_picker)).getText().toString());
		
		Intent randroid_service = new Intent(this, RandroidService.class);
		randroid_service.putExtra(RandroidService.PICKS, n);
		randroid_service.putExtra(RandroidService.MAX, max);
		randroid_service.putExtra(RandroidService.DELAY, delay);
		this.startService(randroid_service);
	}
	
	/*
	 * Called when the switch is OFF
	 * Stops the service
	 */
	protected void stop()
	{
		stopService(new Intent(this, RandroidService.class));
	}
}
