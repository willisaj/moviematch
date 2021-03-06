package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.appspot.willisaj_movie_match.moviematch.model.Account;

public class MainActivity extends Activity implements OnClickListener {
	public static final String KEY_TITLE = "KEY_TITLE";
	public static final String KEY_MOVIE_LIST = "MOVIE_LIST";

	private String distribution = "ERROR";

	private EditText mActorEditText;
	private EditText mDirectorEditText;
	private Spinner mGenreSpinner;

	private Button mLoginButton;
	private Button mSignupButton;
	private Button mLogoutButton;
	private TextView mLoginStatusText;

	private String mUserName;

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
		genres.add(0, new Genre(getString(R.string.genre_hint), 0, 0));
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
		distributionSpinner.setVisibility(View.GONE);

		Button wishListButton = (Button) findViewById(R.id.wishlist_button);
		Button recommendButton = (Button) findViewById(R.id.recommend_movie_button);
		wishListButton.setOnClickListener(this);
		recommendButton.setOnClickListener(this);

		mLoginButton = (Button) findViewById(R.id.login_button);
		mLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginButtonClicked();
			}
		});

		mSignupButton = (Button) findViewById(R.id.signup_button);
		mSignupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				signupButtonClicked();
			}
		});

		mLogoutButton = (Button) findViewById(R.id.logout_button);
		mLogoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logoutButtonClicked();
			}
		});
		
		mLoginStatusText = (TextView) findViewById(R.id.login_status);
	}

	private void updateLoginVisibility() {
		if (mUserName == null) {
			mLoginButton.setVisibility(View.VISIBLE);
			mSignupButton.setVisibility(View.VISIBLE);
			mLogoutButton.setVisibility(View.GONE);
			mLoginStatusText.setText(R.string.logged_out_status);
		} else {
			mLoginButton.setVisibility(View.GONE);
			mSignupButton.setVisibility(View.GONE);
			mLogoutButton.setVisibility(View.VISIBLE);
			mLoginStatusText.setText("Logged in as " + mUserName);
		}
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

		if (mUserName != null) {
			Preferences preferences = Preferences
					.getPreferencesForUser(mUserName);

			Collections.sort(movies, new MovieComparator(preferences));
		}

		intent.putExtra(KEY_TITLE, "Recommendations");
		intent.putExtra(KEY_MOVIE_LIST, (ArrayList<Movie>) movies);

		this.startActivity(intent);
	}

	protected void startMovieWishListActivity() {
		Intent intent;

		intent = new Intent(this, MovieWishListActivity.class);
		intent.putExtra(KEY_TITLE, "Wish List");

		this.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.recommend_movie_button) {
			startRecommendationActivity();
		} else if (v.getId() == R.id.wishlist_button) {
			startMovieWishListActivity();
		} else {
			Log.d("", "CRITICAL ERROR IN MAINACTIVITY: onClick()");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		menu.getItem(0).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						startPreferencesActivity();
						return false;
					}
				});
		return true;
	}

	private void loginButtonClicked() {
		LayoutInflater inflater = getLayoutInflater();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.login));
		final View loginDialog = inflater.inflate(R.layout.dialog_login, null);
		builder.setView(loginDialog);

		builder.setPositiveButton(getString(R.string.login),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText usernameText = (EditText) loginDialog
								.findViewById(R.id.username_editText);
						EditText passwordText = (EditText) loginDialog
								.findViewById(R.id.password_editText);

						attemptLogin(usernameText.getText().toString(),
								passwordText.getText().toString());
					}
				});
		builder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				});
		builder.show();
	}

	private void signupButtonClicked() {
		LayoutInflater inflater = getLayoutInflater();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.signup));
		final View signupDialog = inflater
				.inflate(R.layout.dialog_signup, null);
		builder.setView(signupDialog);

		builder.setPositiveButton(getString(R.string.signup),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText usernameText = (EditText) signupDialog
								.findViewById(R.id.username_editText);
						EditText passwordText = (EditText) signupDialog
								.findViewById(R.id.password_editText);
						EditText emailText = (EditText) signupDialog
								.findViewById(R.id.email_editText);

						attemptSignup(usernameText.getText().toString(),
								passwordText.getText().toString(),
								emailText.toString(), false);
					}
				});
		builder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				});
		builder.show();
	}

	private void logoutButtonClicked() {
		mUserName = null;

		alertMessage(getString(R.string.logout),
				getString(R.string.logout_success_message));

		updateLoginVisibility();
	}

	private void attemptLogin(String userName, String password) {
		try {
			Account account = BackendApi.getAccount(userName);
			if (account == null || !account.getPassword().equals(password)) {
				String title = getString(R.string.error);
				String message = getString(R.string.login_invalid_message);
				alertMessage(title, message);
			} else {
				String title = getString(R.string.success);
				String message = getString(R.string.login_success_message);
				alertMessage(title, message);

				mUserName = userName;
				updateLoginVisibility();
			}
		} catch (Exception e) {
			String title = getString(R.string.error);
			String message = getString(R.string.login_fail_message);
			alertMessage(title, message);
		}

	}

	private void attemptSignup(String userName, String password, String email,
			boolean willShare) {
		try {
			Account account = BackendApi.getAccount(userName);
			if (account != null) {
				String title = getString(R.string.error);
				String message = getString(R.string.signup_invalid_message);
				alertMessage(title, message);
			} else {
				BackendApi.insertAccount(userName, password, email, willShare);

				String title = getString(R.string.success);
				String message = getString(R.string.signup_success_message);
				alertMessage(title, message);

				attemptLogin(userName, password);
			}
		} catch (Exception e) {
			String title = getString(R.string.error);
			String message = getString(R.string.signup_fail_message);
			alertMessage(title, message);
		}
	}

	private void alertMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.okay,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				});
		builder.show();
	}

	protected void startPreferencesActivity() {
		Intent prefIntent = new Intent(this, PreferencesActivity.class);
		prefIntent.putExtra(PreferencesActivity.KEY_USER_NAME, mUserName != null ? mUserName : "");
		this.startActivity(prefIntent);
	}
}
