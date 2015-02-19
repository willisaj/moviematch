package edu.rosehulman.moviematch;

import android.os.Parcel;
import android.os.Parcelable;

public class Platform implements Parcelable {

	private int mPreference;
	private String mName;

	public Platform(String name, int preference) {
		mName = name;
		mPreference = preference;
	}

	public int getPreference() {
		return mPreference;
	}

	public String getName() {
		return mName;
	}

	public void setPreference(int pref) {
		mPreference = pref;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeInt(mPreference);

	}

	public Platform(Parcel source) {
		mName = source.readString();
		mPreference = source.readInt();
	}

	public static final Parcelable.Creator<Platform> CREATOR = new Parcelable.Creator<Platform>() {
		public Platform createFromParcel(Parcel in) {
			return new Platform(in);
		}

		public Platform[] newArray(int size) {
			return new Platform[size];
		}
	};

}
