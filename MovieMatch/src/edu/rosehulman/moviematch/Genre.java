package edu.rosehulman.moviematch;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable {

	private String mName;
	private int mId;
	private int mPreference;

	@Deprecated
	public Genre(String name, int id) {
		mName = name;
		mId = id;
		mPreference = 0;
	}

	public Genre(String name, int id, int preference) {
		mName = name;
		mId = id;
		mPreference = preference;
	}

	public Genre(Parcel source) {
		mName = source.readString();
		mId = source.readInt();
		mPreference = source.readInt();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeInt(mId);
		dest.writeInt(mPreference);
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	@Override
	public String toString() {
		return mName;
	}

	public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
		public Genre createFromParcel(Parcel in) {
			return new Genre(in);
		}

		public Genre[] newArray(int size) {
			return new Genre[size];
		}
	};

	public int getPreference() {
		return mPreference;
	}

	public void setPreference(int preference) {
		mPreference = preference;
	}
}
