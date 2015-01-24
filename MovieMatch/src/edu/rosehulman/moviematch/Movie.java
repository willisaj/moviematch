package edu.rosehulman.moviematch;

import java.util.List;

public class Movie {
	
	private String mTitle;
	private String mDirector;
	private List<String> mActors;
	private List<String> mGenres;
	
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

}
