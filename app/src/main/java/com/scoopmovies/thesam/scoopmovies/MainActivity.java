package com.scoopmovies.thesam.scoopmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
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
            if (savedInstanceState == null) {
                mTwoPanel = true;
                Log.d(TAG, "onCreate: ");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.details_container, new DetailActivityFragment(), DETAILS_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPanel = false;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new MainActivityFragment())
                .commit();
        Stetho.initializeWithDefaults(this);
        Fabric.with(this, new Crashlytics());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
