package edu.rosehulman.moviematch;

import java.util.List;

public interface IMovieRecommendation {
	
	public List<Movie> getMovies();
	
	public IMovieRecommendation withActor(String actor);
	
	public IMovieRecommendation withActors(List<String> actors);
	
	public IMovieRecommendation withDirector(String director);

}
