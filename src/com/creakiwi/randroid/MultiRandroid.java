package com.creakiwi.randroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MultiRandroid extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_randroid);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.multi_randroid, menu);
		return true;
	}

	public void sortOnClick(View v)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();

		EditText n_picker	= (EditText) this.findViewById(R.id.n_picker);
		EditText max_picker = (EditText) this.findViewById(R.id.max_picker);
		
		int n 	= Integer.valueOf(n_picker.getText().toString());
		int max	= Integer.valueOf(max_picker.getText().toString());
		
		for (int i = 0 ; i < n ; i++)
		{
			int rand_number = this.pick(max);

			while (list.contains(rand_number))
				rand_number = this.pick(max);

			list.add(rand_number);
		}
		
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
	
		for (int number: list)
			sb.append("" + number + " ");
		
		TextView result = (TextView) this.findViewById(R.id.result);
		result.setText(sb.toString());
	}
	
	protected int pick(int max)
	{
		Random r = new Random();
		int rand_number = r.nextInt(max) + 1;

		return rand_number;
	}
}
