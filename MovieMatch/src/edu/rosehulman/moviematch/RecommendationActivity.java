package edu.rosehulman.moviematch;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

public class RecommendationActivity extends Activity {

	// TODO launch movieprofileActivity
	private MovieRowAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommendation);

		ArrayList<Movie> list = null;
		if (savedInstanceState != null) {
			list = savedInstanceState.getParcelableArrayList("list");
		}

		AbsListView listView = (AbsListView) findViewById(R.id.list_view);
		adapter = new MovieRowAdapter(this, list);

		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recommendation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("list", adapter.getArrayList());
	}
}
