package com.arctouch.codechallenge.presenters;

import android.os.Bundle;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.views.HomeActivityView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivityPresenter {

    TmdbApi api;
    HomeActivityView view;
    int FIRST_PAGE = 1;

    public HomeActivityPresenter(TmdbApi api) {
        this.api = api;
    }

    public void setView(HomeActivityView view) {
        this.view = view;
    }

    public void getUpcomingMovies(int page) {
        view.loadingStatus(true);
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for (Movie movie : response.results) {
                        movie.genres = new ArrayList<>();
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.genreIds.contains(genre.id)) {
                                movie.genres.add(genre);
                            }
                        }
                    }
                    if (response.results.isEmpty()) view.showError();
                    else {
                        view.showResults(response);
                        view.saveResults(response);
                    }
                });
    }

    public void loadGenresInformation() {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                });
    }

    public void getFirstInformationLoad() {
        if (Cache.getGenres().isEmpty()) loadGenresInformation();
        getUpcomingMovies(FIRST_PAGE);
    }

    public void queryMovie(String query) {
        view.loadingStatus(true);
        api.queryMovies(TmdbApi.API_KEY, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieSearchResponse -> {
                    if (movieSearchResponse.getResults().isEmpty()) {
                        view.showQueryError();
                        view.resetResultView();
                    }
                    else {
                        view.showQueryResults(movieSearchResponse);
                        view.saveQueryResults(movieSearchResponse);
                    }
                });
    }

    public void checkSavedInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) return;
            view.resetMovieList();
            getFirstInformationLoad();
    }
}
