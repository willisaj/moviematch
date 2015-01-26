package edu.rosehulman.moviematch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TMDBAPICaller {

	public static final String CHARACTER_ENCODING = "UTF-8";

	public static JSONObject makeCall(String url) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException, UnsupportedEncodingException {
		Log.d("MOVIEMATCH", encode(url));
		String jsonString = new RetrieveFeedTask().execute(encode(url)).get();
		JSONObject json = new JSONObject(jsonString);
		return json;
	}

	public static JSONObject getFirstResult(String url)
			throws InterruptedException, ExecutionException, JSONException,
			TimeoutException, UnsupportedEncodingException {
		JSONObject pages = makeCall(url);
		return pages.getJSONArray("results").getJSONObject(0);
	}

	public static List<JSONObject> getResults(String url)
			throws InterruptedException, ExecutionException, JSONException,
			TimeoutException, UnsupportedEncodingException {
		JSONObject pages = makeCall(url);
		JSONArray results = pages.getJSONArray("results");
		List<JSONObject> jsonResults = new ArrayList<JSONObject>();
		for (int i = 0; i < results.length(); i++) {
			jsonResults.add(results.getJSONObject(i));
		}
		return jsonResults;
	}
	
	private static String encode(String url) throws UnsupportedEncodingException {
		return url.replaceAll(" ", "%20");
	}

}
