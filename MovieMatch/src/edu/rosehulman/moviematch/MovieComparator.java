package edu.rosehulman.moviematch;

import java.util.Comparator;

import android.util.Log;

public class MovieComparator implements Comparator<Movie> {
	
	private Preferences mPreferences;
	
	private double leftScore;
	private double rightScore;
	
	private Movie left;
	private Movie right;
	
	public MovieComparator(Preferences preferences) {
		mPreferences = preferences;
	}

	@Override
	public int compare(Movie leftMovie, Movie rightMovie) {
		left = leftMovie;
		right = rightMovie;
		
		leftScore = 0;
		rightScore = 0;
		
		actors();
		directors();
		
		if (leftMovie.getDirector().equals("Tim Burton")) {
			Log.d("MOVIEMATCH", "MOdded value: " + leftScore);
		}
		
		if (leftScore < rightScore) {
			return 1;
		} else if (leftScore == rightScore) {
			return 0;
		}
		
		return -1;
	}
	
	private void actors() {
		for (RatablePerson actor : mPreferences.getActors()) {
			if (left.getActors().contains(actor.getName())) {
				double moddedValue = actor.getRating() - 2.5;
				leftScore += moddedValue * 2;
			}
		}
		
		for (RatablePerson actor : mPreferences.getActors()) {
			if (right.getActors().contains(actor.getName())) {
				double moddedValue = actor.getRating() - 2.5;
				rightScore += moddedValue * 2;
			}
		}
	}
	
	private void directors() {
		for (RatablePerson director : mPreferences.getDirectors()) {
			if (left.getDirector().equals(director.getName())) {
				double moddedValue = director.getRating() - 2.5;
				leftScore += moddedValue * 2;
			}
		}
		
		for (RatablePerson director : mPreferences.getDirectors()) {
			if (right.getDirector().equals(director.getName())) {
				double moddedValue = director.getRating() - 2.5;
				rightScore += moddedValue * 2;
			}
		}
	}
}
