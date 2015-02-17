package edu.rosehulman.moviematch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class RatableView extends LinearLayout {

	private TextView mNameTextView;
	private RatingBar mRateBar;

	public RatableView(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.view_starable, this);

		mNameTextView = (TextView) findViewById(R.id.starTitleTextView);
		mRateBar = (RatingBar) findViewById(R.id.listRatingBar);
	}

	public void setText(String name) {
		mNameTextView.setText(name);
	}
	
	public void setRating(float rating){
		mRateBar.setRating(rating);
	}

}
