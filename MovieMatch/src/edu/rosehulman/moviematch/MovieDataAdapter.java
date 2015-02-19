package edu.rosehulman.moviematch;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieDataAdapter {

	// Becomes the filename of the database
	private static final String DATABASE_NAME = "movies.db";

	private static final String SEEN_MOVIES_TABLE_NAME = "seenmovies";
	private static final String ACTORS_TABLE_NAME = "actors";
	private static final String DIRECTORS_TABLE_NAME = "directors";
	private static final String GENRES_TABLE_NAME = "genres";
	private static final String PLATFORMS_TABLE_NAME = "platforms";
	private static final String MPAA_TABLE_NAME = "mpaas";
	private static final String WISHLIST_TABLE_NAME = "wishlist";

	// We increment this every time we change the database schema which will
	// kick off an automatic upgrade
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_ACTOR_NAME = "name";
	public static final String KEY_ACTOR_RATING = "rating";

	public static final String KEY_DIRECTOR_NAME = "name";
	public static final String KEY_DIRECTOR_RATING = "rating";

	public static final String KEY_GENRE_ID = "_id";
	public static final String KEY_GENRE_NAME = "name";
	public static final String KEY_GENRE_PREFERENCE = "preference";

	public static final String KEY_MPAA_NAME = "name";
	public static final String KEY_MPAA_PREFERENCE = "preference";

	public static final String KEY_PLATFORM_NAME = "name";
	public static final String KEY_PLATFORM_PREFERENCE = "preference";

	public static final String KEY_SEEN_MOVIES_ID = "_id";
	public static final String KEY_SEEN_MOVIES_NAME = "name";

	public static final String KEY_WISHLIST_ID = "_id";
	public static final String KEY_WISHLIST_NAME = "name";

	private static String DROP_ACTORS_STATEMENT = "DROP TABLE IF EXISTS "
			+ ACTORS_TABLE_NAME;
	private static String DROP_DIRECTORS_STATEMENT = "DROP TABLE IF EXISTS "
			+ DIRECTORS_TABLE_NAME;
	private static String DROP_GENRES_STATEMENT = "DROP TABLE IF EXISTS "
			+ GENRES_TABLE_NAME;
	private static String DROP_MPAA_STATEMENT = "DROP TABLE IF EXISTS "
			+ MPAA_TABLE_NAME;
	private static String DROP_PLATFORMS_STATEMENT = "DROP TABLE IF EXISTS "
			+ PLATFORMS_TABLE_NAME;
	private static String DROP_SEEN_MOVIES_STATEMENT = "DROP TABLE IF EXISTS "
			+ SEEN_MOVIES_TABLE_NAME;
	private static String DROP_WISHLIST_STATEMENT = "DROP TABLE IF EXISTS "
			+ WISHLIST_TABLE_NAME;

	private static String CREATE_ACTORS_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(ACTORS_TABLE_NAME);
		sb.append(" (");
		sb.append(KEY_ACTOR_NAME);
		sb.append(" text, ");
		sb.append(KEY_ACTOR_RATING);
		sb.append(" real ");
		sb.append(")");
		CREATE_ACTORS_STATEMENT = sb.toString();
	}

	private static String CREATE_DIRECTORS_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(DIRECTORS_TABLE_NAME);
		sb.append(" (");
		sb.append(KEY_DIRECTOR_NAME);
		sb.append(" text, ");
		sb.append(KEY_DIRECTOR_RATING);
		sb.append(" real ");
		sb.append(")");
		CREATE_DIRECTORS_STATEMENT = sb.toString();
	}

	private static String CREATE_GENRES_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(GENRES_TABLE_NAME);
		sb.append(" (");
		sb.append(KEY_GENRE_ID);
		sb.append(" int, ");
		sb.append(KEY_GENRE_NAME);
		sb.append(" text, ");
		sb.append(KEY_GENRE_PREFERENCE);
		sb.append(" int ");
		sb.append(")");
		CREATE_GENRES_STATEMENT = sb.toString();
	}

	private static String CREATE_MPAA_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(MPAA_TABLE_NAME);
		sb.append(" (");
		sb.append(KEY_MPAA_NAME);
		sb.append(" text, ");
		sb.append(KEY_MPAA_PREFERENCE);
		sb.append(" int ");
		sb.append(")");
		CREATE_MPAA_STATEMENT = sb.toString();
	}

	private static String CREATE_PLATFORMS_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(PLATFORMS_TABLE_NAME);
		sb.append(" (");
		sb.append(KEY_PLATFORM_NAME);
		sb.append(" text, ");
		sb.append(KEY_PLATFORM_PREFERENCE);
		sb.append(" int ");
		sb.append(")");
		CREATE_PLATFORMS_STATEMENT = sb.toString();
	}

	private static String CREATE_SEEN_MOVIES_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(SEEN_MOVIES_TABLE_NAME);
		sb.append(" (");
		sb.append(KEY_SEEN_MOVIES_ID);
		sb.append(" int, ");
		sb.append(KEY_SEEN_MOVIES_NAME);
		sb.append(" text ");
		sb.append(")");
		CREATE_SEEN_MOVIES_STATEMENT = sb.toString();
	}

	private static String CREATE_WISHLIST_STATEMENT;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(WISHLIST_TABLE_NAME);
		sb.append(" (");
		sb.append(KEY_WISHLIST_ID);
		sb.append(" int, ");
		sb.append(KEY_WISHLIST_NAME);
		sb.append(" text ");
		sb.append(")");
		CREATE_WISHLIST_STATEMENT = sb.toString();
	}

	private static class MovieDBHelper extends SQLiteOpenHelper {
		private Context context;

		public MovieDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_ACTORS_STATEMENT);
			db.execSQL(CREATE_DIRECTORS_STATEMENT);
			db.execSQL(CREATE_GENRES_STATEMENT);
			db.execSQL(CREATE_MPAA_STATEMENT);
			db.execSQL(CREATE_PLATFORMS_STATEMENT);
			db.execSQL(CREATE_SEEN_MOVIES_STATEMENT);
			db.execSQL(CREATE_WISHLIST_STATEMENT);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d("UPDATE", "Upgrading version from " + oldVersion + "to"
					+ newVersion + "!!!");
			db.execSQL(DROP_ACTORS_STATEMENT);
			db.execSQL(DROP_DIRECTORS_STATEMENT);
			db.execSQL(DROP_GENRES_STATEMENT);
			db.execSQL(DROP_MPAA_STATEMENT);
			db.execSQL(DROP_PLATFORMS_STATEMENT);
			db.execSQL(DROP_SEEN_MOVIES_STATEMENT);
			db.execSQL(DROP_WISHLIST_STATEMENT);
			onCreate(db);

		}

		public void clearPreferences(SQLiteDatabase db) {
			db.execSQL(DROP_ACTORS_STATEMENT);
			db.execSQL(DROP_DIRECTORS_STATEMENT);
			db.execSQL(DROP_GENRES_STATEMENT);
			db.execSQL(DROP_MPAA_STATEMENT);
			db.execSQL(DROP_PLATFORMS_STATEMENT);

			db.execSQL(CREATE_ACTORS_STATEMENT);
			db.execSQL(CREATE_DIRECTORS_STATEMENT);
			db.execSQL(CREATE_GENRES_STATEMENT);
			db.execSQL(CREATE_MPAA_STATEMENT);
			db.execSQL(CREATE_PLATFORMS_STATEMENT);
		}

		public void clearCheckablePreferences(SQLiteDatabase db) {
			db.execSQL(DROP_GENRES_STATEMENT);
			db.execSQL(DROP_MPAA_STATEMENT);
			db.execSQL(DROP_PLATFORMS_STATEMENT);
			db.execSQL(CREATE_GENRES_STATEMENT);
			db.execSQL(CREATE_MPAA_STATEMENT);
			db.execSQL(CREATE_PLATFORMS_STATEMENT);

		}

	}

	private SQLiteOpenHelper mOpenHelper;
	private SQLiteDatabase mDatabase;

	public MovieDataAdapter(Context context) {
		mOpenHelper = new MovieDBHelper(context);
	}

	public void open() {
		mDatabase = mOpenHelper.getWritableDatabase();
	}

	public void close() {
		mDatabase.close();
	}

	public void clearPreferences() {
		((MovieDBHelper) mOpenHelper).clearPreferences(mDatabase);
	}

	public void addActor(RatablePerson actor) {
		ContentValues row = getContentValuesFromActor(actor);
		mDatabase.insert(ACTORS_TABLE_NAME, null, row);
	}

	public void readActors(ArrayList<RatablePerson> mActors) {
		mActors.clear();
		Cursor cursor = getActorsCursor();
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			mActors.add(getActorFromCursor(cursor));
		} while (cursor.moveToNext());
	}

	public void updateActor(RatablePerson actor) {
		ContentValues row = getContentValuesFromActor(actor);
		String selection = KEY_ACTOR_NAME + " = '" + actor.getName() + "'";
		// row is row to add, selection is rows to replace with row
		mDatabase.update(ACTORS_TABLE_NAME, row, selection, null);
	}

	public boolean removeActor(RatablePerson actor) {

		String selection = KEY_ACTOR_NAME + " = ?";
		return mDatabase.delete(ACTORS_TABLE_NAME, selection,
				new String[] { actor.getName() }) > 0;
	}

	public ContentValues getContentValuesFromActor(RatablePerson actor) {
		ContentValues row = new ContentValues();
		row.put(KEY_ACTOR_NAME, actor.getName());
		row.put(KEY_ACTOR_RATING, actor.getRating());
		return row;
	}

	public Cursor getActorsCursor() {
		return mDatabase.query(ACTORS_TABLE_NAME, null, null, null, null, null,
				null);
	}

	public RatablePerson getActorFromCursor(Cursor cursor) {
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_ACTOR_NAME));
		float rating = cursor.getFloat(cursor
				.getColumnIndexOrThrow(KEY_ACTOR_RATING));
		return new RatablePerson(name, rating);
	}

	public void addDirector(RatablePerson director) {
		ContentValues row = getContentValuesFromDirector(director);
		mDatabase.insert(DIRECTORS_TABLE_NAME, null, row);
	}

	public void readDirectors(ArrayList<RatablePerson> mDirectors) {
		mDirectors.clear();
		Cursor cursor = getDirectorsCursor();
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			mDirectors.add(getDirectorFromCursor(cursor));
		} while (cursor.moveToNext());
	}

	public void updateDirector(RatablePerson director) {
		ContentValues row = getContentValuesFromDirector(director);
		String selection = KEY_DIRECTOR_NAME + " = '" + director.getName()
				+ "'";
		// row is row to add, selection is rows to replace with row
		mDatabase.update(DIRECTORS_TABLE_NAME, row, selection, null);
	}

	public boolean removeDirector(RatablePerson director) {

		String selection = KEY_DIRECTOR_NAME + " = ?";
		return mDatabase.delete(DIRECTORS_TABLE_NAME, selection,
				new String[] { director.getName() }) > 0;
	}

	public ContentValues getContentValuesFromDirector(RatablePerson director) {
		ContentValues row = new ContentValues();
		row.put(KEY_DIRECTOR_NAME, director.getName());
		row.put(KEY_DIRECTOR_RATING, director.getRating());
		return row;
	}

	public Cursor getDirectorsCursor() {
		return mDatabase.query(DIRECTORS_TABLE_NAME, null, null, null, null,
				null, null);
	}

	public RatablePerson getDirectorFromCursor(Cursor cursor) {
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_DIRECTOR_NAME));
		float rating = cursor.getFloat(cursor
				.getColumnIndexOrThrow(KEY_DIRECTOR_RATING));
		return new RatablePerson(name, rating);
	}

	public void addSeenMovie(SimpleMovie movie) {
		ContentValues row = getContentValuesFromSeenMovie(movie);
		mDatabase.insert(SEEN_MOVIES_TABLE_NAME, null, row);
	}

	public void readSeenMovies(ArrayList<SimpleMovie> mSeenMovies) {
		mSeenMovies.clear();
		Cursor cursor = getSeenMoviesCursor();
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			mSeenMovies.add(getSeenMovieFromCursor(cursor));
		} while (cursor.moveToNext());
	}

	public boolean removeSeenMovie(SimpleMovie movie) {

		String selection = KEY_SEEN_MOVIES_ID + " = ?";
		return mDatabase.delete(SEEN_MOVIES_TABLE_NAME, selection,
				new String[] { movie.getId() + "" }) > 0;
	}

	public ContentValues getContentValuesFromSeenMovie(SimpleMovie movie) {
		ContentValues row = new ContentValues();
		row.put(KEY_SEEN_MOVIES_ID, movie.getId());
		row.put(KEY_SEEN_MOVIES_NAME, movie.getName());
		return row;
	}

	public Cursor getSeenMoviesCursor() {
		return mDatabase.query(SEEN_MOVIES_TABLE_NAME, null, null, null, null,
				null, null);
	}

	public SimpleMovie getSeenMovieFromCursor(Cursor cursor) {
		int id = cursor
				.getInt(cursor.getColumnIndexOrThrow(KEY_SEEN_MOVIES_ID));
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_SEEN_MOVIES_NAME));
		return new SimpleMovie(id, name);
	}

	public void addWishMovie(SimpleMovie movie) {
		ContentValues row = getContentValuesFromWishMovie(movie);
		mDatabase.insert(WISHLIST_TABLE_NAME, null, row);
	}

	public void readWishMovies(ArrayList<SimpleMovie> mWishMovies) {
		mWishMovies.clear();
		Cursor cursor = getWishMoviesCursor();
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			mWishMovies.add(getWishMovieFromCursor(cursor));
		} while (cursor.moveToNext());
	}

	public boolean isOnWishList(int movieId) {
		ArrayList<SimpleMovie> tempMovies = new ArrayList<SimpleMovie>();
		Log.d("Printing wishlist", "Woot off!");
		readWishMovies(tempMovies);
		for (SimpleMovie movie : tempMovies) {
			Log.d("Printing wishlist", movie.getName());
			Log.d("Printing wishlist", "IDs: " + movie.getId() + "---"
					+ movieId);
			if (movie.getId() == movieId) {
				Log.d("Printing wishlist", "Found movie: " + movie.getName());

				return true;
			}
		}
		return false;
	}

	public boolean removeWishMovie(int movieID) {

		String selection = KEY_WISHLIST_ID + " = ?";
		return mDatabase.delete(WISHLIST_TABLE_NAME, selection,
				new String[] { movieID + "" }) > 0;
	}

	public ContentValues getContentValuesFromWishMovie(SimpleMovie movie) {
		ContentValues row = new ContentValues();
		row.put(KEY_WISHLIST_ID, movie.getId());
		row.put(KEY_WISHLIST_NAME, movie.getName());
		return row;
	}

	public Cursor getWishMoviesCursor() {
		return mDatabase.query(WISHLIST_TABLE_NAME, null, null, null, null,
				null, null);
	}

	public SimpleMovie getWishMovieFromCursor(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_WISHLIST_ID));
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_WISHLIST_NAME));
		return new SimpleMovie(id, name);
	}

	public void addGenres(ArrayList<Genre> genres) {
		for (Genre genre : genres) {
			ContentValues row = getContentValuesFromGenre(genre);
			mDatabase.insert(GENRES_TABLE_NAME, null, row);
		}
	}

	public void readGenres(ArrayList<Genre> genres) {
		genres.clear();
		Cursor cursor = getGenresCursor();
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			genres.add(getGenreFromCursor(cursor));
		} while (cursor.moveToNext());
	}

	public void updateGenre(Genre genre) {
		ContentValues row = getContentValuesFromGenre(genre);
		String selection = KEY_GENRE_ID + " = " + genre.getId();
		// row is row to add, selection is rows to replace with row
		mDatabase.update(GENRES_TABLE_NAME, row, selection, null);
	}

	public ContentValues getContentValuesFromGenre(Genre genre) {
		ContentValues row = new ContentValues();
		row.put(KEY_GENRE_ID, genre.getId());
		row.put(KEY_GENRE_NAME, genre.getName());
		row.put(KEY_GENRE_PREFERENCE, genre.getPreference());
		return row;
	}

	public Cursor getGenresCursor() {
		return mDatabase.query(GENRES_TABLE_NAME, null, null, null, null, null,
				null);
	}

	public Genre getGenreFromCursor(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_GENRE_ID));
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_GENRE_NAME));
		int preference = cursor.getInt(cursor
				.getColumnIndexOrThrow(KEY_GENRE_PREFERENCE));
		return new Genre(name, id, preference);
	}

	public void addMPAAs(ArrayList<MPAA> MPAAs) {
		for (MPAA rating : MPAAs) {
			ContentValues row = getContentValuesFromMPAA(rating);
			mDatabase.insert(MPAA_TABLE_NAME, null, row);
		}
	}

	public void readMPAAs(ArrayList<MPAA> MPAAs) {
		MPAAs.clear();
		Cursor cursor = getMPAAsCursor();
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			MPAAs.add(getMPAAFromCursor(cursor));
		} while (cursor.moveToNext());
	}

	public void updateMPAA(MPAA rating) {
		ContentValues row = getContentValuesFromMPAA(rating);
		String selection = KEY_MPAA_NAME + " = '" + rating.getName() + "'";
		// row is row to add, selection is rows to replace with row
		mDatabase.update(MPAA_TABLE_NAME, row, selection, null);
	}

	public ContentValues getContentValuesFromMPAA(MPAA rating) {
		ContentValues row = new ContentValues();
		row.put(KEY_MPAA_NAME, rating.getName());
		row.put(KEY_MPAA_PREFERENCE, rating.getPreference());
		return row;
	}

	public Cursor getMPAAsCursor() {
		return mDatabase.query(MPAA_TABLE_NAME, null, null, null, null, null,
				null);
	}

	public MPAA getMPAAFromCursor(Cursor cursor) {
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_MPAA_NAME));
		int preference = cursor.getInt(cursor
				.getColumnIndexOrThrow(KEY_MPAA_PREFERENCE));
		return new MPAA(name, preference);
	}

	public void addPlatforms(ArrayList<Platform> platforms) {
		for (Platform platform : platforms) {
			ContentValues row = getContentValuesFromPlatform(platform);
			mDatabase.insert(PLATFORMS_TABLE_NAME, null, row);
		}
	}

	public void readPlatforms(ArrayList<Platform> platforms) {
		platforms.clear();
		Cursor cursor = getPlatformsCursor();
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			platforms.add(getPlatformFromCursor(cursor));
		} while (cursor.moveToNext());
	}

	public void updatePlatform(Platform platform) {
		ContentValues row = getContentValuesFromPlatform(platform);
		String selection = KEY_PLATFORM_NAME + " = '" + platform.getName()
				+ "'";
		// row is row to add, selection is rows to replace with row
		mDatabase.update(PLATFORMS_TABLE_NAME, row, selection, null);
	}

	public ContentValues getContentValuesFromPlatform(Platform platform) {
		ContentValues row = new ContentValues();
		row.put(KEY_PLATFORM_NAME, platform.getName());
		row.put(KEY_PLATFORM_PREFERENCE, platform.getPreference());
		return row;
	}

	public Cursor getPlatformsCursor() {
		return mDatabase.query(PLATFORMS_TABLE_NAME, null, null, null, null,
				null, null);
	}

	public Platform getPlatformFromCursor(Cursor cursor) {
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_PLATFORM_NAME));
		int preference = cursor.getInt(cursor
				.getColumnIndexOrThrow(KEY_PLATFORM_PREFERENCE));
		return new Platform(name, preference);
	}

	public void clearCheckablePreferences() {
		((MovieDBHelper) mOpenHelper).clearCheckablePreferences(mDatabase);

	}

}
