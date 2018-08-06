package com.arctouch.codechallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;

public class UpcomingMoviesResponse implements Parcelable {

    public int page;
    public List<Movie> results;
    @Json(name = "total_pages")
    public int totalPages;
    @Json(name = "total_results")
    public int totalResults;

    public UpcomingMoviesResponse(){

    }

    private UpcomingMoviesResponse(Parcel in) {
        page = in.readInt();
        results = in.createTypedArrayList(Movie.CREATOR);
        totalPages = in.readInt();
        totalResults = in.readInt();
    }

    public static final Creator<UpcomingMoviesResponse> CREATOR = new Creator<UpcomingMoviesResponse>() {
        @Override
        public UpcomingMoviesResponse createFromParcel(Parcel in) {
            return new UpcomingMoviesResponse(in);
        }

        @Override
        public UpcomingMoviesResponse[] newArray(int size) {
            return new UpcomingMoviesResponse[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpcomingMoviesResponse)) return false;

        UpcomingMoviesResponse that = (UpcomingMoviesResponse) o;

        if (page != that.page) return false;
        if (totalPages != that.totalPages) return false;
        if (totalResults != that.totalResults) return false;
        return results != null ? results.equals(that.results) : that.results == null;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + (results != null ? results.hashCode() : 0);
        result = 31 * result + totalPages;
        result = 31 * result + totalResults;
        return result;
    }

    @Override
    public String toString() {
        return "UpcomingMoviesResponse{" +
                "page=" + page +
                ", results=" + results +
                ", totalPages=" + totalPages +
                ", totalResults=" + totalResults +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeTypedList(results);
        parcel.writeInt(totalPages);
        parcel.writeInt(totalResults);
    }
}
