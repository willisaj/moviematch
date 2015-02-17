package edu.rosehulman.moviematch;

public class Platform {

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
}
