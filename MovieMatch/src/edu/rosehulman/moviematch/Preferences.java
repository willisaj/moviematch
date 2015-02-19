package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.List;

public class Preferences {
	
	private List<RatablePerson> mActors;
	private List<RatablePerson> mDirectors;
	
	public Preferences() {
		mActors = new ArrayList<RatablePerson>();
		mDirectors = new ArrayList<RatablePerson>();
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

}
