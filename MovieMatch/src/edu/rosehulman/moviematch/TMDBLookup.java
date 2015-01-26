package edu.rosehulman.moviematch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class TMDBLookup {
	
	private static final String SEARCH_COMMAND = "/search";
	private static final String PERSON = "/person?";
	
	private static final String MOVIE_COMMAND = "/movie";
	private static final String CREDITS = "/credits?";
	
	private static final String QUERY_ARG = "query=";
	private static final String API_KEY_ARG = "api_key=";
	
	private static final String JOB_DIRECTOR = "Director";
	
	private final String mApiKey;
	private String mUrl;
	
	public TMDBLookup(String baseUrl, String apiKey) {
		mApiKey = apiKey;
		mUrl = baseUrl;
	}
	
	public int getPersonId(String personName) {
		mUrl += SEARCH_COMMAND + PERSON;
		mUrl += QUERY_ARG + personName;
		mUrl += "&" + API_KEY_ARG + mApiKey;
		try {
			JSONObject json = TMDBAPICaller.getFirstResult(mUrl);
			return json.getInt("id");
		} catch (Exception e) {
			//TODO handle errors better
		}
		return 0;
	}
	
	public List<String> getActors(int movieId) {
		List<String> actors = new ArrayList<String>();
		
		mUrl += MOVIE_COMMAND + "/" +  movieId + CREDITS;
		mUrl += API_KEY_ARG + mApiKey;
		try {
			JSONObject json = TMDBAPICaller.makeCall(mUrl);
			JSONArray actorsJson = json.getJSONArray("cast");
			for (int i = 0; i < actorsJson.length(); i++) {
				JSONObject actorJson = actorsJson.getJSONObject(i);
				actors.add(actorJson.getString("name"));
			}
		} catch (Exception e) {
			//TODO handle errors better
		}
		return actors;
	}
	
	public String getDirector(int movieId) {
		mUrl += MOVIE_COMMAND + "/" +  movieId + CREDITS;
		mUrl += API_KEY_ARG + mApiKey;
		try {
			JSONObject json = TMDBAPICaller.makeCall(mUrl);
			JSONArray crewJson = json.getJSONArray("crew");
			for (int i = 0; i < crewJson.length(); i++) {
				JSONObject crewMemberJson = crewJson.getJSONObject(i);
				if (crewMemberJson.getString("job").equals(JOB_DIRECTOR)) {
					return crewMemberJson.getString("name");
				}
			}
		} catch (Exception e) {
			//TODO handle errors better
		}
		//TODO return a better message
		return "Could not find director";
	}

}
