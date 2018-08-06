package com.arctouch.codechallenge.views;

public interface MovieDetailsActivityView {

    void loadImages(String imagePath, int elementId);
    void finishActivity();
    void setTitle(String title);
    void setReleaseDate(String releaseDate);
    void setPlot(String overview);
    void setGenres(String genres);
}
