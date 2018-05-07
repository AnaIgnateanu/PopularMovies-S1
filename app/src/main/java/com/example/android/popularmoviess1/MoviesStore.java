package com.example.android.popularmoviess1;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anamanolache on 5/1/18.
 */

public class MoviesStore {
    private static final String TAG = MoviesStore.class.getSimpleName();
    private List<Movie> mMovieItems;
    private static MoviesStore instance = new MoviesStore();
    private int page = 1;
    private int totalPages;

    private MoviesStore() {
        mMovieItems = new ArrayList<>(20);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovieItems() {
        return mMovieItems;
    }

    public static MoviesStore getInstance() {
        return instance;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void resetData() {
        mMovieItems.clear();
        setPage(0);
    }
}
