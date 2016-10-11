package com.scoopmovies.thesam.scoopmovies.utils;

import android.app.ActivityOptions;
import android.support.annotation.Nullable;

import com.scoopmovies.thesam.scoopmovies.model.Movies;

/**
 * Created by Samir Thebti  on 6/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

/**
 * Interface fot handler Movie Click item
 */
public interface Callback {
    public void onMovieItemSelected(Movies movies, int position, @Nullable ActivityOptions activityOptions);

}
