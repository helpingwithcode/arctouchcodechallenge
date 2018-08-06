package com.arctouch.codechallenge.views;

import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import java.util.List;

public interface HomeActivityView {
    void showResults(UpcomingMoviesResponse upcomingMoviesResponse);
    void showError();
    void saveResults(UpcomingMoviesResponse response);
}
