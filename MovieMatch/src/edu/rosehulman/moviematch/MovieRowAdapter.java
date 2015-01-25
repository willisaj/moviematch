package edu.rosehulman.moviematch;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MovieRowAdapter extends BaseAdapter {
	ArrayList<Movie> mMovies;
	private Context mContext;

	public MovieRowAdapter(Context context, ArrayList<Movie> list) {

		mContext = context;
		if (list == null) {
			mMovies = new ArrayList<Movie>();
		} else {
			mMovies = list;
		}
	}

	@Override
	public int getCount() {
		return mMovies.size();
	}

	@Override
	public Object getItem(int position) {
		return mMovies.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MovieView movieView;
		if (convertView == null) {
			// make one
			movieView = new MovieView(mContext);
		} else {
			// use the one passed in
			movieView = (MovieView) convertView;
		}
		// customize it
		movieView.setData(mMovies.get(position).getTitle(),
				mMovies.get(position).getSummaryString(), mMovies.get(position)
						.getImageAddress());
		return movieView;
	}

	public void addView(Movie movie) {
		mMovies.add(movie);
	}

	public void deleteView(int index) {
		mMovies.remove(index);

	}

	public ArrayList<? extends Parcelable> getArrayList() {
		return mMovies;
	}

}
