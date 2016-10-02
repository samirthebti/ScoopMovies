package com.scoopmovies.thesam.scoopmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Samir Thebti  on 2/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class Utils {
    public static final double TMDB_POSTER_SIZE_RATIO = 185.0 / 277.0;

    public static int getScreenWidth(@NonNull Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
