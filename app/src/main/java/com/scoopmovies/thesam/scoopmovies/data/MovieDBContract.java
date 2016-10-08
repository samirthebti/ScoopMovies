package com.scoopmovies.thesam.scoopmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Samir Thebti  on 7/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class MovieDBContract {
    //Content Authority
    public static final String CONTENT_AUTHORITY = "com.scoopmovies.thesam.app";
    // base Content URI
    public static final Uri BASE_CONTENT_URL = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String MOVIE_PATH = "movie";


    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_video = "video";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URL.buildUpon().appendPath(MOVIE_PATH).build();
        public static final String CONTEN_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;
        public static final String CONTEN_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "titre";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop_path";
        public static final String COLUMN_AVERGE = "vote_average";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_FAVOIRT = "favorite";
        public static final String COLUMN_REVIEW_KEY = "review_id";
        public static final String COLUMN_VIDEO_KEY = "video_id";

        public static Uri buildMovieWithId(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }


    public class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
    }

    public class VideoEntry implements BaseColumns {
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
    }


}
