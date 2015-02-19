package edu.rosehulman.moviematch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

import com.appspot.willisaj_movie_match.moviematch.Moviematch;
import com.appspot.willisaj_movie_match.moviematch.Moviematch.Account;
import com.appspot.willisaj_movie_match.moviematch.Moviematch.Actorpreference;
import com.appspot.willisaj_movie_match.moviematch.Moviematch.Directorpreference;
import com.appspot.willisaj_movie_match.moviematch.Moviematch.Genrepreference;
import com.appspot.willisaj_movie_match.moviematch.Moviematch.Platformpreference;
import com.appspot.willisaj_movie_match.moviematch.Moviematch.Ratingpreference;
import com.appspot.willisaj_movie_match.moviematch.Moviematch.Wishlistmovie;
import com.appspot.willisaj_movie_match.moviematch.model.AccountCollection;
import com.appspot.willisaj_movie_match.moviematch.model.AccountProtoUserNamePasswordEmailWillShare;
import com.appspot.willisaj_movie_match.moviematch.model.ActorPreference;
import com.appspot.willisaj_movie_match.moviematch.model.ActorPreferenceCollection;
import com.appspot.willisaj_movie_match.moviematch.model.ActorPreferenceProtoActorNameValueAccountKey;
import com.appspot.willisaj_movie_match.moviematch.model.DirectorPreference;
import com.appspot.willisaj_movie_match.moviematch.model.DirectorPreferenceCollection;
import com.appspot.willisaj_movie_match.moviematch.model.DirectorPreferenceProtoDirectorNameValueAccountKey;
import com.appspot.willisaj_movie_match.moviematch.model.GenrePreference;
import com.appspot.willisaj_movie_match.moviematch.model.GenrePreferenceCollection;
import com.appspot.willisaj_movie_match.moviematch.model.GenrePreferenceProtoGenreNameAccountKey;
import com.appspot.willisaj_movie_match.moviematch.model.PlatformPreference;
import com.appspot.willisaj_movie_match.moviematch.model.PlatformPreferenceCollection;
import com.appspot.willisaj_movie_match.moviematch.model.PlatformPreferenceProtoPlatformNameAccountKey;
import com.appspot.willisaj_movie_match.moviematch.model.RatingPreference;
import com.appspot.willisaj_movie_match.moviematch.model.RatingPreferenceCollection;
import com.appspot.willisaj_movie_match.moviematch.model.RatingPreferenceProtoRatingNameAccountKey;
import com.appspot.willisaj_movie_match.moviematch.model.WishlistMovie;
import com.appspot.willisaj_movie_match.moviematch.model.WishlistMovieCollection;
import com.appspot.willisaj_movie_match.moviematch.model.WishlistMovieProtoMovieNameMovieIdAccountKey;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

public class BackendApi {

	private static Moviematch mService = initialize();
	private static List<Genre> mGenres = (List<Genre>) new TMDBMovieRecommender()
			.getGenres();

