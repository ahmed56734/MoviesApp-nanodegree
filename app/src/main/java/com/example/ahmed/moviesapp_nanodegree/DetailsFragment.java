package com.example.ahmed.moviesapp_nanodegree;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment {

    private Movie mMovie;

    @BindView(R.id.movie_poster)
    ImageView posterImageView;
    @BindView(R.id.movie_release_year)
    TextView releaseYearTextView;
    @BindView(R.id.movie_vote_average) TextView voteAverageTextView;
    @BindView(R.id.movie_synopsis) TextView synopsisTextView;
    @BindView(R.id.movie_title) TextView titleTextView;





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

        return rootView;


    }

}
