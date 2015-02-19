package edu.rosehulman.moviematch;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MovieWishListActivity extends Activity implements
		OnItemClickListener {
	private ArrayAdapter<SimpleMovie> mAdapter;

	private ArrayList<SimpleMovie> mMovies;

	private MovieDataAdapter mMovieDataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_wish_list);

		mMovieDataAdapter = new MovieDataAdapter(this);
		mMovieDataAdapter.open();
		this.setTitle(getString(R.string.wishlist));
		mMovies = new ArrayList<SimpleMovie>();
		mMovieDataAdapter.readWishMovies(mMovies);

		ListView listView = (ListView) findViewById(R.id.wishlist_view);
		mAdapter = new ArrayAdapter<SimpleMovie>(this,
				android.R.layout.simple_list_item_1, mMovies);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMovies.clear();
		mMovieDataAdapter.readWishMovies(mMovies);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent intent = new Intent(this, MovieProfileActivity.class);

		Movie movie = new Movie(mMovies.get(position).getName(), mMovies.get(
				position).getId());
		new TMDBMovieRecommender().getMovieById(mMovies.get(position).getId(),
				movie);

		intent.putExtra(MovieProfileActivity.KEY_METACRITIC_RATING, (float) 0);
		intent.putExtra(MovieProfileActivity.KEY_USER_RATING, 0);

		intent.putExtra(MovieProfileActivity.KEY_MOVIE, movie);
		this.startActivity(intent);

	}
}