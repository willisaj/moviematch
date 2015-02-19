package edu.rosehulman.moviematch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

public class PersonRaterView extends LinearLayout {

	private EditText mEditText;
	private RatingBar mRateBar;

	public PersonRaterView(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.view_person_rater, this);

		mEditText = (EditText) findViewById(R.id.dialogEditText);
		mRateBar = (RatingBar) findViewById(R.id.dialogRatingBar);
	}

	public void setHint(int resid) {
		mEditText.setHint(resid);
	}

	public void setRating(float rating) {
		mRateBar.setRating(rating);
	}

	public float getRating() {
		return mRateBar.getRating();
	}

	public String getName() {
		return mEditText.getText().toString();
	}

}
