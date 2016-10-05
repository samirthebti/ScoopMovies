package com.scoopmovies.thesam.scoopmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    private Movies movie;
    private ImageView mCoverImageView;
    int moviePosition;

    public DetailActivityFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Utils.PARC_MOVIE_TAG, movie);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.PARC_MOVIE_TAG)) {
            Intent intent = getActivity().getIntent();
            movie = intent.getParcelableExtra("movie");
            Log.d(TAG, "FAILES");
        } else if (savedInstanceState != null && savedInstanceState.containsKey(Utils.PARC_MOVIE_TAG)) {
            movie = savedInstanceState.getParcelable(Utils.PARC_MOVIE_TAG);
            Log.d(TAG, "succes ");
        }

        TextView title = (TextView) rootView.findViewById(R.id.movie_detail_title);
        title.setText(movie.getTitre());
        TextView overview = (TextView) rootView.findViewById(R.id.movie_detail_overview);
        overview.setText(movie.getOverview());
        ImageView poster = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
        ImageView cover = (ImageView) rootView.findViewById(R.id.movie_detail_cover);
        Glide.with(getActivity()).load(ApiUtils.POSTER_BASE_URL + movie.getBackdrop_path())
                .error(R.drawable.posternotfound)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(cover);
        Glide.with(getActivity()).load(ApiUtils.POSTER_BASE_URL + movie.getPoster())
                .error(R.drawable.posternotfound)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: 4/10/16 Remplace the coverview by the correspended id
        moviePosition = getActivity().getIntent().getIntExtra(Utils.EXTRA_MOVIE_POSITION, 0);
        mCoverImageView = (ImageView) view.findViewById(R.id.movie_detail_poster);
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            mCoverImageView.setTransitionName(
                    Utils.SHARED_TRANSITION_NAME + moviePosition);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }
}
