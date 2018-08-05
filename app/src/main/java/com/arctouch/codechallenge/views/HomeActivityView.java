package com.arctouch.codechallenge.views;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;

public interface HomeActivityView {
    void showResults(List<Movie> movies);
    void showError();
}
