package edu.rosehulman.moviematch;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PlatformAdapter extends BaseAdapter {
	ArrayList<Platform> mPlatforms;
	private Context mContext;

	public PlatformAdapter(Context context, ArrayList<Platform> platformList) {
		mContext = context;

		// if (genreList == null) {
		// mGenres = new ArrayList<Genre>();
		// } else {
		mPlatforms = platformList;
		// }
	}

	@Override
	public int getCount() {
		return mPlatforms.size();
	}

	@Override
	public Object getItem(int position) {
		return mPlatforms.get(position);
	}

	@Override
	public long getItemId(int position) {
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
		if (mPlatforms.get(position).getPreference() == 1) {
			cv.setSelected();
		} else {
			cv.setUnselected();
		}
		cv.setName(mPlatforms.get(position).getName());
		return cv;
	}

	public ArrayList<? extends Parcelable> getArrayList() {
		return mPlatforms;
	}

}