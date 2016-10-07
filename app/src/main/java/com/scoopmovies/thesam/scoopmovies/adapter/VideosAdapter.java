package com.scoopmovies.thesam.scoopmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    public static final String TAG = VideosAdapter.class.getSimpleName();
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
        Video video = mVideos.get(position);
        Glide.with(holder.videoImageView.getContext()).load(Utils.getThumbnailUrl(video))
                .override(R.dimen.video_width, R.dimen.video_height)
                .fitCenter()
//                .error(R.drawable.posternotfound)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.videoImageView);

        Log.d(TAG, "onBindViewHolder: " + Utils.getThumbnailUrl(video));
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        public ImageView videoImageView;

        public VideoHolder(View itemView) {
            super(itemView);
            videoImageView = (ImageView) itemView.findViewById(R.id.videoimageview);
        }

    }
}
