package edu.rosehulman.moviematch;

import org.json.JSONArray;
import org.json.JSONObject;

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
		
		mUrl += SEARCH_COMMAND + MOVIE_COMMAND + ID + TMDB + "/" + tmdbId;
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
		
		mUrl += MOVIE_COMMAND + "/" + guideboxId;
		
		try {
			JSONObject infoJson = TMDBAPICaller.makeCall(mUrl);
			
			JSONObject trailersJson = infoJson.getJSONObject("trailers");
			JSONArray androidTrailersJson = trailersJson.getJSONArray("android");
			for (int i = 0; i < androidTrailersJson.length() && i < 1; i++) {
				JSONObject trailerJson = androidTrailersJson.getJSONObject(i);
				String trailerUrl = trailerJson.getString("embed");
				movie.setTrailerUrl(trailerUrl);
			}
			
			//Google Play
			JSONArray purchaseAndroidJson = infoJson.getJSONArray("purchase_android_sources");
			for (int i = 0; i < purchaseAndroidJson.length(); i++) {
				JSONObject sourceJson = purchaseAndroidJson.getJSONObject(i);
				String source = sourceJson.getString("source");
				if (source.equals("google_play")) {
					movie.setGooglePlayPurchaseUrl(sourceJson.getString("link"));
				}
			}
			
			//Purchase Amazon
			JSONArray purchaseWebJson = infoJson.getJSONArray("purchase_web_sources");
			for (int i = 0; i < purchaseWebJson.length(); i++) {
				JSONObject sourceJson = purchaseWebJson.getJSONObject(i);
				String source = sourceJson.getString("source");
				if (source.equals("amazon_buy")) {
					movie.setAmazonPurchaseUrl(sourceJson.getString("link"));
				}
			}
		} catch (Exception e) {
			//TODO handle errors better
		}
		
		return false;
	}
}
