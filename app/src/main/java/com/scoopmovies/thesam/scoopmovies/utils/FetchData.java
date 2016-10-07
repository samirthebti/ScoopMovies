package com.scoopmovies.thesam.scoopmovies.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.network.VolleySing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static com.scoopmovies.thesam.scoopmovies.network.ApiUtils.buildUrl;

/**
 * Created by Samir Thebti  on 6/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class FetchData implements INetworkCallBack {
    private ArrayList<Movies> myMovies;
    private JsonObjectRequest mJsObjRequest;

    @Override
    public ArrayList<Movies> onPostExecute(final Context context, String s) throws Exception {
        mJsObjRequest = new JsonObjectRequest
                (Method.GET, buildUrl(s), null, new Listener<JSONObject>() {
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
                        Toast.makeText(context, "Error While Fetching Data", Toast.LENGTH_LONG).show();

                    }
                });
        // Access the RequestQueue through your singleton class. the context of  fragment is geted
        // by call getActivity() methode
        VolleySing.getInstance(context).addToRequestQueue(mJsObjRequest);
        return myMovies;
    }
}

