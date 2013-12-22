package com.creakiwi.randroid;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SimpleRandroid extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_randroid);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.simple_randroid, menu);
		return true;
	}

	public void sortOnClick(View v)
	{
		EditText max_picker = (EditText) this.findViewById(R.id.max_picker);
		
		int max = Integer.valueOf(max_picker.getText().toString());
		
		Random r = new Random();
		int rand_number = r.nextInt(max) + 1;
		
		TextView result = (TextView) this.findViewById(R.id.result);
		result.setText("" + rand_number);
	}
}
