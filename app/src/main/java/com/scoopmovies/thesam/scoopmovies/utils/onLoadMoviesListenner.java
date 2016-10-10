package com.scoopmovies.thesam.scoopmovies.utils;

import com.scoopmovies.thesam.scoopmovies.model.Movies;

import java.util.ArrayList;

/**
 * Created by Samir Thebti  on 6/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */
public interface onLoadMoviesListenner {
    public void onLoadFinished(ArrayList<Movies> movies);

}
