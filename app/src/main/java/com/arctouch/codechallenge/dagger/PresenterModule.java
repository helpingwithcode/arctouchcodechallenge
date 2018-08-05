package com.arctouch.codechallenge.dagger;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.presenters.HomeActivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    HomeActivityPresenter providesHomeActivityPresenter(TmdbApi tmdbApi){
        return new HomeActivityPresenter(tmdbApi);
    }

}
