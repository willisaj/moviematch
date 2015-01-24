package edu.rosehulman.moviematch;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

	private String mTitle;
	private String mDirector;
	private List<String> mActors;
	private List<String> mGenres;

	private Movie(Parcel in) {
		mTitle = in.readString();
		mDirector = in.readString();
		// mActors = in.readArray(loader)
	}

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

	public List<String> getGenres() {
		return mGenres;
	}

	public void setGenres(List<String> genres) {
		this.mGenres = genres;
	}

	public Movie(String title) {
		this.mTitle = title;
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
		dest.writeArray(mGenres.toArray());
		dest.writeArray(mActors.toArray());

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

}
