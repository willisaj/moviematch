package edu.rosehulman.moviematch;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MovieListActivity extends Activity {

	// TODO launch movieprofileActivity
	private MovieRowAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_list);

		ArrayList<Movie> list = null;
		if (savedInstanceState != null) {
			list = savedInstanceState.getParcelableArrayList("list");
		}

		ListView listView = (ListView) findViewById(R.id.list_view);
		adapter = new MovieRowAdapter(this, list);

		listView.setAdapter(adapter);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("list", adapter.getArrayList());
	}

	protected void addMovies(ArrayList<Movie> movies) {
		for (int i = 0; i < movies.size(); i++) {
			adapter.addView(movies.get(i));
		}
	}
}
