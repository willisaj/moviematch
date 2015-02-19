package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Preferences {
	
	private List<RatablePerson> mActors;
	private List<RatablePerson> mDirectors;
	private List<Genre> mGenres;
	
	public Preferences() {
		mActors = new ArrayList<RatablePerson>();
		mDirectors = new ArrayList<RatablePerson>();
		mGenres = new ArrayList<Genre>();
	}
	
	public static Preferences getPreferencesForUser(String userName) {
		Preferences preferences = new Preferences();
		try {
			preferences.setActors(BackendApi.getRatedActorsForUser(userName));
			preferences.setDirectors(BackendApi.getRatedDirectorsForUser(userName));
			preferences.setGenres(BackendApi.getRatedGenresForUser(userName));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: Handle these errors better
		
		return preferences;
	}
	
	public boolean containGenres(Genre genre) {
		for (Genre g : mGenres) {
			if (g.getName().equals(genre.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public List<RatablePerson> getActors() {
		return mActors;
	}
	
	public void addActor(RatablePerson actor) {
		mActors.add(actor);
	}
	
	public void setActors(List<RatablePerson> actors) {
		mActors = actors;
	}
	
	public List<RatablePerson> getDirectors() {
		return mDirectors;
	}
	
	public void addDirector(RatablePerson director) {
		mDirectors.add(director);
	}
	
	public void setDirectors(List<RatablePerson> directors) {
		mDirectors = directors;
	}
	
	public List<Genre> getGenres() {
		return mGenres;
	}
	
	public void addGenre(Genre genre) {
		mGenres.add(genre);
	}
	
	public void setGenres(List<Genre> genres) {
		mGenres = genres;
	}

}
