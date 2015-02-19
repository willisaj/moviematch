package edu.rosehulman.moviematch;

import java.util.List;

public class TMDBMovieRecommender implements IMovieRecommender {

	private final static String BASE_URL = "http://api.themoviedb.org/3";
	private final static String API_KEY = "d72629093aa0bad0f997e31dc04143b7";

	@Override
	public IMovieRecommendation getRecommendation() {
		return new TMDBMovieRecommendation(BASE_URL, API_KEY);
	}

	public int getActorId(String name) {
		return new TMDBLookup(BASE_URL, API_KEY).getPersonId(name);
	}

	public List<Genre> getGenres() {
		return new TMDBLookup(BASE_URL, API_KEY).getGenres();
	}

	public void getMovieById(int movieId, Movie movie) {
		new TMDBLookup(BASE_URL, API_KEY).fillInMovieInfo(movieId, movie);
	}

}
