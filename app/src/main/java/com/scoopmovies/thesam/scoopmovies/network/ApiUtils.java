package com.scoopmovies.thesam.scoopmovies.network;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.scoopmovies.thesam.scoopmovies.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Samir Thebti  on 1/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class ApiUtils {

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";
    public static final String API_KEY = "api_key";
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String OVERVIEW = "overview";
    public static final String POSTER = "poster_path";
    public static final String TAG_MOVIE = "results";
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500/";

    public static String BuildUrl() {
        Uri buildUrl = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY, com.scoopmovies.thesam.scoopmovies.BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
        return buildUrl.toString();
    }

    public static ArrayList getMovies(final Context context) {
        final ArrayList<Movies> myMovies = new ArrayList<>();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, BuildUrl(), null, new Response.Listener<JSONObject>() {
                    JSONArray movies;

                    @Override
                    public void onResponse(JSONObject response) {

                        if (response != null) {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response.toString());
                                movies = jsonObj.getJSONArray(TAG_MOVIE);
                                for (int i = 0; i < movies.length(); i++) {
                                    Movies movie = new Movies();
                                    JSONObject b = (JSONObject) movies.get(i);
                                    movie.setId(b.getString(ID));
                                    movie.setOverview(b.getString(OVERVIEW));
                                    movie.setTitre(b.getString(TITLE));
                                    movie.setPoster(b.getString(POSTER));
                                    myMovies.add(movie);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
//                        Toast.makeText(context, myMovies.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Network Failed", Toast.LENGTH_LONG).show();

                    }
                });
        // Access the RequestQueue through your singleton class. the context of  fragment is geted
        // by call getActivity() methode
        VolleySing.getInstance(context).addToRequestQueue(jsObjRequest);


        return myMovies;
    }

}
