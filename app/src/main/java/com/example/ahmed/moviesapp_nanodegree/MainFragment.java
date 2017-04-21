package com.example.ahmed.moviesapp_nanodegree;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ahmed.moviesapp_nanodegree.data.MovieContentProvider;
import com.example.ahmed.moviesapp_nanodegree.data.MovieContract.MovieEntry;
import com.example.ahmed.moviesapp_nanodegree.data.MovieDbHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> , MoviesAdapter.MovieClickListener {

    private static final int MOVIES_LOADER_ID = 1;

    private String mSelectedSort;
    @BindView(R.id.rv_movies_grid) RecyclerView mMoviesRecyclerView;
    @BindView(R.id.pb_loading) ProgressBar mProgressBar;
    @BindView(R.id.internet_error_message)
    TextView mErrorMessage;
    MoviesAdapter mMoviesAdapter;



    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMoviesAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else
            mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));



        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.item_settings:
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivity(i);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUi();
    }

    void updateUi(){

        showLoadingIndicator();
        mSelectedSort = findUserSortOption();


        if(isOnline()) {

            Bundle loaderArgs = new Bundle();
            loaderArgs.putString("sort", mSelectedSort);
            LoaderManager loaderManager = getLoaderManager();
            Loader moviesLoader = loaderManager.getLoader(MOVIES_LOADER_ID);

            showLoadingIndicator();
            if (moviesLoader == null)
                loaderManager.initLoader(MOVIES_LOADER_ID, loaderArgs, this);


            else
                loaderManager.restartLoader(MOVIES_LOADER_ID, loaderArgs, this);
            ;


        }
        else{
            showErrorMessage(getString(R.string.internet_error_message));
        }

    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

        return new MoviesLoader(getContext(), args.getString("sort"));

    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {


        if(data != null && !data.isEmpty()) {
            mMoviesAdapter.updateData(data);
            showResults();
        }

        else{
            if(mSelectedSort.equals(getString(R.string.pref_movies_sort_favorite_value)))
                showErrorMessage(getString(R.string.empty_favorite_movies_message));
            else
                showErrorMessage(getString(R.string.internet_error_message));
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }



    @Override
    public void onMovieClick(int position) {

        Movie clickedMovie = mMoviesAdapter.getMovie(position);
        Intent i = new Intent(getContext(), DetailsActivity.class);
        i.putExtra("movie", clickedMovie);
        startActivity(i);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    void showErrorMessage(String message){
        mProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(message);
        mErrorMessage.setVisibility(View.VISIBLE);

    }

    void showLoadingIndicator(){
        mProgressBar.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    void showResults(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);

    }

    private String findUserSortOption(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return  sharedPreferences.getString(getString(R.string.pref_movies_sort_key), getString(R.string.pref_movies_sort_popularity_value));
    }


}
