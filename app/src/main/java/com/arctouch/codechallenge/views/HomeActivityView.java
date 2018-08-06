package com.arctouch.codechallenge.views;

import com.arctouch.codechallenge.model.MovieSearchResponse;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

public interface HomeActivityView {
    void showResults(UpcomingMoviesResponse upcomingMoviesResponse);
    void showError();
    void showQueryError();
    void loadingStatus(boolean status);
    void saveResults(UpcomingMoviesResponse response);
    void saveQueryResults(MovieSearchResponse movieSearchResponse);
    void showQueryResults(MovieSearchResponse movieSearchResponse);
    void resetMovieList();
    void resetResultView();
}
