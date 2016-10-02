package com.scoopmovies.thesam.scoopmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scoopmovies.thesam.scoopmovies.DetailActivity;
import com.scoopmovies.thesam.scoopmovies.R;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.services.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import java.util.List;

/**
 * Created by Samir Thebti  on 1/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private Context mContext;
    private List<Movies> mMovies;
    private int mPosterWidth;
    private int mPosterHeight;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImage;
        public TextView title;


        public ViewHolder(View v) {
            super(v);
            posterImage = (ImageView) v.findViewById(R.id.poster);
            title = (TextView) v.findViewById(R.id.title);

        }


    }

    public GridAdapter(Context mContext, List<Movies> mMovies, int actualPosterViewWidth) {
        this.mContext = mContext;
        this.mMovies = mMovies;
        mPosterWidth = actualPosterViewWidth;
        mPosterHeight = (int) (actualPosterViewWidth / Utils.TMDB_POSTER_SIZE_RATIO);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movies movie = mMovies.get(position);
        holder.title.setText(movie.getTitre());
        Glide.with(mContext).load(ApiUtils.POSTER_BASE_URL + movie.getPoster())
                .override(mPosterWidth, mPosterHeight)
                .error(R.drawable.posternotfound)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.posterImage);
        holder.posterImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectedMovie = new Intent(mContext, DetailActivity.class);
                selectedMovie.putExtra("movie", (Parcelable) movie);
                mContext.startActivity(selectedMovie);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


}