package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MovieListActivity extends Activity implements OnItemClickListener {
	private MovieRowAdapter mAdapter;
	
	private List<Movie> mMovies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_list);

		this.setTitle(getIntent().getStringExtra(MainActivity.KEY_TITLE));
		mMovies = getIntent().getExtras()
				.getParcelableArrayList(MainActivity.KEY_MOVIE_LIST);

		ArrayList<Movie> list = null;
		if (savedInstanceState != null) {
			list = savedInstanceState.getParcelableArrayList("list");
		}

		ListView listView = (ListView) findViewById(R.id.list_view);
		mAdapter = new MovieRowAdapter(this, list);
		addMovies(mMovies);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("list", mAdapter.getArrayList());
	}

	protected void addMovies(List<Movie> movies) {
		for (int i = 0; i < movies.size(); i++) {
			mAdapter.addView(movies.get(i));
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d("",
				"the class called in listview is "
						+ (mAdapter.getItem(position)).getClass().getName()
						+ "!");

		Intent intent = new Intent(this, MovieProfileActivity.class);
		
		intent.putExtra(MovieProfileActivity.KEY_METACRITIC_RATING, (float) 1.5);
		intent.putExtra(MovieProfileActivity.KEY_USER_RATING, 0);
		intent.putExtra(MovieProfileActivity.KEY_IS_ON_WISHLIST, false);
		
		intent.putExtra(MovieProfileActivity.KEY_MOVIE, mMovies.get(position));
		this.startActivity(intent);

	}
}
