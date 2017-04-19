package com.example.ahmed.moviesapp_nanodegree;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment {

    private Movie mMovie;
    private static int REVIEWS_LOADER_ID = 2;
    @BindView(R.id.movie_poster)
    ImageView posterImageView;
    @BindView(R.id.movie_release_year)
    TextView releaseYearTextView;
    @BindView(R.id.movie_vote_average) TextView voteAverageTextView;
    @BindView(R.id.movie_synopsis) TextView synopsisTextView;
    @BindView(R.id.movie_title) TextView titleTextView;
    @BindView(R.id.rv_reviews) RecyclerView mReviewsRecyclerView;
    ReviewsAdapter mReviewsAdapter;





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
        mMovie = getMovie();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);

        Picasso.with(getContext()).load(mMovie.getFullImageUrlForMainFragment()).into(posterImageView);
        releaseYearTextView.setText(mMovie.getReleaseYear());
        voteAverageTextView.setText(mMovie.getVoteAverage());
        synopsisTextView.setText(mMovie.getSynopsis());
        titleTextView.setText(mMovie.getTitle());

        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);


        return rootView;


    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(REVIEWS_LOADER_ID, null, reviewsLoaderListner);
    }

    private LoaderManager.LoaderCallbacks<List<Review>> reviewsLoaderListner = new LoaderManager.LoaderCallbacks<List<Review>>(){
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            return new ReviewsLoader(getContext(), mMovie.getID());
        }


        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {

            mReviewsAdapter.updateData(data);
            for (Review review : data)
                Log.d("fragmentdetails", review.toString() + "\n");

        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };


}
