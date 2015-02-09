package edu.rosehulman.moviematch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String KEY_TITLE = "KEY_TITLE";
	public static final String KEY_MOVIE_LIST = null;

	private RatableRowAdapter actorAdapter;
	private RatableRowAdapter directorAdapter;
	private ArrayAdapter<String> selectedGenreAdapter;
	private ArrayList<String> selectedGenreList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ArrayList<RatablePerson> actorList = null;
		NonScrollListView actorListView = (NonScrollListView) findViewById(R.id.actor_mood_list_view);
		actorAdapter = new RatableRowAdapter(this, actorList);
		actorListView.setAdapter(actorAdapter);

		ArrayList<RatablePerson> directorList = null;
		NonScrollListView directorListView = (NonScrollListView) findViewById(R.id.director_mood_list_view);
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

		// actorListView.setOnItemLongClickListener(new
		// OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// removeActor(position);
		// return true;
		// }
		// });

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

		// List<Genre> genres = new TMDBMovieRecommender().getGenres();
		// genres.add(0, new Genre(getString(R.string.genre_hint), 0));
		// ArrayAdapter<Genre> genreAdapter = new ArrayAdapter<Genre>(this,
		// android.R.layout.simple_spinner_item, genres);
		// genreAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// mGenreSpinner.setAdapter(genreAdapter);
		//
		// Spinner distributionSpinner = (Spinner)
		// findViewById(R.id.distribution_spinner);
		// // Create an ArrayAdapter using the string array and a default
		// spinner
		// // layout
		// ArrayAdapter<CharSequence> distAdapter = ArrayAdapter
		// .createFromResource(this, R.array.distributions,
		// android.R.layout.simple_spinner_item);
		// // Specify the layout to use when the list of choices appears
		// distAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// // Apply the adapter to the spinner
		// distributionSpinner.setAdapter(distAdapter);
		//
		// distributionSpinner
		// .setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent,
		// View view, int position, long id) {
		// MainActivity.this.distribution = ((TextView) view)
		// .getText().toString();
		// Log.d("", "You chose " + MainActivity.this.distribution
		// + " as your distribution");
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		this.selectedGenreList = new ArrayList<String>();
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

		Button wishListButton = (Button) findViewById(R.id.wishlist_button);
		Button recommendButton = (Button) findViewById(R.id.recommend_movie_button);

		wishListButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startMovieListActivity(false);
			}
		});

		OnClickListener recommendListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				startRecommendationActivity();
			}
		};

		recommendButton.setOnClickListener(recommendListener);
	}

	protected void showEditGenresDialog() {
		DialogFragment df = new DialogFragment() {
			HashSet<Genre> mGenres;
			private ArrayList<Genre> genreList;
			private ListView genreListView;

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// set options
				builder.setTitle(R.string.edit_genre_title);

				mGenres = new HashSet<Genre>();
				genreList = (ArrayList<Genre>) new TMDBMovieRecommender()
						.getGenres();
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

				// This attempt at populating the listView with valid results
				// failed.
				// Log.d("Woot",
				// "selected genres size: " + selectedGenreList.size());
				// for (int j = 0; j < selectedGenreList.size(); j++) {
				// String selectedName = selectedGenreList.get(j);
				// for (int i = 0; i < genreListView.getChildCount(); i++) {
				// CheckableView cv = (CheckableView) genreListView
				// .getChildAt(i);
				// Log.d("Woot", cv.getName() + "       " + selectedName);
				// if ((cv.getName()).equals(selectedName)) {
				// cv.setSelected();
				// Log.d("Woot", "we have a match ");
				// break;
				// }
				// }
				// }

				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								for (int i = 0; i < genreListView
										.getChildCount(); i++) {
									if (((CheckableView) genreListView
											.getChildAt(i)).isSelected()) {
										mGenres.add(genreList.get(i));
									}
								}

								setGenres(mGenres);
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

	protected void startRecommendationActivity() {
		// Intent intent = new Intent(this, MovieListActivity.class);
		// String actor = mActorEditText.getText().toString();
		// String director = mDirectorEditText.getText().toString();
		// int genreId = ((Genre) mGenreSpinner.getSelectedItem()).getId();
		//
		// IMovieRecommendation recommendation = new TMDBMovieRecommender()
		// .getRecommendation();
		// if (!actor.equals("")) {
		// recommendation.withActor(actor);
		// }
		// if (!director.equals("")) {
		// recommendation.withDirector(director);
		// }
		// if (genreId != 0) {
		// recommendation.withGenre(genreId);
		// }
		//
		// List<Movie> movies = recommendation.getMovies();
		//
		// intent.putExtra(KEY_TITLE, "Recommendations");
		// intent.putExtra(KEY_MOVIE_LIST, (ArrayList<Movie>) movies);
		//
		// this.startActivity(intent);
	}

	protected void startMovieListActivity(boolean isRecommendation) {
		Intent intent = new Intent(this, MovieListActivity.class);
		// TODO make a central state that initializes other classes?
		// TODO database.getArrayOfmovies();

		ArrayList<String> actorsOne = new ArrayList<String>();
		actorsOne.add("Jimmy Kimmel");
		actorsOne.add("That one guy from American Grafitti");
		actorsOne.add("Carrie Grant");
		ArrayList<String> actorsTwo = new ArrayList<String>();
		actorsTwo.add("Daniel Radcliffe");
		actorsTwo.add("Emma Stone");
		actorsTwo.add("Rupert Grint");

		ArrayList<String> genresOne = new ArrayList<String>();
		genresOne.add("Action");
		genresOne.add("Amazing");
		genresOne.add("Sci-Fi");
		ArrayList<String> genresTwo = new ArrayList<String>();
		genresTwo.add("Romance");
		genresTwo.add("Zombie Comedy");
		genresTwo.add("Animated");

		ArrayList<Movie> moviesOne = new ArrayList<Movie>();
		moviesOne.add(new Movie("A New Hope", "George Lucas", actorsOne));
		moviesOne.add(new Movie("The Empire Strikes Back", "George Lucas",
				actorsOne));
		moviesOne
				.add(new Movie("Return of the Jedi", "George Lucas", actorsOne));

		ArrayList<Movie> moviesTwo = new ArrayList<Movie>();
		moviesTwo.add(new Movie("The Sorcerer's Stone", "Stephen Spielberg",
				actorsTwo));
		moviesTwo.add(new Movie("The Chamber of Secrets", "Oprah Winfrey",
				actorsTwo));
		moviesTwo.add(new Movie("The Prisoner of Azkaban", "Barbra Streissand",
				actorsTwo));

		if (isRecommendation) {
			intent.putExtra(KEY_TITLE, "Recommendations");
			intent.putExtra(KEY_MOVIE_LIST, moviesOne);
		} else {
			intent.putExtra(KEY_TITLE, "Wish List");
			intent.putExtra(KEY_MOVIE_LIST, moviesTwo);
		}

		this.startActivity(intent);

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

	protected void setGenres(Set<Genre> genres) {
		// TODO add database call here
		selectedGenreList.clear();
		for (Genre genre : genres) {
			selectedGenreList.add(genre.getName());
		}
		selectedGenreAdapter.notifyDataSetChanged();
	}

}
