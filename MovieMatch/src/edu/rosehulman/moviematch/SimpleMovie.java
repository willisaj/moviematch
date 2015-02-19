package edu.rosehulman.moviematch;

public class SimpleMovie {
	private String mName;
	private int mID;

	public SimpleMovie(int id, String name) {
		mID = id;
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public int getId() {
		return mID;
	}

	@Override
	public String toString() {
		return mName;
	}
}
