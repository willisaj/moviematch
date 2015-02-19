package edu.rosehulman.moviematch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

public class UserMergeView extends LinearLayout {
	private EditText mEditText;

	public UserMergeView(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.user_merge_view, this);

		mEditText = (EditText) findViewById(R.id.mergeDialogEditText);
	}

	public String getUserName() {
		return mEditText.getText().toString();
	}

}
