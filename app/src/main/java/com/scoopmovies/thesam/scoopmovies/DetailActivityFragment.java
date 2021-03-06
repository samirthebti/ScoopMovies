package com.scoopmovies.thesam.scoopmovies;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.scoopmovies.thesam.scoopmovies.adapter.VideosAdapter;
import com.scoopmovies.thesam.scoopmovies.data.MovieDBContract.MovieEntry;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.model.Review;
import com.scoopmovies.thesam.scoopmovies.model.Video;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.network.VolleySing;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;


import static com.scoopmovies.thesam.scoopmovies.network.ApiUtils.TAG_RESULTS;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    public static final String DETAIL_TAG = "path";
    private Movies mMovie;
    private ImageView mCoverImageView;
    private TextView mTitle;
    private TextView mDate;
    private TextView mOverview;
    private ImageView mCover;
    private ImageView mPoster;
    private TextView mVoteAverrge;
    private FloatingActionButton add;
    private RecyclerView mReviewRecycleView;
    private RecyclerView mVideoRecycleView;
    private int moviePosition;
    private boolean mFavorite = false;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Review> mReviews;
    private ArrayList<Video> mVideos;
    private VideosAdapter videoAdapter;


    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey(Utils.PARC_MOVIE_TAG)) {
            if (Utils.isDetailTwoPanel(getActivity())) {
                // TODO: feat : 10/10/16 select the movie to be showing the first time app run
                Bundle bundle = getArguments();
                mMovie = bundle.getParcelable("movie");

            } else {
                Intent intent = getActivity().getIntent();
                mMovie = intent.getParcelableExtra(Utils.EXTRA_MOVIE_INTENT);
            }
            if (mMovie != null) {
                mReviews = getReview(getActivity(), mMovie.getId());
                mVideos = getVideos(getActivity(), mMovie.getId());
            }

        } else if (savedInstanceState != null && savedInstanceState.containsKey(Utils.PARC_MOVIE_TAG)) {
            mMovie = savedInstanceState.getParcelable(Utils.PARC_MOVIE_TAG);
            mReviews = savedInstanceState.getParcelableArrayList(Utils.PARC_REVIEWS_TAG);
            mVideos = savedInstanceState.getParcelableArrayList(Utils.PARC_VIDEOS_TAG);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Utils.PARC_MOVIE_TAG, mMovie);
        outState.putParcelableArrayList(Utils.PARC_REVIEWS_TAG, (ArrayList<? extends Parcelable>) mReviews);
        outState.putParcelableArrayList(Utils.PARC_VIDEOS_TAG, (ArrayList<? extends Parcelable>) mVideos);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isDetailTwoPanel(getActivity())) {
            Bundle bundle = getArguments();
            mMovie = bundle.getParcelable("movie");

        } else {
            Intent intent = getActivity().getIntent();
            mMovie = intent.getParcelableExtra(Utils.EXTRA_MOVIE_INTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mCoverImageView = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
        mReviewRecycleView = (RecyclerView) rootView.findViewById(R.id.reviews);
        mVideoRecycleView = (RecyclerView) rootView.findViewById(R.id.trailler);


        add = (FloatingActionButton) rootView.findViewById(R.id.addtofavorite);
        mFavorite = isFaorite(mMovie);
        if (mFavorite) {
            add.setImageResource(R.drawable.ic_favorit);
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
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviePosition = getActivity().getIntent().getIntExtra(Utils.EXTRA_MOVIE_POSITION, 0);

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            mCoverImageView.setTransitionName(
                    Utils.SHARED_TRANSITION_NAME + moviePosition);
        }
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        //Review & videos RecyclerView settings

        reviewAdapter = new ReviewAdapter(getActivity(), mReviews);
        videoAdapter = new VideosAdapter(getActivity(), mVideos);
        mReviewRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVideoRecycleView.setLayoutManager(layoutManager);
        mVideoRecycleView.setNestedScrollingEnabled(false);
        mReviewRecycleView.setNestedScrollingEnabled(false);
        reviewAdapter.notifyDataSetChanged();
        videoAdapter.notifyDataSetChanged();
        mReviewRecycleView.setHasFixedSize(true);
        mVideoRecycleView.setHasFixedSize(true);

        mReviewRecycleView.setAdapter(new SlideInBottomAnimationAdapter(reviewAdapter));
        mVideoRecycleView.setAdapter(new SlideInBottomAnimationAdapter(videoAdapter));
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie != null) {
                    addToFavorite(mMovie);
                }
            }
        });

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
            if (mVideos != null) {
                actionShare(Utils.YOUTUBE_PLAY_URL_BASE + mVideos.get(0).getKey());
            }

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

    public ArrayList<Video> getVideos(final Context context, final String id) {
        final ArrayList<Video> myVideos = new ArrayList<>();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, ApiUtils.buildVideoUrl(id), null, new Response.Listener<JSONObject>() {
                    JSONArray videos;

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response.toString());
                                videos = jsonObj.getJSONArray(TAG_RESULTS);
                                for (int i = 0; i < videos.length(); i++) {
                                    Video videoItem = new Video();
                                    JSONObject b = (JSONObject) videos.get(i);
                                    videoItem.setId(b.getString(ApiUtils.ID));
                                    videoItem.setKey(b.getString(ApiUtils.KEY));
                                    videoItem.setName(b.getString(ApiUtils.NAME));
                                    videoItem.setSite(b.getString(ApiUtils.SITE));
                                    myVideos.add(videoItem);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            videoAdapter.notifyDataSetChanged();
                        }
                        try {

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


        return myVideos;
    }

    private void addToFavorite(Movies movie) {
        Uri rowUri;
        if (!mFavorite) {
            ContentValues values = new ContentValues();
            values.put(MovieEntry.COLUMN_TITLE, movie.getTitre());
            values.put(MovieEntry.COLUMN_ID, movie.getId());
            values.put(MovieEntry.COLUMN_POSTER, movie.getPoster());
            values.put(MovieEntry.COLUMN_BACKDROP, movie.getBackdrop_path());
            values.put(MovieEntry.COLUMN_AVERGE, movie.getVote_average());
            values.put(MovieEntry.COLUMN_DATE, movie.getDate());
            values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            rowUri = getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, values);
            long rowId = ContentUris.parseId(rowUri);
            if (rowId == -1) {
                Toast.makeText(getContext(), "Insertion FAiled", Toast.LENGTH_LONG).show();
            } else {
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    Snackbar.make(getView(), movie.getTitre() + " Added to favorite List ", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getActivity().getColor(R.color.ok_color)).show();
                } else {
                    Snackbar.make(getView(), movie.getTitre() + " Added to favorite List ", Snackbar.LENGTH_LONG)
                            .show();
                }
                add.setImageResource(R.drawable.ic_favorit);
                mFavorite = true;
            }
        } else {
            int rowsdeleted = getContext().getContentResolver().delete(
                    MovieEntry.CONTENT_URI,
                    MovieEntry.COLUMN_ID + "=? ",
                    new String[] {movie.getId()});
            if (rowsdeleted != 0) {
                add.setImageResource(R.drawable.ic_nofavorit);
                Snackbar.make(getView(), movie.getTitre() + " Removed from favorite List ", Snackbar.LENGTH_LONG)
                        .setAction("ok", null)
                        .setActionTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_dark))
                        .show();
                mFavorite = false;
            }
        }
    }


    private boolean isFaorite(Movies movie) {
        boolean test = false;
        if (movie != null) {
            Cursor cursor = getContext().getContentResolver().query(
                    MovieEntry.CONTENT_URI,
                    new String[] {MovieEntry.COLUMN_ID},
                    MovieEntry.COLUMN_ID + "=? ",
                    new String[] {movie.getId()}, null);
            if (cursor != null && cursor.getCount() > 0) {
                test = true;
            }
            cursor.close();
        }
        return test;
    }
}
