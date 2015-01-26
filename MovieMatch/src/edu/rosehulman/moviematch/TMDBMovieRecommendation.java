package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

public class TMDBMovieRecommendation implements IMovieRecommendation {
	
	private static final String COMMAND = "/discover/movie?";
	private static final String ACTORS = "with_cast=";
	private static final String DIRECTOR = "with_crew=";
	private static final String API_KEY = "api_key=";

	private final String mApiKey;
	private final String mBaseUrl;

	private List<Integer> mActorIds;
	private int mDirectorId = 0;
	
	private String mUrl;

	public TMDBMovieRecommendation(String baseUrl, String apiKey) {
		mBaseUrl = baseUrl;
		mApiKey = apiKey;
		mUrl = mBaseUrl + COMMAND;
	}
	
	@Override
	public List<Movie> getMovies() {
		int numArgs = 0;
		if (mActorIds != null & mActorIds.size() > 0) {
			mUrl += ACTORS;
			mUrl += mActorIds.get(0);
			for (int i = 1; i < mActorIds.size(); i++) {
				mUrl += "," + mActorIds.get(i);
			}
			numArgs++;
		}
		
		if (mDirectorId != 0) {
			mUrl += (numArgs > 0) ? "&" : "";
			mUrl += DIRECTOR + mDirectorId;
			numArgs++;
		}
		
		mUrl += (numArgs > 0) ? "&" : "";
		mUrl += API_KEY + mApiKey;
		
		List<Movie> movies = new ArrayList<Movie>();
		try {
			List<JSONObject> jsonMovies = TMDBAPICaller.getResults(mUrl);
			for (JSONObject jsonMovie : jsonMovies) {
				int movieId = jsonMovie.getInt("id");
				
				String title = jsonMovie.getString("title");
				List<String> actors = new TMDBLookup(mBaseUrl, mApiKey).getActors(movieId);
				String director = new TMDBLookup(mBaseUrl, mApiKey).getDirector(movieId);
				//TODO: Get a movie's genres
				Movie movie = new Movie(title, director, actors, new ArrayList<String>());
				
				movies.add(movie);
			}
		} catch (Exception e) {
			// TODO handle exceptions better
		}
		return movies;
	}

	@Override
	public IMovieRecommendation withActor(String actor) {
		if (mActorIds == null) {
			mActorIds = new ArrayList<Integer>();
		}
		mActorIds.add(new TMDBLookup(mBaseUrl, mApiKey).getPersonId(actor));
		return this;
	}

	@Override
	public IMovieRecommendation withActors(List<String> actors) {
		for (String actor : actors) {
			this.withActor(actor);
		}
		return this;
	}

	@Override
	public IMovieRecommendation withDirector(String director) {
		mDirectorId = new TMDBLookup(mBaseUrl, mApiKey).getPersonId(director);
		return this;
	}

}
