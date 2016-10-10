package com.scoopmovies.thesam.scoopmovies.service;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;

import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

/**
 * Created by Samir Thebti  on 8/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class MoviesService extends IntentService {
    public static final String TAG = MoviesService.class.getSimpleName();
    private ArrayAdapter<Movies> myMovies;
    String sortby = PreferenceManager.getDefaultSharedPreferences(this).getString(Utils.SORT_BY_EXTRA, "popular");


    public MoviesService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String sortBy = intent.getStringExtra(Utils.SORT_BY_EXTRA);
    }


}
