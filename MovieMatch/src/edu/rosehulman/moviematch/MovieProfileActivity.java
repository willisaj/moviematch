package edu.rosehulman.moviematch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class MovieProfileActivity extends Activity implements OnClickListener,
		OnRatingBarChangeListener {
	public static final String KEY_MPAA_RATING = "KEY_MPAA_RATING";
	public static final String KEY_DURATION = "KEY_DURATION";
	public static final String KEY_DESCRIPTION = "KEY_DESCRIPTION";
	public static final String KEY_METACRITIC_RATING = "KEY_METACRITIC_RATING";
	public static final String KEY_ROTTEN_RATING = "KEY_ROTTEN_RATING";
	public static final String KEY_MOVIE_TITLE = "KEY_MOVIE_TITLE";
	public static final String KEY_USER_RATING = "KEY_USER_RATING";
	private static final String REMOVE_FROM_WISHLIST = "Remove from Wishlist";
	private static final String ADD_TO_WISHLIST = "Add to Wishlist";
	public static final String KEY_IS_ON_WISHLIST = "KEY_IS_ON_WISHLIST";
	private double rating;
	private Button wishListButton;
	private boolean isOnWishList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_profile);

		String movieTitle = getIntent().getStringExtra(KEY_MOVIE_TITLE);
		double rottenRating = getIntent().getDoubleExtra(KEY_ROTTEN_RATING, 0);
		String movieDescription = getIntent().getStringExtra(KEY_DESCRIPTION);
		String movieDuration = getIntent().getStringExtra(KEY_DURATION);
		String mpaaRating = getIntent().getStringExtra(KEY_MPAA_RATING);
		this.rating = getIntent().getDoubleExtra(KEY_USER_RATING, 0);
		this.isOnWishList = getIntent().getBooleanExtra(KEY_IS_ON_WISHLIST,
				false);
		double metacriticRating = getIntent().getDoubleExtra(
				KEY_METACRITIC_RATING, 0);

		TextView titleView = (TextView) findViewById(R.id.posterCaptionTitleView);
		TextView rottenRatingView = (TextView) findViewById(R.id.rottenRating);
		TextView metacriticRatingView = (TextView) findViewById(R.id.metacriticRating);
		TextView descriptionView = (TextView) findViewById(R.id.description);
		TextView movieLengthView = (TextView) findViewById(R.id.lengthTextView);
		TextView mpaaRatingView = (TextView) findViewById(R.id.mpaaTextView);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		this.wishListButton = (Button) findViewById(R.id.wishListButton);

		if (this.isOnWishList) {
			this.wishListButton.setBackgroundColor(Color.GREEN);
			this.wishListButton.setText(ADD_TO_WISHLIST);
		} else {
			this.wishListButton.setBackgroundColor(Color.RED);
			this.wishListButton.setText(REMOVE_FROM_WISHLIST);
		}
		this.wishListButton.setOnClickListener(this);

		ratingBar.setRating((float) this.rating);
		ratingBar.setOnRatingBarChangeListener(this);

		this.setTitle(movieTitle);

		titleView.setText(movieTitle);

		rottenRatingView.setText(""+rottenRating);

		metacriticRatingView.setText("" + metacriticRating);

		descriptionView.setText(movieDescription);

		movieLengthView.setText(" "+movieDuration);

		mpaaRatingView.setText(" " + mpaaRating);

	}

	@Override
	public void onClick(View v) {
		toggleMovieOnWishList();

	}

	private void toggleMovieOnWishList() {
		// TODO add communication with database class
		if (this.isOnWishList) {
			this.wishListButton.setBackgroundColor(Color.RED);
			this.wishListButton.setText(REMOVE_FROM_WISHLIST);
			this.isOnWishList = false;
		} else {
			this.wishListButton.setBackgroundColor(Color.GREEN);
			this.wishListButton.setText(ADD_TO_WISHLIST);
			this.isOnWishList = true;
		}

	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// TODO communicate this change to the database
		this.rating = rating;

	}

}
