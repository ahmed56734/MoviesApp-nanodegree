package com.example.ahmed.moviesapp_nanodegree.data;

import android.net.Uri;

import java.net.URI;

/**
 * Created by ahmed on 4/19/17.
 */

public class MovieContract  {

    public static final String AUTHORITY = "com.example.ahmed.moviesapp_nanodegree";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final class MovieEntry{

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_IMAGE_URL = "imageUrl";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    }
}
