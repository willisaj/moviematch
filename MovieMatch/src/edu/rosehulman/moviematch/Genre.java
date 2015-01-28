package edu.rosehulman.moviematch;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable {
	
	private String mName;
	private int mId;
	
	public Genre(String name, int id) {
		mName = name;
		mId = id;
	}
	
	public Genre(Parcel source) {
		mName = source.readString();
		mId = source.readInt();
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

}
