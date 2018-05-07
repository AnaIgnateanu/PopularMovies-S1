package com.example.android.popularmoviess1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;


enum SortingType {
    TOP_RATED("top_rated"),
    POPULARITY("popular");

    private String type;

    SortingType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

public class MainActivity extends AppCompatActivity implements
            PosterAdapter.PosterAdapterOnClickHandler,
            LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int POSTER_LOADER_ID = 453;

    private RecyclerView mPhotoRecyclerView;
    private PosterAdapter mPhotoAdapter;

    private static ScreenState state = ScreenState.INITIAL;
    private static SortingType sortingType = SortingType.POPULARITY;

    private MoviesStore moviesStore = MoviesStore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoRecyclerView = findViewById(R.id.recyclerview_movie);
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mPhotoRecyclerView.setLayoutManager(layoutManager);
        mPhotoRecyclerView.setHasFixedSize(true);


        mPhotoAdapter = new PosterAdapter(this);
        mPhotoAdapter.setMovieData(moviesStore.getMovieItems());
        mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        if (isOnline()) {
            loadMovies();
        }
        else
            displayConnectivityToast();
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> movies;

            @Override
            protected void onStartLoading() {
                if (movies != null)
                    deliverResult(movies);
                else
                    forceLoad();
            }

            @Override
            public List<Movie> loadInBackground() {
                try {
                    MovieResults results = JsonUtil.fetchMovies(moviesStore.getPage(), sortingType.getType());
                    movies = results.getMovies();
                    if (moviesStore.getPage() == 1)
                        moviesStore.setTotalPages(results.getTotalPages());
                    return movies;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            public void deliverResult(List<Movie> items) {
                movies = items;
                super.deliverResult(items);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if (data != null) {
            moviesStore.getMovieItems().addAll(data);
            mPhotoAdapter.notifyDataSetChanged();
        }
        state = ScreenState.CONTENT;
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_top_rated:
                sortingType = SortingType.TOP_RATED;
                if (isOnline())
                    reloadMovies();
                else
                   displayConnectivityToast();
                return true;
            case R.id.menu_item_popularity:
                sortingType = SortingType.POPULARITY;
                if (isOnline())
                    reloadMovies();
                else
                    displayConnectivityToast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadMovies() {
        final LoaderCallbacks<List<Movie>> callback = MainActivity.this;

        final Bundle bundle = null;

        if (state == ScreenState.INITIAL) {
            state = ScreenState.LOADING;
            getSupportLoaderManager().initLoader(POSTER_LOADER_ID, bundle, callback);
        }
        else if (state == ScreenState.RESTARTED) {
            state = ScreenState.LOADING;
            moviesStore.setPage(moviesStore.getPage() + 1);
            getSupportLoaderManager()
                    .restartLoader(POSTER_LOADER_ID, bundle, callback);
        }

        mPhotoAdapter.setOnBottomReachedListener(
                new OnBottomReachedListener() {
                    @Override
                    public void onBottomReached(int position) {
                        if (state == ScreenState.CONTENT && moviesStore.getPage() < moviesStore.getTotalPages()) {
                            state = ScreenState.LOADING;
                            moviesStore.setPage(moviesStore.getPage() + 1);
                            getSupportLoaderManager()
                                    .restartLoader(POSTER_LOADER_ID, bundle, callback);
                        }
                    }
                }
        );
        setActivityTitle();
    }

    private void reloadMovies() {
        moviesStore.resetData();
        state = ScreenState.RESTARTED;
        loadMovies();
        mPhotoAdapter.setMovieData(moviesStore.getMovieItems());
        mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        setActivityTitle();
    }

    private void setActivityTitle() {
        if (sortingType == SortingType.POPULARITY)
            setTitle(getResources().getString(R.string.app_name) + " - "
                    + getResources().getString(R.string.sort_by_popularity));
        else
            setTitle(getResources().getString(R.string.app_name) + " - "
                    +getResources().getString(R.string.sort_by_top_rated));
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void displayConnectivityToast() {
        Toast.makeText(this, getString(R.string.connectivity_display_message), Toast.LENGTH_SHORT)
                .show();
    }


}
