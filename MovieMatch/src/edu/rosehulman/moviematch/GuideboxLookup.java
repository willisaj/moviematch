package edu.rosehulman.moviematch;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class GuideboxLookup {
	
	private final String mBaseUrl;
	
	private final String mApiKey;
	private String mUrl;
	private final String mRegion = "US";
	
	private static final String SEARCH_COMMAND = "/search";
	private static final String MOVIE_COMMAND = "/movie";
	private static final String ID = "/id";
	private static final String TMDB = "/themoviedb";
	
	public GuideboxLookup() {
		this("https://api-public.guidebox.com/v1.43", "rKQTLTXiiXBwaRTCxsNA9FfCFR1hnuNp");
	}
	
	public GuideboxLookup(String baseUrl, String apiKey) {
		mBaseUrl = baseUrl;
		mApiKey = apiKey;
		mUrl = baseUrl + "/" + mRegion;
		mUrl += "/" + mApiKey;
	}
	
	public int getGuideboxIdFromTMDBId(int tmdbId) {
		Log.d("MOVIEMATCH", "TMDB ID: " + tmdbId);
		
		mUrl += SEARCH_COMMAND + MOVIE_COMMAND + ID + TMDB + "/" + tmdbId;
		
		Log.d("MOVIEMATCH", "URL2" + mUrl);
		try {
			JSONObject movieJson = TMDBAPICaller.makeCall(mUrl);
			return movieJson.getInt("id");
		} catch (Exception e) {
			//TODO handle errors better
		}
		//TODO Handle this better
		return -1;
	}
	
	public boolean fillInMovieInfo(Movie movie) {
		int guideboxId = new GuideboxLookup(mBaseUrl, mApiKey).getGuideboxIdFromTMDBId(movie.getTmdbId());
		
		Log.d("MOVIEMATCH", "Guidebox ID: " + guideboxId);
		
		mUrl += MOVIE_COMMAND + "/" + guideboxId;
		
		Log.d("MOVIEMATCH", "Url: " + mUrl);
		
		try {
			JSONObject infoJson = TMDBAPICaller.makeCall(mUrl);
			Log.d("MOVIEMATCH", "Got here");
			
			JSONObject trailersJson = infoJson.getJSONObject("trailers");
			JSONArray androidTrailersJson = trailersJson.getJSONArray("android");
			for (int i = 0; i < androidTrailersJson.length() && i < 1; i++) {
				JSONObject trailerJson = androidTrailersJson.getJSONObject(i);
				String trailerUrl = trailerJson.getString("embed");
				movie.setTrailerUrl(trailerUrl);
			}
			
			JSONArray purchaseAndroidJson = infoJson.getJSONArray("purchase_android_sources");
			for (int i = 0; i < purchaseAndroidJson.length(); i++) {
				Log.d("MOVIEMATCH", "Got here 2");
				JSONObject sourceJson = purchaseAndroidJson.getJSONObject(i);
				String source = sourceJson.getString("source");
				if (source.equals("google_play")) {
					movie.setGooglePlayPurchaseUrl(sourceJson.getString("link"));
					
					Log.d("MOVIEMATCH", "Link: " + movie.getGooglePlayPurchaseUrl());
				}
			}
		} catch (Exception e) {
			//TODO handle errors better
		}
		
		return false;
	}
}
