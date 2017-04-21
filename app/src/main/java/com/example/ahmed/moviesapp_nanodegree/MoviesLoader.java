package com.example.ahmed.moviesapp_nanodegree;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.ahmed.moviesapp_nanodegree.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 4/8/17.
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {


    private static final String popularityURL = "http://api.themoviedb.org/3/movie/popular?api_key="+KEYS.MOVIES_API_KEY;
    private static final String topRatedURL = "http://api.themoviedb.org/3/movie/top_rated?api_key="+KEYS.MOVIES_API_KEY;
    private String sort;

    MoviesLoader(Context context, String sort){
        super(context);
        this.sort = sort;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {


        if(sort.equals(getContext().getString(R.string.pref_movies_sort_favorite_value))){
            List<Movie> movies = new ArrayList<Movie>();
            Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

            if(cursor == null)
                return null;
            while (cursor.moveToNext()){
                Long id = cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                String synopsis = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS));
                String voteAverage = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
                String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                String imageUrl = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL));
                Movie movie = new Movie(id, title, voteAverage, releaseDate, imageUrl, synopsis);
                Log.d("favorite movie", movie.toString());

                movies.add(movie);
            }

            cursor.close();
            return movies;

        }

        else {
            String url;
            if (sort.equals(getContext().getString(R.string.pref_movies_sort_popularity_value)))
                url = popularityURL;
            else if (sort.equals(getContext().getString(R.string.pref_movies_sort_toprated_value)))
                url = topRatedURL;

            else
                url = topRatedURL;

            String json = Utils.downloadJSON(url);
            return Utils.parseMoviesJSON(json);
        }
    }

}