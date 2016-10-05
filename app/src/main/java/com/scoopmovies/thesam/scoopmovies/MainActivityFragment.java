package com.scoopmovies.thesam.scoopmovies;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.scoopmovies.thesam.scoopmovies.adapter.GridAdapter;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.network.VolleySing;
import com.scoopmovies.thesam.scoopmovies.utils.ItemClickSupport;
import com.scoopmovies.thesam.scoopmovies.utils.ItemClickSupport.OnItemClickListener;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import static com.scoopmovies.thesam.scoopmovies.network.ApiUtils.buildUrl;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public static final String LOG_MOVIES = "movies";
    public static final String TOP_RATED = "top_rated";
    public static final String POPULAR = "popular";
    private List<Movies> movies;
    private RecyclerView mRecyclerView;
    private int desiredColumnWidth;
    private GridAdapter mGridAdapter;
    private String mChoix;
    private ArrayList<Movies> myMovies = new ArrayList<>();
    private JsonObjectRequest mJsObjRequest;
    private String mCuurentSortby;
    private SharedPreferences sharedPref;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Utils.PARC_MOVIES_TAG, (ArrayList<? extends Parcelable>) movies);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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
        Log.d(LOG_TAG, "onResume: ");
        mCuurentSortby = sharedPref.getString(getString(R.string.sharedpref), POPULAR);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause: ");
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.clearOnChildAttachStateChangeListeners();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.sharedpref), mCuurentSortby);
        editor.apply();
    }

    public MainActivityFragment() {
        desiredColumnWidth = R.dimen.desired_column_width;
        setHasOptionsMenu(true);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mCuurentSortby = sharedPref.getString(getString(R.string.sharedpref), POPULAR);
//        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.main_coordinator);
        Log.d(LOG_TAG, "onCreateView: ");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.PARC_MOVIES_TAG)) {
            mChoix = mCuurentSortby;
            movies = getMovies(getActivity(), mChoix);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(Utils.PARC_MOVIES_TAG)) {
            movies = savedInstanceState.getParcelableArrayList(Utils.PARC_MOVIES_TAG);
        }
        // compute optimal number of columns based on available width
        int gridWidth = Utils.getScreenWidth(getActivity());
        int optimalColumnCount = Math.max(Math.round((1f * gridWidth) / desiredColumnWidth), 1);
        int actualPosterViewWidth = gridWidth / optimalColumnCount;
        //SetUp the recyclerview
        mRecyclerView.setHasFixedSize(true);
        mGridAdapter = new GridAdapter(getActivity(), movies, actualPosterViewWidth);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mGridAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mGridAdapter);

        // on RecyclerView Item Clicked
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent selectedMovie = new Intent(getActivity(), DetailActivity.class);
                selectedMovie.putExtra("movie", movies.get(position));
                selectedMovie.putExtra("item_selected", position);
                //setup the animation and lanche the intent
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    selectedMovie.putExtra(Utils.EXTRA_MOVIE_POSITION, position);
                    ActivityOptions activityOptions = ActivityOptions
                            .makeSceneTransitionAnimation(getActivity(),
                                    new android.util.Pair<View, String>(v, Utils.SHARED_TRANSITION_NAME + position));
                    startActivity(selectedMovie, activityOptions.toBundle());
                } else {
                    startActivity(selectedMovie);
                }
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toprated_menu_item) {
            mChoix = TOP_RATED;
            if (!mCuurentSortby.equals(mChoix)) {
                movies.clear();
                movies = getMovies(getActivity(), mChoix);
                mRecyclerView.getRecycledViewPool().clear();
                mCuurentSortby = mChoix;
            }
            Log.d(LOG_TAG, "onOptionsItemSelected: ");
        }
        if (id == R.id.popular_menu_item) {
            mChoix = POPULAR;
            if (!mCuurentSortby.equals(mChoix)) {
                movies.clear();
                movies = getMovies(getActivity(), mChoix);
                mRecyclerView.getRecycledViewPool().clear();
                mCuurentSortby = mChoix;
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
                                movies = jsonObj.getJSONArray(ApiUtils.TAG_MOVIE);
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
//                        Toast.makeText(context, myMovies.toString(), Toast.LENGTH_LONG).show();

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
}
