package com.example.ahmed.moviesapp_nanodegree;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 4/8/17.
 */

public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();

    public static String downloadJSON(String urlString){

        String json = "";
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        URL url;


        try {
            url = new URL(urlString);

        }

        catch (MalformedURLException e){
            Log.e(LOG_TAG, "error creating url", e);
            return json;
        }

        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();


            if (httpURLConnection.getResponseCode() == 200){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();

                while (line != null){
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }


                json = stringBuilder.toString();
            }

            else
                Log.e(LOG_TAG, "http response " + httpURLConnection.getResponseCode());

        }catch (IOException e){
            Log.e(LOG_TAG, "error creating http connection", e);
            return json;

        }




        return json;
    }

    public static List<Movie> parseMoviesJSON(String json){

        List<Movie> movies = new ArrayList<>();

        if (json == null || json.isEmpty())
            return null;

        try {
            JSONObject rootJSON = new JSONObject(json);
            JSONArray moviesResultArray = rootJSON.getJSONArray("results");



            for(int i = 0, n = moviesResultArray.length(); i < n ; i++){

                JSONObject movieJSON = moviesResultArray.getJSONObject(i);

                String posterPath = movieJSON.getString("poster_path");
                String overview = movieJSON.getString("overview");
                String releaseDate = movieJSON.getString("release_date");
                String title = movieJSON.getString("original_title");
                String voteAverage = movieJSON.getString("vote_average");
                Long ID = movieJSON.getLong("id");

                movies.add(new Movie(ID, title, voteAverage, releaseDate, posterPath, overview));
            }

        }catch (JSONException e){
            Log.e(LOG_TAG, "error parsing json", e);
        }

        return movies;
    }



    public static List<Review> parseReviewsJSON(String json){

        List<Review> reviews = new ArrayList<>();

        if (json == null || json.isEmpty())
            return null;

        try {

            JSONObject rootJSON = new JSONObject(json);
            JSONArray reviewsResultArray = rootJSON.getJSONArray("results");

            for(int i = 0, n = reviewsResultArray.length(); i < n ; i++){

                JSONObject reviewJSON = reviewsResultArray.getJSONObject(i);
                String review = reviewJSON.getString("content");
                String author = reviewJSON.getString("author");

                reviews.add(new Review(author, review));
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "error parsing json", e);
        }

        return reviews;
    }

//    public static List<Trailer> parseTrailersJSON(String json){
//
//        List<Trailer> trailers = new ArrayList<>();
//
//        if (json == null || json.isEmpty())
//            return null;
//
//        try {
//
//            JSONObject rootJSON = new JSONObject(json);
//            JSONArray trailersResultArray = rootJSON.getJSONArray("results");
//
//            for(int i = 0, n = trailersResultArray.length(); i < n ; i++){
//
//                JSONObject trailerJSON = trailersResultArray.getJSONObject(i);
//                String trailerName = trailerJSON.getString("name");
//                String trailerUrl = trailerJSON.getString("key");
//
//                trailers.add(new Trailer(trailerName, trailerUrl));
//            }
//
//        } catch (JSONException e){
//            Log.e(LOG_TAG, "error parsing json", e);
//        }
//
//        return trailers;
//    }
}