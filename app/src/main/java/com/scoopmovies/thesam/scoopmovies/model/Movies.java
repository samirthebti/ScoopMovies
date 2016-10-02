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

    public Movies(String id, String titre, String overview, String poster) {
        this.id = id;
        this.titre = titre;
        this.overview = overview;
        this.poster = poster;
    }

    public Movies() {
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
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", overview='" + overview + '\'' +
                ", poster='" + poster + '\'' +
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
    }

    protected Movies(Parcel in) {
        this.id = in.readString();
        this.titre = in.readString();
        this.overview = in.readString();
        this.poster = in.readString();
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
