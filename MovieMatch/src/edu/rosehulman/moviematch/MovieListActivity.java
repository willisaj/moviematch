package edu.rosehulman.moviematch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MovieListActivity extends Activity implements OnItemClickListener {
	private MovieRowAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_list);

		this.setTitle(getIntent().getStringExtra(MainActivity.KEY_TITLE));
		ArrayList<Movie> movies = getIntent().getExtras()
				.getParcelableArrayList(MainActivity.KEY_MOVIE_LIST);

		ArrayList<Movie> list = null;
		if (savedInstanceState != null) {
			list = savedInstanceState.getParcelableArrayList("list");
		}

		ListView listView = (ListView) findViewById(R.id.list_view);
		adapter = new MovieRowAdapter(this, list);
		addMovies(movies);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("list", adapter.getArrayList());
	}

	protected void addMovies(ArrayList<Movie> movies) {
		for (int i = 0; i < movies.size(); i++) {
			adapter.addView(movies.get(i));
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d("",
				"the class called in listview is "
						+ (adapter.getItem(position)).getClass().getName()
						+ "!");

		Intent intent = new Intent(this, MovieProfileActivity.class);
		intent.putExtra(MovieProfileActivity.KEY_MOVIE_TITLE, "Citizen Kane");
		intent.putExtra(
				MovieProfileActivity.KEY_DESCRIPTION,
				"this movie is just the worst.  I don't know how people stay awake.  It's about this dude who gets rich and dies and things happen.");
		intent.putExtra(MovieProfileActivity.KEY_METACRITIC_RATING, (float) 1.5);
		intent.putExtra(MovieProfileActivity.KEY_MPAA_RATING, "PG-Terrible");
		intent.putExtra(MovieProfileActivity.KEY_USER_RATING, 0);
		intent.putExtra(MovieProfileActivity.KEY_ROTTEN_RATING, (float) 1.2);
		intent.putExtra(MovieProfileActivity.KEY_DURATION, "A really long time");
		intent.putExtra(MovieProfileActivity.KEY_IS_ON_WISHLIST, false);
		this.startActivity(intent);

	}
}
