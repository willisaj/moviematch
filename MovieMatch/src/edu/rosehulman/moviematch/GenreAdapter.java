package edu.rosehulman.moviematch;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GenreAdapter extends BaseAdapter {
	ArrayList<Genre> mGenres;
	private Context mContext;

	public GenreAdapter(Context context, ArrayList<Genre> genreList) {
		mContext = context;

		// if (genreList == null) {
		// mGenres = new ArrayList<Genre>();
		// } else {
		mGenres = genreList;
		// }
	}

	@Override
	public int getCount() {
		return mGenres.size();
	}

	@Override
	public Object getItem(int position) {
		return mGenres.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckableView cv;
		if (convertView == null) {
			// make one
			cv = new CheckableView(mContext);
		} else {
			// use the one passed in
			cv = (CheckableView) convertView;
		}
		// customize it
		if (mGenres.get(position).getPreference() == 1) {
			cv.setSelected();
		}else{
			cv.setUnselected();
		}
		cv.setName(mGenres.get(position).getName());
		return cv;
	}

	public ArrayList<? extends Parcelable> getArrayList() {
		return mGenres;
	}

}
