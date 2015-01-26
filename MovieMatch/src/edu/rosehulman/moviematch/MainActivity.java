package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
	// TODO consider using a global enum for genres?
	private String genre = "ERROR";
	private String distribution = "ERROR";
	private String actor = "ERROR";
	private String director = "ERROR";
	
	private EditText mActorEditText;
	private EditText mDirectorEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Spinner genreSpinner = (Spinner) findViewById(R.id.genre_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter
				.createFromResource(this, R.array.genres,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		genreAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		genreSpinner.setAdapter(genreAdapter);

		mActorEditText = (EditText) findViewById(R.id.actor_editText);
		mActorEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// do nothing

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// do nothing

			}

			@Override
			public void afterTextChanged(Editable s) {
				MainActivity.this.actor = s.toString();
				Log.d("", "You chose " + MainActivity.this.actor
						+ " as your actor");

			}
		});

		mDirectorEditText = (EditText) findViewById(R.id.director_editText);
		mDirectorEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// do nothing

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// do nothing

			}

			@Override
			public void afterTextChanged(Editable s) {
				MainActivity.this.director = s.toString();
				Log.d("", "You chose " + MainActivity.this.director
						+ " as your director");

			}
		});

		genreSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				MainActivity.this.genre = ((TextView) view).getText()
						.toString();
				Log.d("", "You chose " + MainActivity.this.genre
						+ " as your genre");

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

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
		
		IMovieRecommendation recommendation = new TMDBMovieRecommender().getRecommendation();
		if (!actor.equals("")) {
			recommendation.withActor(actor);
		}
		if (!director.equals("")) {
			recommendation.withDirector(director);
		}
		
		List<Movie> movies = recommendation.getMovies();
		
		intent.putExtra(KEY_TITLE, "Recommendations");
		intent.putExtra(KEY_MOVIE_LIST, (ArrayList<Movie>)movies);
		
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
		moviesOne.add(new Movie("A New Hope", "George Lucas", actorsOne,
				genresOne));
		moviesOne.add(new Movie("The Empire Strikes Back", "George Lucas",
				actorsOne, genresOne));
		moviesOne.add(new Movie("Return of the Jedi", "George Lucas",
				actorsOne, genresOne));

		ArrayList<Movie> moviesTwo = new ArrayList<Movie>();
		moviesTwo.add(new Movie("The Sorcerer's Stone", "Stephen Spielberg",
				actorsTwo, genresTwo));
		moviesTwo.add(new Movie("The Chamber of Secrets", "Oprah Winfrey",
				actorsTwo, genresTwo));
		moviesTwo.add(new Movie("The Prisoner of Azkaban", "Barbra Streissand",
				actorsTwo, genresTwo));

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
