package edu.rosehulman.moviematch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class MovieProfileActivity extends Activity implements OnClickListener,
		OnRatingBarChangeListener {
	public static final String KEY_METACRITIC_RATING = "KEY_METACRITIC_RATING";
	public static final String KEY_USER_RATING = "KEY_USER_RATING";
	private static final String REMOVE_FROM_WISHLIST = "Remove from Wishlist";
	private static final String ADD_TO_WISHLIST = "Add to Wishlist";
	public static final String KEY_IS_ON_WISHLIST = "KEY_IS_ON_WISHLIST";

	public static final String KEY_MOVIE = "KEY_MOVIE";

	private static final String RT_CERTIFIED = "Certified Fresh";
	private static final String RT_FRESH = "Fresh";
	private static final String RT_ROTTEN = "Rotten";

	private double rating;
	private Button wishListButton;
	private boolean isOnWishList;

	private Movie mMovie;

	private TextView mTitleView;
	private TextView mTaglineView;
	private TextView mDescriptionView;
	private TextView mDurationView;
	private TextView mMPAAView;

	private ImageView mMoviePortrait;

	private ImageView mRTRatingView;
	private TextView mRTScoreView;

	private Button mPurchaseGooglePlayButton;
	private Button mPurchaseAmazonButton;

	private Button mWatchTrailerButton;
	private MovieDataAdapter mMovieDataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_profile);

		mMovieDataAdapter = new MovieDataAdapter(this);
		mMovieDataAdapter.open();

		mMovie = (Movie) getIntent().getParcelableExtra(KEY_MOVIE);
		new RTLookup().fillInMovieInfo(mMovie);
		new GuideboxLookup().fillInMovieInfo(mMovie);

		setTitle(mMovie.getTitle());

		mTitleView = (TextView) findViewById(R.id.movie_title_view);
		mTitleView.setText(mMovie.getTitle());

		mTaglineView = (TextView) findViewById(R.id.posterCaptionTitleView);
		mTaglineView.setText("\"" + mMovie.getTagline() + "\"");
		if (mMovie.getTagline().equals("")) {
			mTaglineView.setVisibility(View.GONE);
		}

		mDescriptionView = (TextView) findViewById(R.id.description);
		mDescriptionView.setText(mMovie.getDescription());

		mDurationView = (TextView) findViewById(R.id.lengthTextView);
		mDurationView.setText(mMovie.getDuration() + " minutes");

		mMPAAView = (TextView) findViewById(R.id.mpaaTextView);
		mMPAAView.setText(mMovie.getMPAARating());

		// Rotten Tomatoes
		if (mMovie.getRTRating() != null) {
			mRTRatingView = (ImageView) findViewById(R.id.rottenRating);
			if (mMovie.getRTRating().equals(RT_CERTIFIED)) {
				mRTRatingView.setImageDrawable(getResources().getDrawable(
						R.drawable.rt_certified));
			} else if (mMovie.getRTRating().equals(RT_FRESH)) {
				mRTRatingView.setImageDrawable(getResources().getDrawable(
						R.drawable.rt_fresh));
			} else if (mMovie.getRTRating().equals(RT_ROTTEN)) {
				mRTRatingView.setImageDrawable(getResources().getDrawable(
						R.drawable.rt_rotten));
			}

			mRTScoreView = (TextView) findViewById(R.id.rottenScore);
			mRTScoreView.setText(mMovie.getRTScore() + "%");
		} else {
			LinearLayout rottenTomatoes = (LinearLayout) findViewById(R.id.rottenTomatoes);
			rottenTomatoes.setVisibility(View.GONE);
		}

		// Google Play
		mPurchaseGooglePlayButton = (Button) findViewById(R.id.purchase_google_play_button);
		if (mMovie.getGooglePlayPurchaseUrl() != null) {
			mPurchaseGooglePlayButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(mMovie.getGooglePlayPurchaseUrl()));
					startActivity(browserIntent);
				}
			});
		} else {
			mPurchaseGooglePlayButton.setVisibility(View.GONE);
		}

		// Amazon
		mPurchaseAmazonButton = (Button) findViewById(R.id.purchase_amazon_button);
		if (mMovie.getAmazonPurchaseUrl() != null) {
			mPurchaseAmazonButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(mMovie.getAmazonPurchaseUrl()));
					startActivity(browserIntent);
				}
			});
		} else {
			mPurchaseAmazonButton.setVisibility(View.GONE);
		}

		mWatchTrailerButton = (Button) findViewById(R.id.watch_trailer_button);
		mWatchTrailerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startTrailer();
			}
		});

		this.rating = getIntent().getDoubleExtra(KEY_USER_RATING, 0);
		this.isOnWishList = mMovieDataAdapter.isOnWishList(mMovie.getTmdbId());
		double metacriticRating = getIntent().getDoubleExtra(
				KEY_METACRITIC_RATING, 0);

		// Show the movie poster
		mMoviePortrait = (ImageView) findViewById(R.id.moviePortrait);
		new DownloadImageTask(mMoviePortrait).execute(mMovie.getPosterUrl());

		this.wishListButton = (Button) findViewById(R.id.wishListButton);

		if (this.isOnWishList) {
			this.wishListButton.setBackgroundColor(Color.RED);
			this.wishListButton.setText(REMOVE_FROM_WISHLIST);
		} else {
			this.wishListButton.setBackgroundColor(Color.GREEN);
			this.wishListButton.setText(ADD_TO_WISHLIST);
		}
		this.wishListButton.setOnClickListener(this);

	}

	private void startTrailer() {
		Intent intent = new Intent(this, TrailerActivity.class);
		intent.putExtra(TrailerActivity.KEY_TRAILER_URL, mMovie.getTrailerUrl());
		this.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		toggleMovieOnWishList();
	}

	private void toggleMovieOnWishList() {
		if (this.isOnWishList) {
			this.wishListButton.setBackgroundColor(Color.GREEN);
			this.wishListButton.setText(ADD_TO_WISHLIST);
			this.isOnWishList = false;
			mMovieDataAdapter.removeWishMovie(mMovie.getTmdbId());
		} else {
			this.wishListButton.setBackgroundColor(Color.RED);
			this.wishListButton.setText(REMOVE_FROM_WISHLIST);
			this.isOnWishList = true;
			SimpleMovie movieToAdd = new SimpleMovie(mMovie.getTmdbId(),
					mMovie.getTitle());
			mMovieDataAdapter.addWishMovie(movieToAdd);
		}

	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// TODO communicate this change to the database
		this.rating = rating;
	}

}