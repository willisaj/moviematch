package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

		@Override
		public Movie createFromParcel(Parcel source) {
			return new Movie(source);
		}

		@Override
		public Movie[] newArray(int size) {
			return new Movie[size];
		}

	};

	private String mTitle;
	private String mDirector;
	private List<String> mActors;
	private List<Genre> mGenres;
	
	private String mDescription;
	private String mTagline;
	//Duration in minutes
	private int mDuration;
	private String mMPAARating;
	
	private int mReleaseYear;

	private String mPosterUrl;
	
	private String mRTRating;
	private int mRTScore;

	public Movie(String title) {
		this.mTitle = title;
	}

	public Movie(String title, String director, List<String> actors) {
		mTitle = title;
		mDirector = director;
		mActors = actors;
	}
	
	protected Movie(Parcel in) {
		mTitle = in.readString();
		mDirector = in.readString();
		
		mActors = new ArrayList<String>();
		in.readStringList(mActors);
		
		mGenres = in.createTypedArrayList(Genre.CREATOR);
		mDescription = in.readString();
		mTagline = in.readString();
		mDuration = in.readInt();
		mReleaseYear = in.readInt();
		mPosterUrl = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeString(mDirector);
		dest.writeStringList(mActors);
		dest.writeTypedList(mGenres);
		dest.writeString(mDescription);
		dest.writeString(mTagline);
		dest.writeInt(mDuration);
		dest.writeInt(mReleaseYear);
		dest.writeString(mPosterUrl);
	}

	public String getSummaryString() {
		StringBuilder summary = new StringBuilder();

		for (int i = 0; i < 2 && i < mGenres.size(); i++) {
			summary.append(mGenres.get(i));
			summary.append(" - ");
		}
		summary.append(mDirector);

		for (int i = 0; i < 2 && i < mActors.size(); i++) {
			summary.append(" - ");
			summary.append(mActors.get(i));

		}
		return summary.toString();
	}

	public String getImageAddress() {
		// TODO figure out how to implement
		return null;
	}

	// public class MovieCreator implements Parcelable.Creator<Movie> {
	//
	// @Override
	// public Movie createFromParcel(Parcel source) {
	// return new Movie(source);
	// }
	//
	// @Override
	// public Movie[] newArray(int size) {
	// return new Movie[size];
	// }
	//
	// }
	
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getDirector() {
		return mDirector;
	}

	public void setDirector(String director) {
		this.mDirector = director;
	}

	public List<String> getActors() {
		return mActors;
	}

	public void setActors(List<String> actors) {
		this.mActors = actors;
	}

	public List<Genre> getGenres() {
		return mGenres;
	}

	public void setGenres(List<Genre> genres) {
		this.mGenres = genres;
	}
	
	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getTagline() {
		return mTagline;
	}

	public void setTagline(String tagline) {
		this.mTagline = tagline;
	}

	public int getDuration() {
		return mDuration;
	}

	public void setDuration(int duration) {
		this.mDuration = duration;
	}
	
	public String getMPAARating() {
		return mMPAARating;
	}

	public void setMPAARating(String mpaaRating) {
		this.mMPAARating = mpaaRating;
	}
	
	public int getReleaseYear() {
		return mReleaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.mReleaseYear = releaseYear;
	}
	
	public String getPosterUrl() {
		return this.mPosterUrl;
	}
	
	public void setPosterUrl(String posterUrl) {
		this.mPosterUrl = posterUrl;
	}
	
	public String getRTRating() {
		return mRTRating;
	}

	public void setRTRating(String rtRating) {
		this.mRTRating = rtRating;
	}

	public int getRTScore() {
		return mRTScore;
	}

	public void setRTScore(int rtScore) {
		this.mRTScore = rtScore;
	}

}
