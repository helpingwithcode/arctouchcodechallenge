package com.arctouch.codechallenge.presenters;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.views.HomeActivityView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class HomeActivityPresenter {

    TmdbApi api;
    HomeActivityView view;

    public HomeActivityPresenter(TmdbApi api){
        this.api = api;
    }

    public void setView(HomeActivityView view){
        this.view = view;
    }

    public void getUpcomingMovies(int page) {
        Timber.e("getUpcomingMovies(page:%s)", page);
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Timber.e("Response results\n%s", response.results);
                    Timber.e("Response page\n%s", response.page);
                    Timber.e("Response totalResults\n%s", response.totalResults);
                    Timber.e("Response totalPages\n%s", response.totalPages);
                    for (Movie movie : response.results) {
                        movie.genres = new ArrayList<>();
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.genreIds.contains(genre.id)) {
                                movie.genres.add(genre);
                            }
                        }
                    }
                    if(response.results.isEmpty()) view.showError();
                    else view.showResults(response.results);
                });
    }
}
