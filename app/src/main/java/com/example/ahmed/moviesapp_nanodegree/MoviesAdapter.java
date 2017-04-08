package com.example.ahmed.moviesapp_nanodegree;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 4/8/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> mMovies;
    private MovieClickListener mMovieClickListener;

    interface MovieClickListener {
        void onMovieClick(int position);
    }

    MoviesAdapter(List<Movie> movies, MovieClickListener movieClickListener){
        mMovies = new ArrayList<Movie>();
        mMovies.addAll(movies);
        mMovieClickListener = movieClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        ImageView imageView;
        MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.iv_movie);
        }

        void bind(int position){
            String posterUrl = mMovies.get(position).getFullImageUrlForMainFragment();
            Picasso.with(imageView.getContext()).load(posterUrl).into(imageView);
        }

        @Override
        public void onClick(View view) {
            mMovieClickListener.onMovieClick(getAdapterPosition());
        }
    }

    void updateData(List<Movie> movies){
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();

    }

    Movie getMovie(int position){
        return mMovies.get(position);
    }



}

