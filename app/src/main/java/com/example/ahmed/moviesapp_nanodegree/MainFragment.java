package com.example.ahmed.moviesapp_nanodegree;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> , MoviesAdapter.MovieClickListener {

    private static final int MOVIES_LOADER_ID = 1;

    private static final String popularityURL = "http://api.themoviedb.org/3/movie/popular?api_key="+KEYS.MOVIES_API_KEY;
    private static final String topRatedURL = "http://api.themoviedb.org/3/movie/top_rated?api_key="+KEYS.MOVIES_API_KEY;
    private String selectedSort = popularityURL;
    @BindView(R.id.rv_movies_grid)
    RecyclerView mMoviesRecyclerView;
    @BindView(R.id.pb_loading)
    ProgressBar progressBar;
    @BindView(R.id.internet_error_message)
    TextView errorMessage;
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
        mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Log.d("mainfragment", "onCreateView");
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
            case R.id.item_most_popular:
                selectedSort = popularityURL;
                updateUi();
                return true;

            case R.id.item_top_reated:
                selectedSort = topRatedURL;
                updateUi();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("mainfragment", "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("mainfragment", "onStart");
        updateUi();
    }

    void updateUi(){

        if(isOnline()) {
            Bundle loaderArgs = new Bundle();
            loaderArgs.putString("url", selectedSort);
            LoaderManager loaderManager = getLoaderManager();
            Loader moviesLoader = loaderManager.getLoader(MOVIES_LOADER_ID);
            Log.d("sort", selectedSort);

            if (moviesLoader == null)
                loaderManager.initLoader(MOVIES_LOADER_ID, loaderArgs, this);


            else
                loaderManager.restartLoader(MOVIES_LOADER_ID, loaderArgs, this);
        }
        else{
            mMoviesRecyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

        Log.d("mainfragment", "ONCreateLoader");
        return new MoviesLoader(getContext(), args.getString("url"));

    }



    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

        Log.d("mainfragment", "onLoadFinished");

        if(data != null) {

            for (Movie movie : data)
                Log.d("movie", movie.toString());

            mMoviesAdapter.updateData(data);

            progressBar.setVisibility(View.GONE);
            mMoviesRecyclerView.setVisibility(View.VISIBLE);
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


}
