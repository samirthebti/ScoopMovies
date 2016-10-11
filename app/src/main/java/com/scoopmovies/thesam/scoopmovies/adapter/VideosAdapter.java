package com.scoopmovies.thesam.scoopmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.scoopmovies.thesam.scoopmovies.R;
import com.scoopmovies.thesam.scoopmovies.model.Video;
import com.scoopmovies.thesam.scoopmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Samir Thebti  on 7/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoHolder> {
    public static final String TAG = VideosAdapter.class.getSimpleName();
    private final Context mContext;
    private final ArrayList<Video> mVideos;


    public VideosAdapter(Context mContext, ArrayList<Video> mVideos) {
        this.mContext = mContext;
        this.mVideos = mVideos;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_videos, parent, false);
        VideoHolder videoHolder = new VideoHolder(view);
        return videoHolder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        final Video video = mVideos.get(position);
        //Glide failed to load the videos image
        Picasso.with(mContext)
                .load(Utils.getThumbnailUrl(video))
                .into(holder.videoImageView);
        holder.playImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(Utils.YOUTUBE_PLAY_URL_BASE + video.getKey());
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        public final ImageView videoImageView;
        public final ImageView playImageView;
        public final FrameLayout frameLayout;

        public VideoHolder(View itemView) {
            super(itemView);
            playImageView = (ImageView) itemView.findViewById(R.id.VideoPreviewPlayButton);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.videoitem);
            videoImageView = (ImageView) itemView.findViewById(R.id.videoimageview);
        }

    }
}
