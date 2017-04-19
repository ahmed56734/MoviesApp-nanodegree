package com.example.ahmed.moviesapp_nanodegree;

/**
 * Created by ahmed on 4/19/17.
 */

public class Review {
    private String author;
    private String review;

    public Review(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

    @Override
    public String toString() {
        return author + " " + review ;
    }
}
