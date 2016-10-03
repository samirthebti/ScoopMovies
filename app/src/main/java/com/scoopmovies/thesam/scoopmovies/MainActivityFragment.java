package com.scoopmovies.thesam.scoopmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scoopmovies.thesam.scoopmovies.adapter.GridAdapter;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.utils.ItemClickSupport;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public static final String LOG_MOVIES = "movies";
    private List<Movies> movies;
    private RecyclerView mRecyclerView;
    private int desiredColumnWidth;
    private CoordinatorLayout coordinatorLayout;

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
        movies.clear();
    }

    public MainActivityFragment() {
        desiredColumnWidth = R.dimen.desired_column_width;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.main_coordinator);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.PARC_MOVIES_TAG)) {
            movies = ApiUtils.getMovies(getActivity());
            Log.d(LOG_TAG, "parcel sucess savedinstance null");
        } else if (savedInstanceState != null && savedInstanceState.containsKey(Utils.PARC_MOVIES_TAG)) {
            movies = savedInstanceState.getParcelableArrayList(Utils.PARC_MOVIES_TAG);
            Log.d(LOG_TAG, "parcel failed " + movies.toString());
        }
        // compute optimal number of columns based on available width
        int gridWidth = Utils.getScreenWidth(getActivity());
        int optimalColumnCount = Math.max(Math.round((1f * gridWidth) / desiredColumnWidth), 1);
        int actualPosterViewWidth = gridWidth / optimalColumnCount;

        mRecyclerView.setHasFixedSize(true);
        GridAdapter gridAdapter = new GridAdapter(getActivity(), movies, actualPosterViewWidth);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setAdapter(gridAdapter);
        // on RecyclerView Item Clicked
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent selectedMovie = new Intent(getActivity(), DetailActivity.class);
                selectedMovie.putExtra("movie", movies.get(position));
                startActivity(selectedMovie);
            }
        });
        return rootView;
    }
}
