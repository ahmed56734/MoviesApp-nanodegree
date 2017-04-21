package com.example.ahmed.moviesapp_nanodegree;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by ahmed on 4/21/17.
 */

public class TrailersLoader extends AsyncTaskLoader<List<Trailer>> {


    private static final String trailersBaseUrl = "http://api.themoviedb.org/3/movie/{ID}/videos?api_key="+KEYS.MOVIES_API_KEY;
    private Long movieID;
    TrailersLoader(Context context, Long movieID){
        super(context);
        this.movieID = movieID;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        List<Trailer> result;
        String json;

        json = Utils.downloadJSON(trailersBaseUrl.replace("{ID}", String.valueOf(movieID)));
//        Log.i("json", json);
//        Log.i("id", String.valueOf(movieID));
        result = Utils.parseTrailersJSON(json);

        return result;
    }

}
