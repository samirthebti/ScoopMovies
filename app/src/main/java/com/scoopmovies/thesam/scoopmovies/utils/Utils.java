package com.scoopmovies.thesam.scoopmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Samir Thebti  on 2/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class Utils {
    public static final double TMDB_POSTER_SIZE_RATIO = 185.0 / 277.0;
    public static final String PARC_MOVIES_TAG = "movies";
    public static final String PARC_REVIEWS_TAG = "reviews";
    public static final String PARC_VIDEOS_TAG = "reviews";

    public static final String PARC_MOVIE_TAG = "movie";
    public static final String PARC_REVIEW_TAG = "review";
    public static final String PARC_VIDEO_TAG = "video";

    public static final String SHARED_TRANSITION_NAME = "poster";
    public final static String EXTRA_MOVIE_POSITION = "movie_position";
    public static final String EXTRA_MOVIE_INTENT = "mMovie";
    public static final String SORTBY_PREF = "pref";

    public static int getScreenWidth(@NonNull Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
