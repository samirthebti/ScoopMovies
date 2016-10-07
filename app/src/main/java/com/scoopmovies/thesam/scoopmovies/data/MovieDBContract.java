package com.scoopmovies.thesam.scoopmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Samir Thebti  on 7/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class MovieDBContract {

    public static final String CONTENT_AUTHORITY = "com.scoopmovies.thesam.app";

    public static final Uri BASE_CONTENT_URL = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_video = "video";

    public class MovieEntry implements BaseColumns {
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
