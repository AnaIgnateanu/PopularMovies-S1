package com.example.android.popularmoviess1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by anamanolache on 4/25/18.
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String  TMDB_URL =
            "https://api.themoviedb.org/3/movie";
    private static final String IMG_BASE_URL =
            "https://image.tmdb.org/t/p/";
    private static final String API_KEY =
            "Your key";


    public static URL buildUrl(String sort_type, Integer page) {
        Uri builtUri = Uri.parse(TMDB_URL).buildUpon()
                    .appendPath(sort_type)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("page", page.toString())
                    .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForPoster(String posterPath, String fileSize) {
        Uri builtUri = Uri.parse(IMG_BASE_URL).buildUpon()
                .appendPath(fileSize)
                .appendPath(posterPath)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    public static byte[] getUrlBytes(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        url.toString());
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public static String getUrlString(URL url) throws IOException {
        return new String(getUrlBytes(url));
    }

}
