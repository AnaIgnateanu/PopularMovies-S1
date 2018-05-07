package com.example.android.popularmoviess1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anamanolache on 4/25/18.
 */

public class JsonUtil {

    private static final String TAG = JsonUtil.class.getSimpleName();


    public static MovieResults fetchMovies(int page, String sortType) {
        List<Movie> items = new ArrayList<>(20);
        String jsonString;
        MovieResults results = new MovieResults(0, items);

        try {
            URL url = NetworkUtils.buildUrl(sortType, page);
            jsonString = NetworkUtils.getUrlString(url);
            JSONObject moviesJson = new JSONObject(jsonString);
            int totalPages = moviesJson.getInt("total_pages");
            parseMovies(items, moviesJson);
            results = new MovieResults(totalPages, items);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return results;
    }


    private static void parseMovies(List<Movie> items, JSONObject moviesJson) throws JSONException {
        JSONArray movies = moviesJson.getJSONArray("results");
        Movie movie;

        for (int i = 0; i < movies.length(); i++) {
            JSONObject movieObject = movies.getJSONObject(i);

            movie = new Movie();
            movie.setId(movieObject.getInt("id"));
            movie.setTitle(movieObject.getString("original_title"));

            if (!movieObject.getString("overview").equals("null") &&
                    !movieObject.getString("overview").isEmpty())
                movie.setSynopsis(movieObject.getString("overview"));

            movie.setVoteAverage(movieObject.getDouble("vote_average"));

            if (!movieObject.getString("poster_path").equals("null") &&
                    !movieObject.getString("poster_path").isEmpty()) {
                movie.setPosterPath(movieObject.getString("poster_path"));
            }

            if (!movieObject.getString("release_date").equals("null") &&
                    !movieObject.getString("release_date").isEmpty())
                movie.setReleaseDate(movieObject.getString("release_date"));
            items.add(movie);
        }
    }
}
