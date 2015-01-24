package edu.rosehulman.moviematch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

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

		Spinner distributionSpinner = (Spinner) findViewById(R.id.genre_spinner);
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

	}
}
