package com.scoopmovies.thesam.scoopmovies;


import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scoopmovies.thesam.scoopmovies.adapter.ReviewAdapter;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.model.Review;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.network.VolleySing;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static com.scoopmovies.thesam.scoopmovies.network.ApiUtils.TAG_RESULTS;

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
    private RecyclerView mReviewRecycleView;
    private int moviePosition;

    private RecyclerView.LayoutManager layoutManager;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Review> mReviews;

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
        //Review Recycler settings
        mReviewRecycleView = (RecyclerView) rootView.findViewById(R.id.reviews);
        mReviews = getReview(getActivity(), mMovie.getId());
        reviewAdapter = new ReviewAdapter(getActivity(), mReviews);
        mReviewRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewAdapter.notifyDataSetChanged();
        mReviewRecycleView.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
        Log.d(TAG, "onCreateView: " + mReviews.toString());
        //widgets settings
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

    public ArrayList<Review> getReview(final Context context, final String id) {
        final ArrayList<Review> myReview = new ArrayList<>();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, ApiUtils.buildReviwUrl(id), null, new Response.Listener<JSONObject>() {
                    JSONArray movies;

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response.toString());
                                movies = jsonObj.getJSONArray(TAG_RESULTS);
                                for (int i = 0; i < movies.length(); i++) {
                                    Review reviewItem = new Review();
                                    JSONObject b = (JSONObject) movies.get(i);
                                    reviewItem.setId(b.getString(ApiUtils.ID));
                                    reviewItem.setAuthor(b.getString(ApiUtils.AUTHOR));
                                    reviewItem.setContent(b.getString(ApiUtils.CONTENT));
                                    myReview.add(reviewItem);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            reviewAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: " + e);
                        }
//                        Toast.makeText(context, mReview.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error While Fetching Data", Toast.LENGTH_LONG).show();
//                        Toast.makeText(context, buildReviwUrl(id), Toast.LENGTH_LONG).show();

                    }
                });
        // Access the RequestQueue through your singleton class. the context of  fragment is geted
        // by call getActivity() methode
        VolleySing.getInstance(context).addToRequestQueue(jsObjRequest);


        return myReview;
    }

}
