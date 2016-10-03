package com.scoopmovies.thesam.scoopmovies.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Samir Thebti  on 1/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */


public class VolleySing {
    private static VolleySing mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private VolleySing(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySing getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySing(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}

