package com.scoopmovies.thesam.scoopmovies.network;

import android.net.Uri;


/**
 * Created by Samir Thebti  on 1/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class ApiUtils {

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "api_key";
    public static final String REVIEW = "reviews";
    public static final String VIDEO = "videos";
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String OVERVIEW = "overview";
    public static final String POSTER = "poster_path";
    public static final String TAG_RESULTS = "results";
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500/";
    public static final String BACKDROP = "backdrop_path";
    public static final String VOTE_AVERGE = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    //Review constants

    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    //Viedo constants
    public static final String KEY = "key";
    public static final String NAME = "name";
    public static final String SITE = "site";

    public static String buildUrl(String sortBy) {
        Uri buildedUrl = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY, com.scoopmovies.thesam.scoopmovies.BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
        return buildedUrl.toString();
    }


    public static String buildReviwUrl(String id) {
        Uri buildReviewUrl = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendPath(REVIEW)
                .appendQueryParameter(API_KEY, com.scoopmovies.thesam.scoopmovies.BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
        return buildReviewUrl.toString();
    }

    public static String buildVideoUrl(String id) {
        Uri buildReviewUrl = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendPath(VIDEO)
                .appendQueryParameter(API_KEY, com.scoopmovies.thesam.scoopmovies.BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
        return buildReviewUrl.toString();
    }


}
