package com.scoopmovies.thesam.scoopmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scoopmovies.thesam.scoopmovies.R;
import com.scoopmovies.thesam.scoopmovies.model.Review;

import java.util.ArrayList;

/**
 * Created by Samir Thebti  on 6/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.PlaceHolder> {
    private ArrayList<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        mReviews = reviews;
        mContext = context;
    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_review, parent, false);
        PlaceHolder viewHolder = new PlaceHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(PlaceHolder holder, int position) {
        holder.reviewContent.setText(mReviews.get(position).getContent());
        holder.reviewAuthor.setText(mReviews.get(position).getAuthor());

    }


    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class PlaceHolder extends RecyclerView.ViewHolder {
        public TextView reviewAuthor;
        public TextView reviewContent;

        public PlaceHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}