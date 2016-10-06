package com.scoopmovies.thesam.scoopmovies.utils;

import android.content.Context;

import com.scoopmovies.thesam.scoopmovies.model.Movies;

import java.util.ArrayList;

/**
 * Created by Samir Thebti  on 6/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public interface INetworkCallBack {
    public ArrayList<Movies> onPostExecute(Context context, String s) throws Exception;
}
