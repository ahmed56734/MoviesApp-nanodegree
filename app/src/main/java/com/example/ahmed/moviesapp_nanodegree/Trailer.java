package com.example.ahmed.moviesapp_nanodegree;

/**
 * Created by ahmed on 4/21/17.
 */

public class Trailer {
    private String name;
    private String url;

    public Trailer(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
