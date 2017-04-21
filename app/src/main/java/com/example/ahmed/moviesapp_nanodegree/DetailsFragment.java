package com.example.ahmed.moviesapp_nanodegree;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.preference.AndroidResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.moviesapp_nanodegree.data.MovieContract.MovieEntry;
import com.example.ahmed.moviesapp_nanodegree.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment implements TrailersAdapter.TrailerClickListener {

    private Movie mMovie;
    private static int REVIEWS_LOADER_ID = 2;
    private static int TRAILER_LOADER_ID = 3;
    private Boolean moviesIsInFavorites;
    @BindView(R.id.movie_poster)
    ImageView posterImageView;
    @BindView(R.id.movie_release_year)
    TextView releaseYearTextView;
    @BindView(R.id.movie_vote_average) TextView voteAverageTextView;
    @BindView(R.id.movie_synopsis) TextView synopsisTextView;
    @BindView(R.id.movie_title) TextView titleTextView;
    @BindView(R.id.rv_reviews) RecyclerView mReviewsRecyclerView;
    ReviewsAdapter mReviewsAdapter;
    @BindView(R.id.rv_trailers) RecyclerView mTrailersRecyclerView;
    TrailersAdapter mTrailersAdapter;





    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Movie movie){

        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);

        return detailsFragment;
    }


    private Movie getMovie(){
        return getArguments().getParcelable("movie");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMovie = getMovie();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        moviesIsInFavorites = isMovieInFavorites();
        MenuItem favoriteIcon = menu.findItem(R.id.ic_favorite);

        if(moviesIsInFavorites)
            favoriteIcon.setIcon(android.R.drawable.star_big_on);
        else
            favoriteIcon.setIcon(android.R.drawable.star_big_off);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.ic_favorite){
            if(moviesIsInFavorites){ //delete from database
                getContext().getContentResolver().delete(ContentUris.withAppendedId(MovieEntry.CONTENT_URI, mMovie.getID()),null,null);
                item.setIcon(android.R.drawable.star_big_off);
                moviesIsInFavorites = false;
                Toast.makeText(getContext(), "Movie removed from your favorites", Toast.LENGTH_SHORT).show();
            }
            else{
                getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, makeMovieContentValues());
                item.setIcon(android.R.drawable.star_big_on);
                moviesIsInFavorites = true;
                Toast.makeText(getContext(), "Movie added to your favorites", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);

        Picasso.with(getContext()).load(mMovie.getFullImageUrlForMainFragment()).into(posterImageView);
        releaseYearTextView.setText(mMovie.getReleaseYear());
        voteAverageTextView.setText(mMovie.getVoteAverage()+"/10");
        synopsisTextView.setText(mMovie.getSynopsis());
        titleTextView.setText(mMovie.getTitle());

        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);




        return rootView;


    }

    @Override
    public void onStart() {
        super.onStart();

        getLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailersLoaderListner);
        getLoaderManager().initLoader(REVIEWS_LOADER_ID, null, reviewsLoaderListner);


    }

    ///////////////////// reviews loader callbacks
    private LoaderManager.LoaderCallbacks<List<Review>> reviewsLoaderListner = new LoaderManager.LoaderCallbacks<List<Review>>(){
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            return new ReviewsLoader(getContext(), mMovie.getID());
        }


        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {

            mReviewsAdapter.updateData(data);

        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };

    ////////////////////// trailers loader callbacks
    private LoaderManager.LoaderCallbacks<List<Trailer>> trailersLoaderListner = new LoaderManager.LoaderCallbacks<List<Trailer>>(){
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            return new TrailersLoader(getContext(), mMovie.getID());
        }


        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {

            mTrailersAdapter.updateData(data);

        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };

    private boolean isMovieInFavorites(){
        return new MovieDbHelper(getContext()).checkIfRecordAlreadyExists(String.valueOf(mMovie.getID()));
    }

    private ContentValues makeMovieContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_ID, mMovie.getID());
        contentValues.put(MovieEntry.COLUMN_IMAGE_URL, mMovie.getImageURL());
        contentValues.put(MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        contentValues.put(MovieEntry.COLUMN_SYNOPSIS, mMovie.getSynopsis());
        contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());

        return contentValues;
    }


    @Override
    public void onTrailerClick(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+url)));

    }
}
