package edu.rosehulman.moviematch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.rosehulman.moviematch.R;

public class CheckableView extends RelativeLayout {

	private TextView mNameTextView;
	private boolean isSelected;

	public CheckableView(Context context) {
		super(context);
		isSelected = false;
		LayoutInflater.from(context).inflate(R.layout.view_checkable, this);

		mNameTextView = (TextView) findViewById(R.id.checkTitleTextView);

	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setName(String name) {
		mNameTextView.setText(name);
	}

	public void setSelected() {
		isSelected = true;
		this.setBackgroundResource(R.color.green);
	}

	public void toggle() {
		if (isSelected) {
			isSelected = false;
			this.setBackgroundResource(android.R.drawable.list_selector_background);
		} else {
			isSelected = true;
			this.setBackgroundResource(R.color.green);
		}
	}

	public String getName() {
		return mNameTextView.getText().toString();
	}

}