	private static Moviematch initialize() {
		Moviematch.Builder builder = new Moviematch.Builder(
				AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		return builder.build();
	}

	// ///////////////////////////////////////////////////////
	// Accounts
	public static com.appspot.willisaj_movie_match.moviematch.model.Account getAccount(
			String user_name) throws InterruptedException, ExecutionException {
		AccountCollection accounts = new QueryForAccountTask().execute(
				user_name).get();
		if (accounts.getItems() == null) {
			return null;
		}
		return accounts.getItems().get(0);
	}

	public static void insertAccount(String user_name, String password,
			String email, boolean willShare) throws InterruptedException,
			ExecutionException {
		AccountProtoUserNamePasswordEmailWillShare account = new AccountProtoUserNamePasswordEmailWillShare();
		account.setUserName(user_name);
		account.setPassword(password);
		account.setEmail(email);
		account.setWillShare(willShare);

		new InsertAccountTask().execute(account);
	}

	// ///////////////////////////////////////////////////////////
	// Wishlist Movies

	private static SimpleMovie wishlistMovieToSimpleMovie(WishlistMovie movie) {
		return new SimpleMovie(Integer.parseInt(movie.getMovieId()),
				movie.getMovieName());
	}

	private static List<SimpleMovie> wishlistMovieToSimpleMovie(
			List<WishlistMovie> wishlistMovies) {
		List<SimpleMovie> movies = new ArrayList<SimpleMovie>();
		for (WishlistMovie movie : wishlistMovies) {
			movies.add(wishlistMovieToSimpleMovie(movie));
		}
		return movies;
	}

	public static List<SimpleMovie> getWishlistMoviesForUser(String user_name)
			throws InterruptedException, ExecutionException {
		return wishlistMovieToSimpleMovie(getWishlistMoviesForUser_API(user_name));
	}

	private static List<WishlistMovie> getWishlistMoviesForUser_API(
			String user_name) throws InterruptedException, ExecutionException {
		WishlistMovieCollection movies = new QueryForWishlistMoviesTask()
				.execute(user_name).get();
		if (movies.getItems() == null) {
			return new ArrayList<WishlistMovie>();
		}
		return movies.getItems();
	}

	public static void insertWishlistMovieForUser(String user_name,
			String movie_name, int movie_id) throws InterruptedException,
			ExecutionException {
		WishlistMovieProtoMovieNameMovieIdAccountKey movie = new WishlistMovieProtoMovieNameMovieIdAccountKey();
		movie.setAccountKey(user_name);
		movie.setMovieName(movie_name);
		movie.setMovieId("" + movie_id);

		new InsertWishlistMovieTask().execute(movie);
	}

	public static void deleteWishlistMovieForUser(String user_name, int movie_id)
			throws InterruptedException, ExecutionException {
		String entityKey = null;
		for (WishlistMovie movie : getWishlistMoviesForUser_API(user_name)) {
			if (movie.getMovieId().equals("" + movie_id)) {
				entityKey = movie.getEntityKey();
			}
		}
		if (entityKey == null) {
			return;
		}
		new DeleteWishlistMovieTask().execute(entityKey);
	}

	// /////////////////////////////////////////////////////
	// Actor Preferences
	private static RatablePerson actorPreferenceToRatablePerson(
			ActorPreference preference) {
		return new RatablePerson(preference.getActorName(), new Float(
				preference.getValue()));
	}

	private static List<RatablePerson> actorPreferenceToRatablePerson(
			List<ActorPreference> preferences) {
		List<RatablePerson> people = new ArrayList<RatablePerson>();
		for (ActorPreference preference : preferences) {
			people.add(actorPreferenceToRatablePerson(preference));
		}
		return people;
	}

	public static List<RatablePerson> getRatedActorsForUser(String user_name)
			throws InterruptedException, ExecutionException {
		return actorPreferenceToRatablePerson(getActorPreferencesForUser(user_name));
	}

	private static List<ActorPreference> getActorPreferencesForUser(
			String user_name) throws InterruptedException, ExecutionException {
		ActorPreferenceCollection actorPreferences = new QueryForActorPreferencesTask()
				.execute(user_name).get();
		if (actorPreferences.getItems() == null) {
			return new ArrayList<ActorPreference>();
		}
		return actorPreferences.getItems();
	}

	public static void insertRatedActorForUser(String user_name,
			String actor_name, double rating_value)
			throws InterruptedException, ExecutionException {
		ActorPreferenceProtoActorNameValueAccountKey actorPreference = new ActorPreferenceProtoActorNameValueAccountKey();
		actorPreference.setAccountKey(user_name);
		actorPreference.setActorName(actor_name);
		actorPreference.setValue(rating_value);

		new InsertActorPreferenceTask().execute(actorPreference);
	}

	public static void deleteRatedActorForUser(String user_name,
			String actor_name) throws InterruptedException, ExecutionException {
		String entityKey = null;
		for (ActorPreference preference : getActorPreferencesForUser(user_name)) {
			if (preference.getActorName().equals(actor_name)) {
				entityKey = preference.getEntityKey();
			}
		}
		if (entityKey == null) {

			return;
		}
		new DeleteActorPreferenceTask().execute(entityKey);
	}

	// ///////////////////////////////////////////////////////
	// Director Preferences
	private static RatablePerson directorPreferenceToRatablePerson(
			DirectorPreference preference) {
		return new RatablePerson(preference.getDirectorName(), new Float(
				preference.getValue()));
	}

	private static List<RatablePerson> directorPreferenceToRatablePerson(
			List<DirectorPreference> preferences) {
		List<RatablePerson> people = new ArrayList<RatablePerson>();
		for (DirectorPreference preference : preferences) {
			people.add(directorPreferenceToRatablePerson(preference));
		}
		return people;
	}

	public static List<RatablePerson> getRatedDirectorsForUser(String user_name)
			throws InterruptedException, ExecutionException {
		return directorPreferenceToRatablePerson(getDirectorPreferencesForUser(user_name));
	}

	private static List<DirectorPreference> getDirectorPreferencesForUser(
			String user_name) throws InterruptedException, ExecutionException {
		DirectorPreferenceCollection directorPreferences = new QueryForDirectorPreferencesTask()
				.execute(user_name).get();
		if (directorPreferences.getItems() == null) {
			return new ArrayList<DirectorPreference>();
		}
		return directorPreferences.getItems();
	}

	public static DirectorPreference insertRatedDirectorForUser(
			String user_name, String director_name, double rating_value)
			throws InterruptedException, ExecutionException {
		DirectorPreferenceProtoDirectorNameValueAccountKey directorPreference = new DirectorPreferenceProtoDirectorNameValueAccountKey();
		directorPreference.setAccountKey(user_name);
		directorPreference.setDirectorName(director_name);
		directorPreference.setValue(rating_value);

		return new InsertDirectorPreferenceTask().execute(directorPreference)
				.get();
	}

	public static DirectorPreference deleteRatedDirectorForUser(
			String user_name, String director_name)
			throws InterruptedException, ExecutionException {
		String entityKey = null;
		for (DirectorPreference preference : getDirectorPreferencesForUser(user_name)) {
			if (preference.getDirectorName().equals(director_name)) {
				entityKey = preference.getEntityKey();
			}
		}
		if (entityKey == null) {
			return null;
		}
		return new DeleteDirectorPreferenceTask().execute(entityKey).get();
	}

	// /////////////////////////////////////////////////////
	// Genre Preferences

	private static Genre genrePreferenceToGenre(GenrePreference preference) {
		for (Genre genre : mGenres) {
			if (preference.getGenreName().equals(genre.getName())) {
				return new Genre(genre.getName(), genre.getId(),
						genre.getPreference());
			}
		}
		return mGenres.get(0);
	}

	private static List<Genre> genrePreferenceToGenre(
			List<GenrePreference> preferences) {
		List<Genre> genres = new ArrayList<Genre>();
		for (GenrePreference preference : preferences) {
			genres.add(genrePreferenceToGenre(preference));
		}
		return genres;
	}

	public static List<Genre> getRatedGenresForUser(String user_name)
			throws InterruptedException, ExecutionException {
		return genrePreferenceToGenre(getGenrePreferencesForUser(user_name));
	}

	private static List<GenrePreference> getGenrePreferencesForUser(
			String user_name) throws InterruptedException, ExecutionException {
		GenrePreferenceCollection genrePreferences = new QueryForGenrePreferencesTask()
				.execute(user_name).get();
		if (genrePreferences.getItems() == null) {
			return new ArrayList<GenrePreference>();
		}
		return genrePreferences.getItems();
	}

	public static GenrePreference insertRatedGenreForUser(String user_name,
			String genre_name, double rating_value)
			throws InterruptedException, ExecutionException {
		GenrePreferenceProtoGenreNameAccountKey genrePreference = new GenrePreferenceProtoGenreNameAccountKey();
		genrePreference.setAccountKey(user_name);
		genrePreference.setGenreName(genre_name);

		return new InsertGenrePreferenceTask().execute(genrePreference).get();
	}

	public static GenrePreference deleteRatedGenreForUser(String user_name,
			String genre_name) throws InterruptedException, ExecutionException {
		String entityKey = null;
		for (GenrePreference preference : getGenrePreferencesForUser(user_name)) {
			if (preference.getGenreName().equals(genre_name)) {
				entityKey = preference.getEntityKey();
			}
		}
		if (entityKey == null) {
			return null;
		}
		return new DeleteGenrePreferenceTask().execute(entityKey).get();
	}

	// /////////////////////////////////////////////////////
	// Rating Preferences

	private static MPAA ratingPreferenceToMPAA(RatingPreference preference) {
		return new MPAA(preference.getRatingName(), 0);
	}

	private static List<MPAA> ratingPreferenceToMPAA(
			List<RatingPreference> preferences) {
		List<MPAA> ratings = new ArrayList<MPAA>();
		for (RatingPreference preference : preferences) {
			ratings.add(ratingPreferenceToMPAA(preference));
		}
		return ratings;
	}

	public static List<MPAA> getRatedRatingsForUser(String user_name)
			throws InterruptedException, ExecutionException {
		return ratingPreferenceToMPAA(getRatingPreferencesForUser(user_name));
	}

	private static List<RatingPreference> getRatingPreferencesForUser(
			String user_name) throws InterruptedException, ExecutionException {

		RatingPreferenceCollection ratingPreferences = new QueryForRatingPreferencesTask()
				.execute(user_name).get();
		if (ratingPreferences.getItems() == null) {
			return new ArrayList<RatingPreference>();
		}
		return ratingPreferences.getItems();
	}

	public static RatingPreference insertRatedMPAAForUser(String user_name,
			String rating_name, double rating_value)
			throws InterruptedException, ExecutionException {
		RatingPreferenceProtoRatingNameAccountKey ratingPreference = new RatingPreferenceProtoRatingNameAccountKey();
		ratingPreference.setAccountKey(user_name);
		ratingPreference.setRatingName(rating_name);

		return new InsertRatingPreferenceTask().execute(ratingPreference).get();
	}

	public static RatingPreference deleteRatedMPAAForUser(String user_name,
			String rating_name) throws InterruptedException, ExecutionException {
		String entityKey = null;
		for (RatingPreference preference : getRatingPreferencesForUser(user_name)) {
			if (preference.getRatingName().equals(rating_name)) {
				entityKey = preference.getEntityKey();
			}
		}
		if (entityKey == null) {
			return null;
		}
		return new DeleteRatingPreferenceTask().execute(entityKey).get();
	}

	// /////////////////////////////////////////////////////
	// Platform Preferences

	private static Platform platformPreferenceToPlatform(
			PlatformPreference preference) {
		return new Platform(preference.getPlatformName(), 0);
	}

	private static List<Platform> platformPreferenceToPlatform(
			List<PlatformPreference> preferences) {
		List<Platform> platforms = new ArrayList<Platform>();
		for (PlatformPreference preference : preferences) {
			platforms.add(platformPreferenceToPlatform(preference));
		}
		return platforms;
	}

	public static List<Platform> getRatedPlatformsForUser(String user_name)
			throws InterruptedException, ExecutionException {
		return platformPreferenceToPlatform(getPlatformPreferencesForUser(user_name));
	}

	private static List<PlatformPreference> getPlatformPreferencesForUser(
			String user_name) throws InterruptedException, ExecutionException {

		PlatformPreferenceCollection platformPreferences = new QueryForPlatformPreferencesTask()
				.execute(user_name).get();
		if (platformPreferences.getItems() == null) {
			return new ArrayList<PlatformPreference>();
		}
		return platformPreferences.getItems();
	}

	public static PlatformPreference insertRatedPlatformForUser(
			String user_name, String platform_name, double platform_value)
			throws InterruptedException, ExecutionException {
		PlatformPreferenceProtoPlatformNameAccountKey platformPreference = new PlatformPreferenceProtoPlatformNameAccountKey();
		platformPreference.setAccountKey(user_name);
		platformPreference.setPlatformName(platform_name);

		return new InsertPlatformPreferenceTask().execute(platformPreference)
				.get();
	}

	public static PlatformPreference deleteRatedPlatformForUser(
			String user_name, String platform_name)
			throws InterruptedException, ExecutionException {
		String entityKey = null;
		for (PlatformPreference preference : getPlatformPreferencesForUser(user_name)) {
			if (preference.getPlatformName().equals(platform_name)) {
				entityKey = preference.getEntityKey();
			}
		}
		if (entityKey == null) {
			return null;
		}
		return new DeletePlatformPreferenceTask().execute(entityKey).get();
	}

	// ////////////////////////////////////////////
	// Accounts
	private static class QueryForAccountTask extends
			AsyncTask<String, Void, AccountCollection> {

		@Override
		protected AccountCollection doInBackground(String... params) {
			AccountCollection accounts = null;
			try {
				Account.List query = mService.account().list(params[0]);
				accounts = query.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed loading " + e);
			}
			return accounts;
		}
	}

	private static class InsertAccountTask
			extends
			AsyncTask<AccountProtoUserNamePasswordEmailWillShare, Void, com.appspot.willisaj_movie_match.moviematch.model.Account> {

		@Override
		protected com.appspot.willisaj_movie_match.moviematch.model.Account doInBackground(
				AccountProtoUserNamePasswordEmailWillShare... quotes) {
			com.appspot.willisaj_movie_match.moviematch.model.Account returnedPreference = null;
			try {
				returnedPreference = mService.account().insert(quotes[0])
						.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed inserting " + e);
			}
			return returnedPreference;
		}
	}

	// ////////////////////////////////////////////
	// WishlistMovie Preferences
	private static class QueryForWishlistMoviesTask extends
			AsyncTask<String, Void, WishlistMovieCollection> {

		@Override
		protected WishlistMovieCollection doInBackground(String... params) {
			WishlistMovieCollection preferences = null;
			try {
				Wishlistmovie.List query = mService.wishlistmovie().list(
						params[0]);
				preferences = query.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed loading " + e);
			}
			return preferences;
		}
	}

	private static class InsertWishlistMovieTask
			extends
			AsyncTask<WishlistMovieProtoMovieNameMovieIdAccountKey, Void, WishlistMovie> {

		@Override
		protected WishlistMovie doInBackground(
				WishlistMovieProtoMovieNameMovieIdAccountKey... quotes) {
			WishlistMovie returnedPreference = null;
			try {
				returnedPreference = mService.wishlistmovie()
						.insert(quotes[0].getAccountKey(), quotes[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed inserting " + e);
			}
			return returnedPreference;
		}
	}

	private static class DeleteWishlistMovieTask extends
			AsyncTask<String, Void, WishlistMovie> {

		@Override
		protected WishlistMovie doInBackground(String... entityKeys) {
			WishlistMovie returnedMovie = null;
			try {
				returnedMovie = mService.wishlistmovie().delete(entityKeys[0])
						.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed deleting " + e);
			}
			return returnedMovie;
		}
	}

	// ////////////////////////////////////////////
	// Actor Preferences
	private static class QueryForActorPreferencesTask extends
			AsyncTask<String, Void, ActorPreferenceCollection> {

		@Override
		protected ActorPreferenceCollection doInBackground(String... params) {
			ActorPreferenceCollection preferences = null;
			try {
				Actorpreference.List query = mService.actorpreference().list(
						params[0]);
				preferences = query.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed loading " + e);
			}
			return preferences;
		}
	}

	private static class InsertActorPreferenceTask
			extends
			AsyncTask<ActorPreferenceProtoActorNameValueAccountKey, Void, ActorPreference> {

		@Override
		protected ActorPreference doInBackground(
				ActorPreferenceProtoActorNameValueAccountKey... quotes) {
			ActorPreference returnedPreference = null;
			try {
				returnedPreference = mService.actorpreference()
						.insert(quotes[0].getAccountKey(), quotes[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed inserting " + e);
			}
			return returnedPreference;
		}
	}

	private static class DeleteActorPreferenceTask extends
			AsyncTask<String, Void, ActorPreference> {

		@Override
		protected ActorPreference doInBackground(String... entityKeys) {
			ActorPreference returnedQuote = null;
			try {
				returnedQuote = mService.actorpreference()
						.delete(entityKeys[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed deleting " + e);
			}
			return returnedQuote;
		}
	}

	// ///////////////////////////////////////////////
	// Director Preferences
	private static class QueryForDirectorPreferencesTask extends
			AsyncTask<String, Void, DirectorPreferenceCollection> {

		@Override
		protected DirectorPreferenceCollection doInBackground(String... params) {
			DirectorPreferenceCollection preferences = null;
			try {
				Directorpreference.List query = mService.directorpreference()
						.list(params[0]);
				preferences = query.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed loading " + e);
			}
			return preferences;
		}
	}

	private static class InsertDirectorPreferenceTask
			extends
			AsyncTask<DirectorPreferenceProtoDirectorNameValueAccountKey, Void, DirectorPreference> {

		@Override
		protected DirectorPreference doInBackground(
				DirectorPreferenceProtoDirectorNameValueAccountKey... quotes) {
			DirectorPreference returnedPreference = null;
			try {
				returnedPreference = mService.directorpreference()
						.insert(quotes[0].getAccountKey(), quotes[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed inserting " + e);
			}
			return returnedPreference;
		}
	}

	private static class DeleteDirectorPreferenceTask extends
			AsyncTask<String, Void, DirectorPreference> {

		@Override
		protected DirectorPreference doInBackground(String... entityKeys) {
			DirectorPreference returnedQuote = null;
			try {
				returnedQuote = mService.directorpreference()
						.delete(entityKeys[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed deleting " + e);
			}
			return returnedQuote;
		}
	}

	// ///////////////////////////////////////////////
	// Genre Preferences
	private static class QueryForGenrePreferencesTask extends
			AsyncTask<String, Void, GenrePreferenceCollection> {

		@Override
		protected GenrePreferenceCollection doInBackground(String... params) {
			GenrePreferenceCollection preferences = null;
			try {
				Genrepreference.List query = mService.genrepreference().list(
						params[0]);
				preferences = query.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed loading " + e);
			}
			return preferences;
		}
	}

	private static class InsertGenrePreferenceTask
			extends
			AsyncTask<GenrePreferenceProtoGenreNameAccountKey, Void, GenrePreference> {

		@Override
		protected GenrePreference doInBackground(
				GenrePreferenceProtoGenreNameAccountKey... quotes) {
			GenrePreference returnedPreference = null;
			try {
				returnedPreference = mService.genrepreference()
						.insert(quotes[0].getAccountKey(), quotes[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed inserting " + e);
			}
			return returnedPreference;
		}
	}

	private static class DeleteGenrePreferenceTask extends
			AsyncTask<String, Void, GenrePreference> {

		@Override
		protected GenrePreference doInBackground(String... entityKeys) {
			GenrePreference returnedQuote = null;
			try {
				returnedQuote = mService.genrepreference()
						.delete(entityKeys[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed deleting " + e);
			}
			return returnedQuote;
		}
	}

	// ///////////////////////////////////////////////
	// Rating Preferences
	private static class QueryForRatingPreferencesTask extends
			AsyncTask<String, Void, RatingPreferenceCollection> {

		@Override
		protected RatingPreferenceCollection doInBackground(String... params) {
			RatingPreferenceCollection preferences = null;
			try {
				Ratingpreference.List query = mService.ratingpreference().list(
						params[0]);
				preferences = query.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed loading " + e);
			}
			return preferences;
		}
	}

	private static class InsertRatingPreferenceTask
			extends
			AsyncTask<RatingPreferenceProtoRatingNameAccountKey, Void, RatingPreference> {

		@Override
		protected RatingPreference doInBackground(
				RatingPreferenceProtoRatingNameAccountKey... quotes) {
			RatingPreference returnedPreference = null;
			try {
				returnedPreference = mService.ratingpreference()
						.insert(quotes[0].getAccountKey(), quotes[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed inserting " + e);
			}
			return returnedPreference;
		}
	}

	private static class DeleteRatingPreferenceTask extends
			AsyncTask<String, Void, RatingPreference> {

		@Override
		protected RatingPreference doInBackground(String... entityKeys) {
			RatingPreference returnedQuote = null;
			try {
				returnedQuote = mService.ratingpreference()
						.delete(entityKeys[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed deleting " + e);
			}
			return returnedQuote;
		}
	}

	// ///////////////////////////////////////////////
	// Platform Preferences
	private static class QueryForPlatformPreferencesTask extends
			AsyncTask<String, Void, PlatformPreferenceCollection> {

		@Override
		protected PlatformPreferenceCollection doInBackground(String... params) {
			PlatformPreferenceCollection preferences = null;
			try {
				Platformpreference.List query = mService.platformpreference()
						.list(params[0]);
				preferences = query.execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed loading " + e);
			}
			return preferences;
		}
	}

	private static class InsertPlatformPreferenceTask
			extends
			AsyncTask<PlatformPreferenceProtoPlatformNameAccountKey, Void, PlatformPreference> {

		@Override
		protected PlatformPreference doInBackground(
				PlatformPreferenceProtoPlatformNameAccountKey... quotes) {
			PlatformPreference returnedPreference = null;
			try {
				returnedPreference = mService.platformpreference()
						.insert(quotes[0].getAccountKey(), quotes[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed inserting " + e);
			}
			return returnedPreference;
		}
	}

	private static class DeletePlatformPreferenceTask extends
			AsyncTask<String, Void, PlatformPreference> {

		@Override
		protected PlatformPreference doInBackground(String... entityKeys) {
			PlatformPreference returnedQuote = null;
			try {
				returnedQuote = mService.platformpreference()
						.delete(entityKeys[0]).execute();
			} catch (IOException e) {
				Log.e("MOVIEMATCH", "Failed deleting " + e);
			}
			return returnedQuote;
		}
	}

}
