package com.example.android.popularmoviess1;

import java.util.List;

/**
 * Created by anamanolache on 5/1/18.
 */

public class MovieResults {
    private List<Movie> movies;
    private int totalPages;

    public MovieResults(int totalPages, List<Movie> movies) {
        this.totalPages = totalPages;
        this.movies = movies;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
