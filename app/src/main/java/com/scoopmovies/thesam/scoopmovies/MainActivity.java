package com.scoopmovies.thesam.scoopmovies;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.utils.Callback;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements Callback {
    public static final String TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPanel;
    public static final String DETAILS_FRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.details_container) != null) {
            mTwoPanel = true;

        } else {
            mTwoPanel = false;
        }

//        Stetho.initializeWithDefaults(this);
        Fabric.with(this, new Crashlytics());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mTwoPanel != true) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_two_pane_layout, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieItemSelected(Movies movies, int position, ActivityOptions activityOptions) {
        if (mTwoPanel) {
            if (movies != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie", movies);

                DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
                detailActivityFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.details_container, detailActivityFragment, DETAILS_FRAGMENT_TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else {
            Intent intent = new Intent(this, DetailActivity.class);

            intent.putExtra(Utils.EXTRA_MOVIE_INTENT, movies);
            intent.putExtra(Utils.EXTRA_MOVIE_POSITION, position);
            if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, activityOptions.toBundle());
            } else {
                startActivity(intent, activityOptions.toBundle());
            }
        }
    }
}
