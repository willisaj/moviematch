package edu.rosehulman.moviematch;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RatableRowAdapter extends BaseAdapter {
	ArrayList<RatablePerson> mPeople;
	private Context mContext;

	public RatableRowAdapter(Context context, ArrayList<RatablePerson> actorList) {
		mContext = context;

		if (actorList == null) {
			mPeople = new ArrayList<RatablePerson>();
		} else {
			mPeople = actorList;
		}

	}

	@Override
	public int getCount() {
		return mPeople.size();
	}

	@Override
	public Object getItem(int position) {
		return mPeople.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RatableView rv;
		if (convertView == null) {
			// make one
			rv = new RatableView(mContext);
		} else {
			// use the one passed in
			rv = (RatableView) convertView;
		}
		// customize it
		rv.setText(mPeople.get(position).getName());
		rv.setRating(mPeople.get(position).getRating());
		return rv;
	}

	public void addView(RatablePerson person) {
		mPeople.add(person);
	}

	public void deleteView(int index) {
		mPeople.remove(index);

	}

	public ArrayList<? extends Parcelable> getArrayList() {
		return mPeople;
	}

}
