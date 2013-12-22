package com.creakiwi.randroid;

import java.util.ArrayList;
import java.util.Random;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SuspenseRandroid extends Activity {
	private ArrayAdapter<Integer> adapter;
	private ArrayList<Integer> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suspense_randroid);

		this.list		= new ArrayList<Integer>();
		this.adapter	= new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, this.list);
		ListView lv 	= (ListView) this.findViewById(R.id.result);
		lv.setAdapter(this.adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.suspense_randroid, menu);
		return true;
	}

	/*
	 * Called when the button to start the pick is clicked
	 */
	public void sortOnClick(View v)
	{
		this.list						= new ArrayList<Integer>();
		ArrayList<Integer> temp_list 	= new ArrayList<Integer>();

		EditText n_picker	= (EditText) this.findViewById(R.id.n_picker);
		EditText max_picker = (EditText) this.findViewById(R.id.max_picker);
		
		int n 	= Integer.valueOf(n_picker.getText().toString());
		int max	= Integer.valueOf(max_picker.getText().toString());
		
		for (int i = 0 ; i < n ; i++)
		{
			int rand_number = this.pick(max);

			while (temp_list.contains(rand_number))
				rand_number = this.pick(max);

			temp_list.add(rand_number);
		}
		
		AsyncTask<ArrayList<Integer>, Integer, Void> task = new AsyncTask<ArrayList<Integer>, Integer, Void>() {
			@Override
			protected Void doInBackground(ArrayList<Integer>... params) {
				final ArrayList<Integer> list = params[0];

				for (int number: list) {
					try {
						Thread.sleep(1000);
						publishProgress(number);
					} catch (InterruptedException e) {
					}
				}

				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate();
				adapter.add(values[0]);
				adapter.notifyDataSetChanged();
			}
		};
		task.execute(temp_list);
	}
	
	protected int pick(int max)
	{
		Random r = new Random();
		int rand_number = r.nextInt(max) + 1;
		
		return rand_number;
	}
}
