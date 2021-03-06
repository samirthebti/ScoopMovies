package com.scoopmovies.thesam.scoopmovies.adapter;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scoopmovies.thesam.scoopmovies.R;
import com.scoopmovies.thesam.scoopmovies.model.Movies;
import com.scoopmovies.thesam.scoopmovies.network.ApiUtils;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import java.util.ArrayList;
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

    public GridAdapter(Context mContext, int actualPosterViewWidth) {
        this.mContext = mContext;
        mPosterWidth = actualPosterViewWidth;
        mPosterHeight = (int) (actualPosterViewWidth / Utils.TMDB_POSTER_SIZE_RATIO);
    }

    public void setData(ArrayList<Movies> data) {
        this.mMovies = data;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_view, parent, false);
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
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.posterImage);
        // we will try to animate the transition
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            holder.posterImage.setTransitionName("poster" + position);
        }
    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}