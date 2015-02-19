package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.appspot.willisaj_movie_match.moviematch.model.Account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
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
	private ArrayAdapter<RatablePerson> actorAdapter;
	private ArrayAdapter<RatablePerson> directorAdapter;
	private ArrayAdapter<String> selectedGenreAdapter;
	private ArrayList<Genre> genreList;
	private MovieDataAdapter mMovieDataAdapter;
	private ArrayList<String> selectedGenreList;
	private ArrayList<MPAA> mpaaList;
	private ArrayList<String> selectedMPAAList;
	private ArrayAdapter<String> selectedMPAAAdapter;
	private ArrayList<Platform> platformList;
	private ArrayList<String> selectedPlatformList;
	private ArrayAdapter<String> selectedPlatformAdapter;
	private ArrayList<RatablePerson> actorList;
	private ArrayList<RatablePerson> directorList;
	private String userName = "willisaj";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);

		loadPreferences();

		configureListViews();

		configureButtons();

	}

	private void loadPreferences() {
		mMovieDataAdapter = new MovieDataAdapter(this);
		mMovieDataAdapter.open();

		actorList = new ArrayList<RatablePerson>();
		mMovieDataAdapter.readActors(actorList);

		directorList = new ArrayList<RatablePerson>();
		mMovieDataAdapter.readDirectors(directorList);

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
			mMovieDataAdapter.addMPAAs(mpaaList);
		}

		selectedMPAAList = new ArrayList<String>();
		for (MPAA mpaa : mpaaList) {
			if (mpaa.getPreference() == 1) {
				selectedMPAAList.add(mpaa.getName());
			}
		}

		platformList = new ArrayList<Platform>();
		mMovieDataAdapter.readPlatforms(platformList);
		if (platformList.isEmpty()) {
			platformList.add(new Platform("RedBox", 0));
			platformList.add(new Platform("HULU", 0));
			platformList.add(new Platform("In Theatres", 0));
			platformList.add(new Platform("Google Play", 0));
			platformList.add(new Platform("Amazon Prime", 0));
			platformList.add(new Platform("YouTube", 0));
			platformList.add(new Platform("Netflix", 0));
			mMovieDataAdapter.addPlatforms(platformList);
		}

		selectedPlatformList = new ArrayList<String>();
		for (Platform platform : platformList) {
			if (platform.getPreference() == 1) {
				selectedPlatformList.add(platform.getName());
			}
		}
	}

	private void configureListViews() {

		// NonScrollListView actorListView = (NonScrollListView)
		// findViewById(R.id.actor_mood_list_view);
		// actorAdapter = new RatableRowAdapter(this, actorList);
		// actorListView.setAdapter(actorAdapter);
		NonScrollListView actorListView = (NonScrollListView) findViewById(R.id.actor_mood_list_view);
		actorAdapter = new ArrayAdapter<RatablePerson>(this,
				android.R.layout.simple_list_item_1, actorList);
		actorListView.setAdapter(actorAdapter);

		// AbsListView directorListView = (AbsListView)
		// findViewById(R.id.director_mood_list_view);
		// directorAdapter = new RatableRowAdapter(this, directorList);
		// directorListView.setAdapter(directorAdapter);

		AbsListView directorListView = (AbsListView) findViewById(R.id.director_mood_list_view);
		directorAdapter = new ArrayAdapter<RatablePerson>(this,
				android.R.layout.simple_list_item_1, directorList);
		directorListView.setAdapter(directorAdapter);

		directorListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						showDeleteConfirmDialog(false, position);
						return false;
					}
				});

		actorListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDeleteConfirmDialog(true, position);
				return true;
			}
		});

		AbsListView selectedGenreListView = (AbsListView) findViewById(R.id.genre_mood_list_view);
		selectedGenreAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, selectedGenreList);
		selectedGenreListView.setAdapter(selectedGenreAdapter);

		AbsListView selectedMPAAListView = (AbsListView) findViewById(R.id.mpaa_mood_list_view);
		selectedMPAAAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, selectedMPAAList);
		selectedMPAAListView.setAdapter(selectedMPAAAdapter);

		AbsListView selectedPlatformListView = (AbsListView) findViewById(R.id.platform_mood_list_view);
		selectedPlatformAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, selectedPlatformList);
		selectedPlatformListView.setAdapter(selectedPlatformAdapter);

	}

	private void configureButtons() {

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

		Button editGenresButton = (Button) findViewById(R.id.editGenresButton);
		editGenresButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEditGenresDialog();
			}
		});

		Button editMPAAButton = (Button) findViewById(R.id.editMPAAsButton);
		editMPAAButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEditMPAADialog();
			}
		});

		Button editPlatformButton = (Button) findViewById(R.id.editPlatformsButton);
		editPlatformButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEditPlatformDialog();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences, menu);

		menu.getItem(0).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						showDeletePreferencesDialog();
						return false;
					}
				});

		menu.getItem(1).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						showSavePreferencesDialog();
						return false;
					}
				});

		menu.getItem(2).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						showLoadPreferencesDialog();
						return false;
					}
				});

		menu.getItem(3).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						showMergePreferencesDialog();
						return false;
					}
				});

		// menu.getItem(4).setOnMenuItemClickListener(
		// new OnMenuItemClickListener() {
		//
		// @Override
		// public boolean onMenuItemClick(MenuItem item) {
		// createCloudAccount("willisaj", "change",
		// "willisaj@rose", true);
		// return false;
		// }
		// });
		return true;
	}

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

	// protected void createCloudAccount(String userName, String password,
	// String email, boolean publicAccessible) {
	// try {
	// BackendApi.insertAccount(userName, password, email,
	// publicAccessible);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// } catch (ExecutionException e) {
	// e.printStackTrace();
	// }
	// }

	protected void showMergePreferencesDialog() {
		DialogFragment df = new DialogFragment() {
			private UserMergeView mv;

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				mv = new UserMergeView(getActivity());

				builder.setTitle(R.string.user_merge_dialog);

				builder.setView(mv);

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								initializeMerge();
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

			private void initializeMerge() {

				String name = mv.getUserName();

				if (!name.equals("")) {
					mergePreferencesWithUser(name);
				} else {
					Toast.makeText(getActivity(),
							"You didn't enter in a name, silly!\n  Try again.",
							Toast.LENGTH_SHORT).show();
				}
			}
		};

		df.show(getFragmentManager(), "merging user preferences");
	}

	protected void showSavePreferencesDialog() {
		DialogFragment df = new DialogFragment() {

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// set options
				builder.setTitle(R.string.save_preferences_title);
				builder.setMessage(R.string.save_preferences_message);

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								savePreferencesToCloud();
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
		df.show(getFragmentManager(), "saving preferences");

	}

	protected void showLoadPreferencesDialog() {
		DialogFragment df = new DialogFragment() {

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// set options
				builder.setTitle(R.string.load_preferences_title);
				builder.setMessage(R.string.load_preferences_message);

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								loadPreferencesFromCloud();
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
		df.show(getFragmentManager(), "loading preferences");

	}

	protected void mergePreferencesWithUser(String user_Name) {
		// TODO save all data to database and selectedLists as you go, but don't
		// add duplicates
		try {
			List<RatablePerson> actors = BackendApi
					.getRatedActorsForUser(user_Name);

			for (RatablePerson person : actors) {
				actorList.add(person);
				mMovieDataAdapter.addActor(person);
				Log.d("Foo", "Foo loading could prefs");
			}

			List<RatablePerson> directors = BackendApi
					.getRatedDirectorsForUser(user_Name);
			for (RatablePerson person : directors) {
				mMovieDataAdapter.addDirector(person);
				directorList.add(person);
			}

			List<Genre> genres = BackendApi.getRatedGenresForUser(user_Name);
			for (Genre genre : genres) {
				for (Genre localGenre : genreList) {
					if ((localGenre.getName()).equals(genre.getName())) {
						localGenre.setPreference(1);
						break;
					}
				}
			}
			
			mMovieDataAdapter.clearCheckablePreferences();

			mMovieDataAdapter.addGenres(genreList);
			selectedGenreList.clear();
			for (Genre genre : genreList) {
				if (genre.getPreference() == 1) {
					selectedGenreList.add(genre.getName());
				}
			}

			List<Platform> platforms = BackendApi
					.getRatedPlatformsForUser(user_Name);
			for (Platform platform : platforms) {
				for (Platform localPlatform : platformList) {
					if ((localPlatform.getName()).equals(platform.getName())) {
						localPlatform.setPreference(1);
						break;
					}
				}
			}

			mMovieDataAdapter.addPlatforms(platformList);
			selectedPlatformList.clear();
			for (Platform platform : platformList) {
				if (platform.getPreference() == 1) {
					selectedPlatformList.add(platform.getName());
				}
			}

			List<MPAA> mpaas = BackendApi.getRatedRatingsForUser(user_Name);
			for (MPAA mpaa : mpaas) {
				for (MPAA localMPAA : mpaaList) {
					if ((localMPAA.getName()).equals(mpaa.getName())) {
						localMPAA.setPreference(1);
						break;
					}
				}
			}

			mMovieDataAdapter.addMPAAs(mpaaList);
			selectedMPAAList.clear();
			for (MPAA mpaa : mpaaList) {
				if (mpaa.getPreference() == 1) {
					selectedMPAAList.add(mpaa.getName());
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		actorAdapter.notifyDataSetChanged();
		directorAdapter.notifyDataSetChanged();
		selectedGenreAdapter.notifyDataSetChanged();
		selectedMPAAAdapter.notifyDataSetChanged();
		selectedPlatformAdapter.notifyDataSetChanged();

	}

	protected void loadPreferencesFromCloud() {
		clearAllPreferences();
		mergePreferencesWithUser(userName);
	}

	protected void savePreferencesToCloud() {

		clearCloudAccountPreferences();
		copyLocalPreferencesToCloudAccount();

	}

	private void clearCloudAccountPreferences() {
		try {
			List<RatablePerson> actors = BackendApi
					.getRatedActorsForUser(userName);
			for (RatablePerson person : actors) {
				BackendApi.deleteRatedActorForUser(userName, person.getName());
			}

			List<RatablePerson> directors = BackendApi
					.getRatedDirectorsForUser(userName);
			for (RatablePerson person : directors) {
				BackendApi.deleteRatedDirectorForUser(userName,
						person.getName());
			}

			List<Genre> genres = BackendApi.getRatedGenresForUser(userName);
			for (Genre genre : genres) {
				BackendApi.deleteRatedGenreForUser(userName, genre.getName());
			}

			List<Platform> platforms = BackendApi
					.getRatedPlatformsForUser(userName);
			for (Platform platform : platforms) {
				BackendApi.deleteRatedPlatformForUser(userName,
						platform.getName());
			}

			List<MPAA> mpaas = BackendApi.getRatedRatingsForUser(userName);
			for (MPAA mpaa : mpaas) {
				BackendApi.deleteRatedMPAAForUser(userName, mpaa.getName());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	private void copyLocalPreferencesToCloudAccount() {
		try {

			for (RatablePerson actor : actorList) {
				BackendApi.insertRatedActorForUser(userName, actor.getName(),
						actor.getRating());
			}

			for (RatablePerson director : directorList) {
				BackendApi.insertRatedDirectorForUser(userName,
						director.getName(), director.getRating());
			}

			for (Genre genre : genreList) {
				if (genre.getPreference() == 1) {
					BackendApi.insertRatedGenreForUser(userName,
							genre.getName(), genre.getPreference());
				}
			}

			for (Platform platform : platformList) {
				if (platform.getPreference() == 1) {
					BackendApi.insertRatedPlatformForUser(userName,
							platform.getName(), platform.getPreference());
				}
			}

			for (MPAA mpaa : mpaaList) {
				if (mpaa.getPreference() == 1) {
					BackendApi.insertRatedMPAAForUser(userName, mpaa.getName(),
							mpaa.getPreference());
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	protected void showEditPlatformDialog() {
		DialogFragment df = new DialogFragment() {
			private ListView platformListView;

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// set options
				builder.setTitle(R.string.edit_platform_title);

				platformListView = new ListView(getActivity());
				PlatformAdapter platformAdapter = new PlatformAdapter(
						getActivity(), platformList);
				platformListView.setAdapter(platformAdapter);

				platformListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								((CheckableView) view).toggle();
							}
						});

				builder.setView(platformListView);

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								selectedPlatformList.clear();

								for (int i = 0; i < platformListView
										.getChildCount(); i++) {

									if (((CheckableView) platformListView
											.getChildAt(i)).isSelected()) {
										(platformList.get(i)).setPreference(1);
										selectedPlatformList.add(platformList
												.get(i).getName());
									} else {
										(platformList.get(i)).setPreference(0);
									}
								}

								updatePlatforms();

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

		df.show(getFragmentManager(), "editing Platforms");

	}

	protected void showDeleteConfirmDialog(boolean isAct, int position) {
		final int index = position;
		final boolean isActor = isAct;
		DialogFragment df = new DialogFragment() {

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// set options
				if (isActor) {
					builder.setTitle(R.string.delete_actor_title);
					builder.setMessage(R.string.delete_actor_message);
				} else {
					builder.setTitle(R.string.delete_director_title);
					builder.setMessage(R.string.delete_director_message);
				}

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (isActor) {
									removeActor(index);
								} else {
									removeDirector(index);
								}
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
		df.show(getFragmentManager(), "deleting person");
	}

	protected void showDeletePreferencesDialog() {
		DialogFragment df = new DialogFragment() {

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// set options
				builder.setTitle(R.string.delete_preferences_title);
				builder.setMessage(R.string.delete_preferences_message);

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								clearAllPreferences();
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
		df.show(getFragmentManager(), "deleting preferences");
	}

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
		mMovieDataAdapter.removeDirector((RatablePerson) directorAdapter
				.getItem(position));
		directorAdapter.remove(directorAdapter.getItem(position));
		directorAdapter.notifyDataSetChanged();
	}

	protected void removeActor(int position) {
		mMovieDataAdapter.removeActor((RatablePerson) actorAdapter
				.getItem(position));
		actorAdapter.remove(actorAdapter.getItem(position));
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
			mMovieDataAdapter.addActor(person);
			actorAdapter.add(person);
			actorAdapter.notifyDataSetChanged();
		} else {
			mMovieDataAdapter.addDirector(person);
			directorAdapter.add(person);
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
		selectedMPAAAdapter.notifyDataSetChanged();
		ArrayList<MPAA> stuff = new ArrayList<MPAA>();
		mMovieDataAdapter.readMPAAs(stuff);

		// for (MPAA mpaa : stuff) {
		// Log.d("printing mpaas",
		// mpaa.getName() + "  :       " + mpaa.getPreference());
		// }
		for (MPAA mpaa : mpaaList) {
			mMovieDataAdapter.updateMPAA(mpaa);
		}
	}

	private void updatePlatforms() {
		selectedPlatformAdapter.notifyDataSetChanged();
		for (Platform platform : platformList) {
			mMovieDataAdapter.updatePlatform(platform);
		}
	}

	private void clearAllPreferences() {
		mMovieDataAdapter.clearPreferences();

		actorList.clear();
		directorList.clear();
		selectedGenreList.clear();
		selectedPlatformList.clear();
		selectedMPAAList.clear();
		mpaaList.clear();
		platformList.clear();

		genreList = (ArrayList<Genre>) new TMDBMovieRecommender().getGenres();
		mMovieDataAdapter.addGenres(genreList);

		mpaaList = new ArrayList<MPAA>();
		mpaaList.add(new MPAA("G", 0));
		mpaaList.add(new MPAA("PG", 0));
		mpaaList.add(new MPAA("PG-13", 0));
		mpaaList.add(new MPAA("R", 0));
		mpaaList.add(new MPAA("NC-17", 0));
		mMovieDataAdapter.addMPAAs(mpaaList);

		platformList.add(new Platform("RedBox", 0));
		platformList.add(new Platform("HULU", 0));
		platformList.add(new Platform("In Theatres", 0));
		platformList.add(new Platform("Google Play", 0));
		platformList.add(new Platform("Amazon Prime", 0));
		platformList.add(new Platform("YouTube", 0));
		platformList.add(new Platform("Netflix", 0));
		mMovieDataAdapter.addPlatforms(platformList);

		actorAdapter.notifyDataSetChanged();
		directorAdapter.notifyDataSetChanged();
		selectedGenreAdapter.notifyDataSetChanged();
		selectedMPAAAdapter.notifyDataSetChanged();
		selectedPlatformAdapter.notifyDataSetChanged();

	}

}