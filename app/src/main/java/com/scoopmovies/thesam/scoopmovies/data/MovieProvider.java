package com.scoopmovies.thesam.scoopmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.scoopmovies.thesam.scoopmovies.data.MovieDBContract.MovieEntry;

/**
 * Created by Samir Thebti  on 8/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class MovieProvider extends ContentProvider {
    public static final UriMatcher mURI_MATCHER = buildUrimatcher();


    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;
    private MovieDBHelper mMovieDBHelper;

    @Override
    public boolean onCreate() {
        mMovieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    private static UriMatcher buildUrimatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieDBContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MovieDBContract.MOVIE_PATH, MOVIE);
        uriMatcher.addURI(authority, MovieDBContract.MOVIE_PATH + "/*", MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderby) {
        int match = mURI_MATCHER.match(uri);
        Cursor cursor;
        switch (match) {
            case MOVIE:
                cursor = mMovieDBHelper.getReadableDatabase()
                        .query(MovieEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                orderby);
                break;
            case MOVIE_WITH_ID:
                cursor = mMovieDBHelper.getReadableDatabase()
                        .query(MovieEntry.TABLE_NAME,
                                projection,
                                MovieEntry.COLUMN_ID + "='" + ContentUris.parseId(uri),
                                selectionArgs
                                , null
                                , null
                                , orderby);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = mURI_MATCHER.match(uri);
        switch (match) {
            case MOVIE:
                return MovieEntry.CONTEN_TYPE;
            case MOVIE_WITH_ID:
                return MovieEntry.CONTEN_TYPE_ITEM;
            default:
                throw new UnsupportedOperationException("Unknoun uri" + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int matcher = mURI_MATCHER.match(uri);
        Uri resultUri;
        switch (matcher) {
            case MOVIE:
                long _id = mMovieDBHelper.getReadableDatabase().insert(MovieEntry.TABLE_NAME, null, contentValues);
                if (_id == -1) {
                    throw new android.database.SQLException("Error while Inert data");
                } else {
                    resultUri = MovieDBContract.MovieEntry.buildMovieWithId(String.valueOf(_id));
                }
                break;
            default:
                throw new UnsupportedOperationException("Insert uri not matcher");

        }
        getContext().getContentResolver().notifyChange(uri, null);

        return resultUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();
        final int matcher = mURI_MATCHER.match(uri);
        int rowsdeleted;
        switch (matcher) {
            case MOVIE:
                rowsdeleted = db.delete(MovieEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("delete uri not matcher");
        }
        if (rowsdeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsdeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();
        final int matcher = mURI_MATCHER.match(uri);
        int rowsUpdated;
        switch (matcher) {
            case MOVIE:
                rowsUpdated = db.update(MovieEntry.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("update uri not match");
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}