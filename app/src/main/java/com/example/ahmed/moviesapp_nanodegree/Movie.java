package com.example.ahmed.moviesapp_nanodegree;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmed on 4/8/17.
 */

public class Movie implements Parcelable {

    private static final String imageBaseURL = "http://image.tmdb.org/t/p/";
    private static final String imageSize = "w500";
    private String imageURL;
    private String title;
    private String releaseDate;
    private String voteAverage;
    private String overview;
    private int ID;


    public Movie(int ID, String title, String voteAverage, String releaseDate, String imageURL, String overview) {

        this.title = title;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.imageURL = imageURL;
        this.overview = overview;
        this.ID = ID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage + "/10";
    }

    public int getID() {
        return ID;
    }

    String getReleaseYear() {

        return releaseDate.substring(0, 4);
    }

    public String getSynopsis() {
        return overview;
    }

    String getFullImageUrlForMainFragment() {

        return Uri.parse(imageBaseURL).buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(this.getImageURL()).build().toString();
    }


    @Override
    public String toString() {

        return String.valueOf(ID) + " " + title + " " + voteAverage + " " + releaseDate + " " + getImageURL() + " " + getSynopsis();
    }

    protected Movie(Parcel in) {
        imageURL = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        overview = in.readString();
        ID = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageURL);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(overview);
        dest.writeInt(ID);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}