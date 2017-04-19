package com.example.ahmed.moviesapp_nanodegree;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by ahmed on 4/8/17.
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {


    String url;

    MoviesLoader(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        String json = Utils.downloadJSON(url);
        return Utils.parseMoviesJSON(json);
    }
}