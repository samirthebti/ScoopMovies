package com.scoopmovies.thesam.scoopmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.scoopmovies.thesam.scoopmovies.model.Video;

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
    private static final String SITE_YOUTUBE = "YouTube";

    public static int getScreenWidth(@NonNull Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * get the thumbnil image from youtube trailler
     *
     * @param video
     * @return
     */
    public static String getThumbnailUrl(@NonNull Video video) {
        if (SITE_YOUTUBE.equals(video.getSite())) {
            return String.format("http://img.youtube.com/vi/" + video.getKey() + "/0.jpg");
        } else {
            throw new UnsupportedOperationException("Only YouTube is supported!");
        }
    }

    /**
     * get the url of the Video for share acction in details screen
     *
     * @param video
     * @return
     */
    public static String getUrl(@NonNull Video video) {
        if (SITE_YOUTUBE.equals(video.getSite())) {
            return String.format("http://www.youtube.com/watch?v=%1$s", video.getId());
        } else {
            throw new UnsupportedOperationException("Only YouTube is supported!");
        }
    }

}
