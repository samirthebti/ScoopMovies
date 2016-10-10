package com.scoopmovies.thesam.scoopmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

public class DetailActivity extends AppCompatActivity {
    private Movies mMovie;
    private Bundle bundle;
    private Bundle bundle1;
    private boolean mTwoPanel;

    public static final String DETAIL_FRAGMENT_TAG = "DTTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(getIntent().getExtras());
            bundle = getIntent().getExtras();
            mMovie = bundle.getParcelable(Utils.EXTRA_MOVIE_INTENT);
            getSupportActionBar().setTitle(mMovie.getTitre());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, detailActivityFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        }

    }

    // on restore the instance of this activity wew will ensure the title in toolbar is loaded correctly
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bundle = getIntent().getExtras();
        bundle1 = new Bundle();

        mMovie = bundle.getParcelable(Utils.EXTRA_MOVIE_INTENT);
        try {
            getSupportActionBar().setTitle(mMovie.getTitre());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
