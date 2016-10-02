package com.scoopmovies.thesam.scoopmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scoopmovies.thesam.scoopmovies.adapter.GridAdapter;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.services.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private List<Movies> movies;
    private RecyclerView mRecyclerView;
    private int desiredColumnWidth;

    public MainActivityFragment() {
        desiredColumnWidth = R.dimen.desired_column_width;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // compute optimal number of columns based on available width
        int gridWidth = Utils.getScreenWidth(getActivity());
        int optimalColumnCount = Math.max(Math.round((1f * gridWidth) / desiredColumnWidth), 1);
        int actualPosterViewWidth = gridWidth / optimalColumnCount;

        mRecyclerView.setHasFixedSize(true);
        movies = ApiUtils.getMovies(getActivity());
        GridAdapter gridAdapter = new GridAdapter(getActivity(), movies, actualPosterViewWidth);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setAdapter(gridAdapter);


        return rootView;
    }
}
