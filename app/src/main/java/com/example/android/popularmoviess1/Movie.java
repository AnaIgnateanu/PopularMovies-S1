package com.example.android.popularmoviess1;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

/**
 * Created by anamanolache on 4/25/18.
 */

public class Movie implements Parcelable{
    private  int id;
    private  double voteAverage;
    private  String title;
    private  double popularity;
    private  String synopsis;
    private  String posterPath;
    private String releaseDate;
    private static final String FILE_SIZE_FOR_LIST =
            "w342";
    private static final String FILE_SIZE_FOR_DETAIL =
            "w500";

    public Movie() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public  void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath.substring(1);
    }

    public URL getPosterPathSmall() {
       return NetworkUtils.buildUrlForPoster(posterPath, FILE_SIZE_FOR_LIST);
    }

    public URL getPosterPathBig() {
        return NetworkUtils.buildUrlForPoster(posterPath, FILE_SIZE_FOR_DETAIL);
    }

    @Override
    public String toString() {
        String movieString;
        movieString = "title: "+ getTitle() + " release date: "+ getReleaseDate()+"\n";
        return movieString;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getReleaseDate() {
        return releaseDate;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        voteAverage = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        synopsis = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(voteAverage);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(synopsis);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
