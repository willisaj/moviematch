package edu.rosehulman.moviematch;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MPAAAdapter extends BaseAdapter {
	ArrayList<MPAA> mMPAAs;
	private Context mContext;

	public MPAAAdapter(Context context, ArrayList<MPAA> mpaaList) {
		mContext = context;

		// if (genreList == null) {
		// mGenres = new ArrayList<Genre>();
		// } else {
		mMPAAs = mpaaList;
		// }
	}

	@Override
	public int getCount() {
		return mMPAAs.size();
	}

	@Override
	public Object getItem(int position) {
		return mMPAAs.get(position);
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
		if (mMPAAs.get(position).getPreference() == 1) {
			cv.setSelected();
		} else {
			cv.setUnselected();
		}
		cv.setName(mMPAAs.get(position).getName());
		return cv;
	}

	public ArrayList<? extends Parcelable> getArrayList() {
		return mMPAAs;
	}

}
