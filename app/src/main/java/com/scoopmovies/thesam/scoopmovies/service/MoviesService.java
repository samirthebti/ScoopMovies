package com.scoopmovies.thesam.scoopmovies.service;

import android.app.IntentService;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.network.VolleySing;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.scoopmovies.thesam.scoopmovies.network.ApiUtils.buildUrl;

/**
 * Created by Samir Thebti  on 8/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class MoviesService extends IntentService {
    public static final String TAG = MoviesService.class.getSimpleName();
    private ArrayAdapter<Movies> myMovies;

    public MoviesService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String sortBy = intent.getStringExtra(Utils.SORT_BY_EXTRA);

        JsonObjectRequest mJsObjRequest = new JsonObjectRequest
                (Method.GET, buildUrl(sortBy), null, new Listener<JSONObject>() {
                    JSONArray movies;

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response.toString());
                                movies = jsonObj.getJSONArray(ApiUtils.TAG_RESULTS);
                                for (int i = 0; i < movies.length(); i++) {
                                    Movies movie = new Movies();
                                    JSONObject b = (JSONObject) movies.get(i);
                                    movie.setId(b.getString(ApiUtils.ID));
                                    movie.setOverview(b.getString(ApiUtils.OVERVIEW));
                                    movie.setTitre(b.getString(ApiUtils.TITLE));
                                    movie.setPoster(b.getString(ApiUtils.POSTER));
                                    movie.setBackdrop_path(b.getString(ApiUtils.BACKDROP));
                                    movie.setDate(b.getString(ApiUtils.RELEASE_DATE));
                                    movie.setVote_average(b.getString(ApiUtils.VOTE_AVERGE));
                                    myMovies.add(movie);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

//                        Toast.makeText(context, myMovies.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error While Fetching Data", Toast.LENGTH_LONG).show();

                    }
                });
        // Access the RequestQueue through your singleton class. the context of  fragment is geted
        // by call getActivity() methode
        VolleySing.getInstance(getApplicationContext()).addToRequestQueue(mJsObjRequest);

        // TODO: 8/10/16 remplace this statment by cursor builkinsert
//        return myMovies;

    }

}
