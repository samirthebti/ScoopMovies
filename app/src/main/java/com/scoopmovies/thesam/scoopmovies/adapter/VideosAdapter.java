package com.scoopmovies.thesam.scoopmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scoopmovies.thesam.scoopmovies.R;
import com.scoopmovies.thesam.scoopmovies.adapter.VideosAdapter.VideoHolder;
import com.scoopmovies.thesam.scoopmovies.model.Video;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Samir Thebti  on 7/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class VideosAdapter extends RecyclerView.Adapter<VideoHolder> {
    private Context mContext;
    private ArrayList<Video> mVideos;


    public VideosAdapter(Context mContext, ArrayList<Video> mVideos) {
        this.mContext = mContext;
        this.mVideos = mVideos;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_videos, parent, false);
        VideoHolder videoHolder = new VideoHolder(view);
        return videoHolder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Glide.with(mContext).load(Utils.getUrl(mVideos.get(position)))
                .fitCenter()
                .crossFade()
                .error(R.drawable.posternotfound)
                .into(holder.videoImageView);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        private ImageView videoImageView;

        public VideoHolder(View itemView) {
            super(itemView);
            videoImageView = (ImageView) itemView.findViewById(R.id.videoimageview);
        }
    }
}
