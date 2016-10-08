package com.scoopmovies.thesam.scoopmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.scoopmovies.thesam.scoopmovies.data.MovieDBContract.MovieEntry;

/**
 * Created by Samir Thebti  on 7/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    public static final String DATA_BASE_NAME = "scoopmovie.db";
    public static final int DATA_BASE_VERSION = 1;


    public MovieDBHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoEntry.TABLE_NAME
//                + "(" + VideoEntry.COLUMN_ID + " INTEGER PRIMARY KEY,"
//                + VideoEntry.COLUMN_NAME + " STRING NOT NULL,"
//                + VideoEntry.COLUMN_KEY + " INTEGER NOT NULL UNIQUE,"
//                + VideoEntry.COLUMN_SITE + " STRING NOT NULL " +
//                " );";


//        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME
//                + "(" + ReviewEntry.COLUMN_ID + " INTEGER PRIMARY KEY,"
//                + ReviewEntry.COLUMN_AUTHOR + " STRING NOT NULL,"
//                + ReviewEntry.COLUMN_CONTENT + " STRING NOT NULL UNIQUE," +
//                " );";
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME
                + "( " + MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + MovieEntry.COLUMN_TITLE + " STRING NOT NULL, "
                + MovieEntry.COLUMN_BACKDROP + " INTEGER, "
                + MovieEntry.COLUMN_POSTER + " INTEGER, "
                + MovieEntry.COLUMN_AVERGE + " REAL, "
                + MovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL,"
//                + MovieEntry.COLUMN_FAVOIRT + " INTEGER"
//                // foreign key for videos
//                + "FOREIGN KEY (" + MovieEntry.COLUMN_VIDEO_KEY + ") REFERENCES " +
//                VideoEntry.TABLE_NAME + "(" + VideoEntry.COLUMN_ID + "),"
//                //foreign key for reviews
//                + "FOREIGN KEY (" + MovieEntry.COLUMN_REVIEW_KEY + ") REFERENCES " +
//                ReviewEntry.TABLE_NAME + "(" + ReviewEntry.COLUMN_ID + "),"

                + MovieEntry.COLUMN_DATE + " STRING NOT NULL " +
                " );";
//        sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);
//        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXIST" + VideoEntry.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXIST" + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST" + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
