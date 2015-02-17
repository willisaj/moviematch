package edu.rosehulman.moviematch;

import android.os.Parcel;
import android.os.Parcelable;

public class RatablePerson implements Parcelable {
	private String mName;
	private float rating;

	public RatablePerson(String name, float rating) {
		mName = name;
		this.rating = rating;
	}

	public RatablePerson(Parcel in) {
		mName = in.readString();
		rating = in.readFloat();
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeFloat(rating);
	}

	public static final Parcelable.Creator<RatablePerson> CREATOR = new Parcelable.Creator<RatablePerson>() {
		public RatablePerson createFromParcel(Parcel in) {
			return new RatablePerson(in);
		}

		public RatablePerson[] newArray(int size) {
			return new RatablePerson[size];
		}
	};

}
