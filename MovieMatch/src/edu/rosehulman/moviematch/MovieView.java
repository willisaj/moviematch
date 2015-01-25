package edu.rosehulman.moviematch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieView extends LinearLayout {

	private TextView mTitleTextView;
	private TextView mDescriptionTextView;
	private ImageView mImageView;

	public MovieView(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.view_movie, this);

		mTitleTextView = (TextView) findViewById(R.id.title_text_view);
		mDescriptionTextView = (TextView) findViewById(R.id.description_text_view);
		mImageView = (ImageView) findViewById(R.id.movie_imageView);

	}

	public void setData(String title, String description, String filepath) {
		mTitleTextView.setText(title);
		mDescriptionTextView.setText(description);
		// mImageView.set TODO

	}

}
