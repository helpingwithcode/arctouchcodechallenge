package com.arctouch.codechallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MovieSearchResponse implements Parcelable{
    private List<Movie> results;

    public MovieSearchResponse(){

    }

    protected MovieSearchResponse(Parcel in) {
        results = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Creator<MovieSearchResponse> CREATOR = new Creator<MovieSearchResponse>() {
        @Override
        public MovieSearchResponse createFromParcel(Parcel in) {
            return new MovieSearchResponse(in);
        }

        @Override
        public MovieSearchResponse[] newArray(int size) {
            return new MovieSearchResponse[size];
        }
    };

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(results);
    }
}
