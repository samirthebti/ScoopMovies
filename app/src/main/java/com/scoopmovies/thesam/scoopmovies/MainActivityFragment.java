package com.scoopmovies.thesam.scoopmovies;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.scoopmovies.thesam.scoopmovies.adapter.GridAdapter;
import com.scoopmovies.thesam.scoopmovies.data.MovieDBContract.MovieEntry;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.network.VolleySing;
import com.scoopmovies.thesam.scoopmovies.utils.Callback;
import com.scoopmovies.thesam.scoopmovies.utils.ItemClickSupport;
import com.scoopmovies.thesam.scoopmovies.utils.ItemClickSupport.OnItemClickListener;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;


import static com.scoopmovies.thesam.scoopmovies.network.ApiUtils.buildUrl;
import static com.scoopmovies.thesam.scoopmovies.utils.Utils.FAVORITE;
import static com.scoopmovies.thesam.scoopmovies.utils.Utils.POPULAR;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public static final String LOG_MOVIES = "movies";

    private ArrayList<Movies> movies;
    private RecyclerView mRecyclerView;
    private int mDesiredColumnWidth;
    private GridAdapter mGridAdapter;
    private String mChoix;
    private ArrayList<Movies> myMovies = new ArrayList<>();
    private JsonObjectRequest mJsObjRequest;
    private String mCurentSortby;
    private SharedPreferences sharedPref;
    private int mCurrentPosition;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Utils.PARC_MOVIES_TAG, (ArrayList<? extends Parcelable>) movies);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.clearOnChildAttachStateChangeListeners();

    }

    @Override
    public void onResume() {
        super.onResume();
        mCurentSortby = sharedPref.getString(getString(R.string.sharedpref), Utils.POPULAR);
        mCurrentPosition = sharedPref.getInt(getString(R.string.positionpref), 1);
        if (mCurentSortby.equals(FAVORITE)) {
            movies = getFavorit();
            mGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.sharedpref), mCurentSortby);
        editor.apply();
        SharedPreferences.Editor editor1 = sharedPref.edit();
        editor1.putInt(getString(R.string.positionpref), mCurrentPosition);
        editor1.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.sharedpref), mCurentSortby);
        editor.apply();
        SharedPreferences.Editor editor1 = sharedPref.edit();
        editor1.putInt(getString(R.string.positionpref), mCurrentPosition);
        editor1.apply();
    }

    public MainActivityFragment() {
        mDesiredColumnWidth = R.dimen.desired_column_width;
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.smoothScrollToPosition(mCurrentPosition);
        if (getActivity().findViewById(R.id.details_container) != null) {
            if (Utils.isMainTwoPanel(getActivity())) {
                ((Callback) getActivity()).onMovieItemSelected(movies.get(0), 0, null);
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!sharedPref.contains(getString(R.string.sharedpref))) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.sharedpref), POPULAR);
            editor.apply();
        }
        if (!sharedPref.contains(getString(R.string.positionpref))) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.positionpref), 1);
            editor.apply();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Glide.clear(rootView);
        mCurentSortby = sharedPref.getString(getString(R.string.sharedpref), POPULAR);
        mCurrentPosition = sharedPref.getInt(getString(R.string.positionpref), 1);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.PARC_MOVIES_TAG)) {
            mChoix = mCurentSortby;

            if (!mCurentSortby.equals(FAVORITE)) {
                movies = getMovies(getActivity(), mChoix);
            } else {
                movies = getFavorit();
            }

        } else if (savedInstanceState != null && savedInstanceState.containsKey(Utils.PARC_MOVIES_TAG)) {
            movies = savedInstanceState.getParcelableArrayList(Utils.PARC_MOVIES_TAG);
        }
        // compute optimal number of columns based on available width
        int gridWidth = Utils.getScreenWidth(getActivity());
        int optimalColumnCount = Math.max(Math.round((1f * gridWidth) / mDesiredColumnWidth), 1);
        int actualPosterViewWidth = gridWidth / optimalColumnCount;
        //SetUp the recyclerview
        mRecyclerView.setHasFixedSize(true);
        mGridAdapter = new GridAdapter(getActivity().getApplicationContext(), actualPosterViewWidth);

        mGridAdapter.setData(movies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mGridAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(mGridAdapter));

        // on RecyclerView Item Clicked
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                mCurrentPosition = position;
                //setup the animation and lanche the intent
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions activityOptions = ActivityOptions
                            .makeSceneTransitionAnimation(getActivity(),
                                    new android.util.Pair<View, String>(v, Utils.SHARED_TRANSITION_NAME + position));

                    ((Callback) getActivity()).onMovieItemSelected(movies.get(position), position, activityOptions);
                } else {
                    ((Callback) getActivity()).onMovieItemSelected(movies.get(position), position, null);
                }
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(LOG_TAG, "onOptionsItemSelected: ");
        if (id == R.id.toprated_menu_item) {
            mChoix = Utils.TOP_RATED;
            if (!mCurentSortby.equals(mChoix)) {
                movies.clear();
                movies = getMovies(getActivity(), mChoix);
                mGridAdapter.notifyDataSetChanged();
                mRecyclerView.getRecycledViewPool().clear();
                mCurentSortby = mChoix;

            }

        } else if (id == R.id.popular_menu_item) {
            mChoix = Utils.POPULAR;
            if (!mCurentSortby.equals(mChoix)) {
                movies.clear();
                movies = getMovies(getActivity(), mChoix);
                mGridAdapter.notifyDataSetChanged();
                mRecyclerView.getRecycledViewPool().clear();
                mCurentSortby = mChoix;
            }

        } else if (id == R.id.favorite_menu_item) {
            mChoix = Utils.FAVORITE;
            if (!mCurentSortby.equals(mChoix)) {
                movies.clear();
                movies = getFavorit();
                mGridAdapter.notifyDataSetChanged();
                mRecyclerView.getRecycledViewPool().clear();
                mCurentSortby = mChoix;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public ArrayList getMovies(final Context context, final String sortBy) {

        mJsObjRequest = new JsonObjectRequest
                (Method.GET, buildUrl(sortBy), null, new Listener<JSONObject>() {
                    JSONArray movies;

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response.toString());
                                movies = jsonObj.getJSONArray(ApiUtils.TAG_RESULTS);
                                for (int i = 0; i < movies.length(); i++) {
                                    Movies movie = new Movies();
                                    JSONObject b = (JSONObject) movies.get(i);
                                    movie.setId(b.getString(ApiUtils.ID));
                                    movie.setOverview(b.getString(ApiUtils.OVERVIEW));
                                    movie.setTitre(b.getString(ApiUtils.TITLE));
                                    movie.setPoster(b.getString(ApiUtils.POSTER));
                                    movie.setBackdrop_path(b.getString(ApiUtils.BACKDROP));
                                    movie.setDate(b.getString(ApiUtils.RELEASE_DATE));
                                    movie.setVote_average(b.getString(ApiUtils.VOTE_AVERGE));
                                    myMovies.add(movie);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mGridAdapter.notifyDataSetChanged();
                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error While Fetching Data", Toast.LENGTH_LONG).show();

                    }
                });
        // Access the RequestQueue through your singleton class. the context of  fragment is geted
        // by call getActivity() methode
        VolleySing.getInstance(context).addToRequestQueue(mJsObjRequest);
        return myMovies;
    }

    public ArrayList<Movies> getFavorit() {
        Cursor cursor = getContext().getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);
        myMovies.clear();
        while (cursor.moveToNext()) {
            Movies movie = new Movies();
            movie.setId(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ID)));
            movie.setTitre(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)));
            movie.setVote_average(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_AVERGE)));
            movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP)));
            movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER)));
            movie.setDate(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_DATE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)));
            myMovies.add(movie);
        }
        cursor.close();
        return myMovies;
    }

}
