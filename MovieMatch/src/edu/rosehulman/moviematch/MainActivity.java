package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	public static final String KEY_TITLE = "KEY_TITLE";
	public static final String KEY_MOVIE_LIST = null;

	private String distribution = "ERROR";

	private EditText mActorEditText;
	private EditText mDirectorEditText;
	private Spinner mGenreSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Actor Edit Text
		mActorEditText = (EditText) findViewById(R.id.actor_editText);

		// Director Edit Text
		mDirectorEditText = (EditText) findViewById(R.id.director_editText);

		// Genre Spinner
		mGenreSpinner = (Spinner) findViewById(R.id.genre_spinner);
		List<Genre> genres = new TMDBMovieRecommender().getGenres();
		genres.add(0, new Genre(getString(R.string.genre_hint), 0));
		ArrayAdapter<Genre> genreAdapter = new ArrayAdapter<Genre>(this,
				android.R.layout.simple_spinner_item, genres);
		genreAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mGenreSpinner.setAdapter(genreAdapter);

		Spinner distributionSpinner = (Spinner) findViewById(R.id.distribution_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> distAdapter = ArrayAdapter
				.createFromResource(this, R.array.distributions,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		distAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		distributionSpinner.setAdapter(distAdapter);

		distributionSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						MainActivity.this.distribution = ((TextView) view)
								.getText().toString();
						Log.d("", "You chose " + MainActivity.this.distribution
								+ " as your distribution");

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		Button wishListButton = (Button) findViewById(R.id.wishlist_button);
		Button recommendButton = (Button) findViewById(R.id.recommend_movie_button);
		wishListButton.setOnClickListener(this);
		recommendButton.setOnClickListener(this);
	}

	protected void startRecommendationActivity() {
		Intent intent = new Intent(this, MovieListActivity.class);
		String actor = mActorEditText.getText().toString();
		String director = mDirectorEditText.getText().toString();
		int genreId = ((Genre) mGenreSpinner.getSelectedItem()).getId();

		IMovieRecommendation recommendation = new TMDBMovieRecommender()
				.getRecommendation();
		if (!actor.equals("")) {
			recommendation.withActor(actor);
		}
		if (!director.equals("")) {
			recommendation.withDirector(director);
		}
		if (genreId != 0) {
			recommendation.withGenre(genreId);
		}

		List<Movie> movies = recommendation.getMovies();

		intent.putExtra(KEY_TITLE, "Recommendations");
		intent.putExtra(KEY_MOVIE_LIST, (ArrayList<Movie>) movies);

		this.startActivity(intent);
	}

	protected void startMovieListActivity(boolean isRecommendation) {
		Intent intent = new Intent(this, MovieListActivity.class);
		// TODO make a central state that initializes other classes?
		// TODO database.getArrayOfmovies();

		ArrayList<String> actorsOne = new ArrayList<String>();
		actorsOne.add("Jimmy Kimmel");
		actorsOne.add("That one guy from American Grafitti");
		actorsOne.add("Carrie Grant");
		ArrayList<String> actorsTwo = new ArrayList<String>();
		actorsTwo.add("Daniel Radcliffe");
		actorsTwo.add("Emma Stone");
		actorsTwo.add("Rupert Grint");

		ArrayList<String> genresOne = new ArrayList<String>();
		genresOne.add("Action");
		genresOne.add("Amazing");
		genresOne.add("Sci-Fi");
		ArrayList<String> genresTwo = new ArrayList<String>();
		genresTwo.add("Romance");
		genresTwo.add("Zombie Comedy");
		genresTwo.add("Animated");

		ArrayList<Movie> moviesOne = new ArrayList<Movie>();
		moviesOne.add(new Movie("A New Hope", "George Lucas", actorsOne));
		moviesOne.add(new Movie("The Empire Strikes Back", "George Lucas",
				actorsOne));
		moviesOne
				.add(new Movie("Return of the Jedi", "George Lucas", actorsOne));

		ArrayList<Movie> moviesTwo = new ArrayList<Movie>();
		moviesTwo.add(new Movie("The Sorcerer's Stone", "Stephen Spielberg",
				actorsTwo));
		moviesTwo.add(new Movie("The Chamber of Secrets", "Oprah Winfrey",
				actorsTwo));
		moviesTwo.add(new Movie("The Prisoner of Azkaban", "Barbra Streissand",
				actorsTwo));

		if (isRecommendation) {
			intent.putExtra(KEY_TITLE, "Recommendations");
			intent.putExtra(KEY_MOVIE_LIST, moviesOne);
		} else {
			intent.putExtra(KEY_TITLE, "Wish List");
			intent.putExtra(KEY_MOVIE_LIST, moviesTwo);
		}

		this.startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.recommend_movie_button) {
			startRecommendationActivity();
		} else if (v.getId() == R.id.wishlist_button) {
			startMovieListActivity(false);
		} else {
			Log.d("", "CRITICAL ERROR IN MAINACTIVITY: onClick()");
		}

	}
}
