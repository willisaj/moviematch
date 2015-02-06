package edu.rosehulman.moviematch;

import org.json.JSONArray;
import org.json.JSONObject;

public class RTLookup {
	
	private String mApiKey = "ezqhvtcjp45sa4ejzp7x4424";
	
	private String mUrl;
	
	private static final String MOVIES_COMMAND = "/movies.json?";
	private static final String Q = "q=";
	private static final String API_KEY = "apikey=";
	private static final String PAGE_LIMIT = "page_limit=";
	
	public RTLookup() {
		mUrl = "http://api.rottentomatoes.com/api/public/v1.0";
	}
	
	public boolean fillInMovieInfo(Movie movie) {
		mUrl += MOVIES_COMMAND;
		mUrl += Q + qEncode(movie.getTitle());
		mUrl += "&" + API_KEY + mApiKey;
		mUrl += "&" + PAGE_LIMIT + 1;
		
		try {
			JSONObject resultsJson = TMDBAPICaller.makeCall(mUrl);
			JSONArray moviesJson = resultsJson.getJSONArray("movies");
			JSONObject movieJson = null;
			for (int i = 0; i < moviesJson.length(); i++) {
				movieJson = moviesJson.getJSONObject(i);
				int year = movieJson.getInt("year");
				String title = movieJson.getString("title");
				if (year == movie.getReleaseYear() && title.equals(movie.getTagline())) {
					break;
				}
			}
			
			JSONObject ratingsJson = movieJson.getJSONObject("ratings");
			movie.setRTRating(ratingsJson.getString("critics_rating"));
			movie.setRTScore(ratingsJson.getInt("critics_score"));
			
			movie.setMPAARating(movieJson.getString("mpaa_rating"));
		} catch (Exception e) {
			//TODO: Handle errors better
			return false;
		}
		
		return true;
	}

	private String qEncode(String title) {
		return title.replace(' ', '+');
	}
	
}
