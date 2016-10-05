package com.scoopmovies.thesam.scoopmovies.model;

import android.os.Parcel;

/**
 * Created by Samir Thebti  on 1/10/16.
 * ----->> thebtisam@gmail.com <<-----
 */

public class Movies implements android.os.Parcelable {
    private String id;
    private String titre;
    private String overview;
    private String poster;
    private String backdrop_path;
    private String vote_average;
    private String date;

    public Movies(String id, String titre, String overview, String poster, String backdrop_path, String vote_average, String date) {
        this.id = id;
        this.titre = titre;
        this.overview = overview;
        this.poster = poster;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.date = date;
    }

    public Movies() {
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", overview='" + overview + '\'' +
                ", poster='" + poster + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.titre);
        dest.writeString(this.overview);
        dest.writeString(this.poster);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.vote_average);
        dest.writeString(this.date);
    }

    protected Movies(Parcel in) {
        this.id = in.readString();
        this.titre = in.readString();
        this.overview = in.readString();
        this.poster = in.readString();
        this.backdrop_path = in.readString();
        this.vote_average = in.readString();
        this.date = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
