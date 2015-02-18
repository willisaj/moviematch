package edu.rosehulman.moviematch;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PreferencesActivity extends Activity {
	private RatableRowAdapter actorAdapter;
	private RatableRowAdapter directorAdapter;
	private ArrayAdapter<String> selectedGenreAdapter;
	private ArrayList<Genre> genreList;
	private MovieDataAdapter mMovieDataAdapter;
	private ArrayList<String> selectedGenreList;
	private ArrayList<MPAA> mpaaList;
	private ArrayList<String> selectedMPAAList;
	private ArrayAdapter<String> selectedMPAAAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);

		mMovieDataAdapter = new MovieDataAdapter(this);
		mMovieDataAdapter.open();

		genreList = new ArrayList<Genre>();
		mMovieDataAdapter.readGenres(genreList);
		if (genreList.isEmpty()) {
			genreList = (ArrayList<Genre>) new TMDBMovieRecommender()
					.getGenres();
			mMovieDataAdapter.addGenres(genreList);
		}

		selectedGenreList = new ArrayList<String>();
		for (Genre genre : genreList) {
			if (genre.getPreference() == 1) {
				selectedGenreList.add(genre.getName());
			}
		}

		mpaaList = new ArrayList<MPAA>();
		mMovieDataAdapter.readMPAAs(mpaaList);
		if (mpaaList.isEmpty()) {
			mpaaList = new ArrayList<MPAA>();
			mpaaList.add(new MPAA("G", 0));
			mpaaList.add(new MPAA("PG", 0));
			mpaaList.add(new MPAA("PG-13", 0));
			mpaaList.add(new MPAA("R", 0));
			mpaaList.add(new MPAA("NC-17", 0));
		}

		selectedMPAAList = new ArrayList<String>();
		for (MPAA mpaa : mpaaList) {
			if (mpaa.getPreference() == 1) {
				selectedMPAAList.add(mpaa.getName());
			}
		}

		ArrayList<RatablePerson> actorList = null;

		NonScrollListView actorListView = (NonScrollListView) findViewById(R.id.actor_mood_list_view);
		actorAdapter = new RatableRowAdapter(this, actorList);
		actorListView.setAdapter(actorAdapter);

		ArrayList<RatablePerson> directorList = null;
		AbsListView directorListView = (AbsListView) findViewById(R.id.director_mood_list_view);
		directorAdapter = new RatableRowAdapter(this, directorList);
		directorListView.setAdapter(directorAdapter);

		// TODO update values on changing the stars and delete on long click.

		directorListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						removeDirector(position);
						return false;
					}
				});

		actorListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				removeActor(position);
				return true;
			}
		});

		Button addActorButton = (Button) findViewById(R.id.addActorButton);
		Button addDirectorButton = (Button) findViewById(R.id.addDirectorButton);

		OnClickListener addRatableListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.addActorButton) {
					showAddActorDirectorDialog(true);
				} else if (v.getId() == R.id.addDirectorButton) {
					showAddActorDirectorDialog(false);
				} else {
					Log.d("ERROR",
							"ERROR: something else clicked the ratableListener?");
				}

			}
		};

		addActorButton.setOnClickListener(addRatableListener);
		addDirectorButton.setOnClickListener(addRatableListener);

		AbsListView selectedGenreListView = (AbsListView) findViewById(R.id.genre_mood_list_view);
		selectedGenreAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, selectedGenreList);
		selectedGenreListView.setAdapter(selectedGenreAdapter);

		Button editGenresButton = (Button) findViewById(R.id.editGenresButton);
		editGenresButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEditGenresDialog();
			}
		});

		AbsListView selectedMPAAListView = (AbsListView) findViewById(R.id.mpaa_mood_list_view);
		selectedMPAAAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, selectedMPAAList);
		selectedMPAAListView.setAdapter(selectedMPAAAdapter);

		Button editMPAAButton = (Button) findViewById(R.id.editMPAAsButton);
		editMPAAButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEditMPAADialog();
			}
		});

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.preferences, menu);
	// return true;
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	protected void showEditMPAADialog() {
		DialogFragment df = new DialogFragment() {
			private ListView mpaaListView;

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// set options
				builder.setTitle(R.string.edit_mpaa_title);

				mpaaListView = new ListView(getActivity());
				MPAAAdapter mpaaAdapter = new MPAAAdapter(getActivity(),
						mpaaList);
				mpaaListView.setAdapter(mpaaAdapter);

				mpaaListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						((CheckableView) view).toggle();
					}
				});

				builder.setView(mpaaListView);

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								selectedMPAAList.clear();

								for (int i = 0; i < mpaaListView
										.getChildCount(); i++) {

									if (((CheckableView) mpaaListView
											.getChildAt(i)).isSelected()) {
										(mpaaList.get(i)).setPreference(1);
										selectedMPAAList.add(mpaaList.get(i)
												.getName());
									} else {
										(mpaaList.get(i)).setPreference(0);
									}
								}

								updateMPAAs();

								dialog.dismiss();

							}

						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				return builder.create();
			}

		};

		df.show(getFragmentManager(), "editing MPAAs");

	}

	protected void showEditGenresDialog() {
		DialogFragment df = new DialogFragment() {
			private ListView genreListView;

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// set options
				builder.setTitle(R.string.edit_genre_title);

				genreListView = new ListView(getActivity());
				GenreAdapter genreAdapter = new GenreAdapter(getActivity(),
						genreList);
				genreListView.setAdapter(genreAdapter);

				genreListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						((CheckableView) view).toggle();
					}
				});

				builder.setView(genreListView);

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								selectedGenreList.clear();

								for (int i = 0; i < genreListView
										.getChildCount(); i++) {

									if (((CheckableView) genreListView
											.getChildAt(i)).isSelected()) {
										(genreList.get(i)).setPreference(1);
										selectedGenreList.add(genreList.get(i)
												.getName());
									} else {
										(genreList.get(i)).setPreference(0);
									}
								}

								updateGenres();

								dialog.dismiss();

							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				return builder.create();
			}

		};

		df.show(getFragmentManager(), "editing genres");

	}

	protected void removeDirector(int position) {
		directorAdapter.deleteView(position);
		directorAdapter.notifyDataSetChanged();
	}

	protected void removeActor(int position) {
		actorAdapter.deleteView(position);
		actorAdapter.notifyDataSetChanged();
	}

	protected void showAddActorDirectorDialog(boolean isActor) {
		final boolean isAct = isActor;

		DialogFragment df = new DialogFragment() {
			private PersonRaterView pv;

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				pv = new PersonRaterView(getActivity());

				if (isAct) {
					pv.setHint(R.string.actor_hint);
				} else {
					pv.setHint(R.string.director_hint);
				}

				if (isAct) {
					builder.setTitle(R.string.actor_add_dialog);
				} else {
					builder.setTitle(R.string.director_add_dialog);
				}

				builder.setView(pv);

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								addItem();
								dialog.dismiss();

							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				return builder.create();
			}

			private void addItem() {

				float rating = pv.getRating();
				String name = pv.getName();

				if (!name.equals("")) {
					addPerson(new RatablePerson(name, rating), isAct);
				} else {
					Toast.makeText(getActivity(),
							"You didn't enter in a name, silly!\n  Try again.",
							Toast.LENGTH_SHORT).show();

				}

			}

		};
		String tag;
		if (isAct) {
			tag = "add actor dialog";
		} else {
			tag = "add director dialog";
		}
		df.show(getFragmentManager(), tag);

	}

	protected void addPerson(RatablePerson person, boolean isActor) {
		if (isActor) {
			actorAdapter.addView(person);
			actorAdapter.notifyDataSetChanged();
		} else {
			directorAdapter.addView(person);
			directorAdapter.notifyDataSetChanged();
		}
	}

	protected void updateGenres() {
		selectedGenreAdapter.notifyDataSetChanged();
		for (Genre genre : genreList) {
			mMovieDataAdapter.updateGenre(genre);
		}
	}

	private void updateMPAAs() {
		// TODO Auto-generated method stub

	}
}
