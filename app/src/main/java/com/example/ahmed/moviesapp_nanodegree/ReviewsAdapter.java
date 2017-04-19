package com.example.ahmed.moviesapp_nanodegree;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 4/19/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    List<Review> mReviews;

    ReviewsAdapter(){
        mReviews = new ArrayList<Review>();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);

        holder.authorNameTextView.setText(review.getAuthor());
        holder.reviewTextView.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView authorNameTextView;
        TextView reviewTextView;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorNameTextView = (TextView) itemView.findViewById(R.id.movie_review_author_name);
            reviewTextView = (TextView) itemView.findViewById(R.id.movie_review);
        }


    }

    void updateData(List<Review> reviews){
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();

    }
}
