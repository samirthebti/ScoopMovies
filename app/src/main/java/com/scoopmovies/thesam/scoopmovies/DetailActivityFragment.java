package com.scoopmovies.thesam.scoopmovies;


import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private Movies mMovie;
    private ImageView mCoverImageView;
    private TextView mTitle;
    private TextView mDate;
    private TextView mOverview;
    private ImageView mCover;
    private ImageView mPoster;
    private TextView mVoteAverrge;

    int moviePosition;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Utils.PARC_MOVIE_TAG, mMovie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.PARC_MOVIE_TAG)) {
            Intent intent = getActivity().getIntent();
            mMovie = intent.getParcelableExtra(Utils.EXTRA_MOVIE_INTENT);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(Utils.PARC_MOVIE_TAG)) {
            mMovie = savedInstanceState.getParcelable(Utils.PARC_MOVIE_TAG);

        }
        mTitle = (TextView) rootView.findViewById(R.id.movie_detail_title);
        mTitle.setText(mMovie.getTitre());

        mDate = (TextView) rootView.findViewById(R.id.movie_detail_date);
        mDate.setText(mMovie.getDate());

        mVoteAverrge = (TextView) rootView.findViewById(R.id.movie_detail_popular);
        mVoteAverrge.setText(mMovie.getVote_average());

        mOverview = (TextView) rootView.findViewById(R.id.movie_detail_overview);
        mOverview.setText(mMovie.getOverview());

        mPoster = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
        mCover = (ImageView) rootView.findViewById(R.id.movie_detail_cover);
        Glide.with(getActivity()).load(ApiUtils.POSTER_BASE_URL + mMovie.getBackdrop_path())
                .error(R.drawable.posternotfound)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mCover);
        Glide.with(getActivity()).load(ApiUtils.POSTER_BASE_URL + mMovie.getPoster())
                .error(R.drawable.posternotfound)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPoster);
        ApiUtils.getReview(getContext(), mMovie.getId());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviePosition = getActivity().getIntent().getIntExtra(Utils.EXTRA_MOVIE_POSITION, 0);
        mCoverImageView = (ImageView) view.findViewById(R.id.movie_detail_poster);
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            mCoverImageView.setTransitionName(
                    Utils.SHARED_TRANSITION_NAME + moviePosition);
        }
    }

    public void actionShare(String content) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(shareIntent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            actionShare(mMovie.toString());
        }
        return super.onOptionsItemSelected(item);
    }
}
