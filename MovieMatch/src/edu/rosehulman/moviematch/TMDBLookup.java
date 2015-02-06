package edu.rosehulman.moviematch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class TMDBLookup {
	
	private static final String GENRE_COMMAND = "/genre";
	private static final String LIST = "/list?";
	
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
	
	public boolean fillInMovieInfo(int movieId, Movie movie) {
		mUrl += MOVIE_COMMAND + "/" + movieId + "?";
		mUrl += API_KEY_ARG + mApiKey;
		try {
			JSONObject infoJson = TMDBAPICaller.makeCall(mUrl);
			
			List<Genre> genres = new ArrayList<Genre>();
			JSONArray genresJson = infoJson.getJSONArray("genres");
			for (int i = 0; i < genresJson.length(); i++) {
				JSONObject genreJson = genresJson.getJSONObject(i);
				String name = genreJson.getString("name");
				int id = genreJson.getInt("id");
				genres.add(new Genre(name, id));
			}
			movie.setGenres(genres);
			
			movie.setDescription(infoJson.getString("overview"));
			movie.setTagline(infoJson.getString("tagline"));
			movie.setDuration(infoJson.getInt("runtime"));
			String posterUrl = "http://image.tmdb.org/t/p/w300";
			posterUrl += infoJson.getString("poster_path");
			movie.setPosterUrl(posterUrl);
			
			String releaseDate = infoJson.getString("release_date");
			int releaseYear = Integer.valueOf(releaseDate.substring(0, releaseDate.indexOf("-")));
			movie.setReleaseYear(releaseYear);
			
		} catch (Exception e) {
			//TODO Do something about errors
			return false;
		}
		
		return true;
	}

	public List<Genre> getGenres() {
		List<Genre> genres = new ArrayList<Genre>();
		
		mUrl += GENRE_COMMAND + MOVIE_COMMAND + LIST;
		mUrl += API_KEY_ARG + mApiKey;
		try {
			JSONObject baseJson = TMDBAPICaller.makeCall(mUrl);
			JSONArray genresJson = baseJson.getJSONArray("genres");
			for (int i = 0; i < genresJson.length(); i++) {
				JSONObject genreJson = genresJson.getJSONObject(i);
				String name = genreJson.getString("name");
				int id = genreJson.getInt("id");
				genres.add(new Genre(name, id));
			}
		} catch (Exception e) {
			//TODO Handle errors better
		}
		
		return genres;
	}

}
