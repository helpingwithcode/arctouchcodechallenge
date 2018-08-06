package com.arctouch.codechallenge.dagger;


import com.arctouch.codechallenge.activities.HomeActivity;
import com.arctouch.codechallenge.activities.MovieDetailsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        PresenterModule.class,
        TmdbApiModule.class,
        AppModule.class
})
public interface AppComponent {
    void inject(HomeActivity homeActivity);
    void inject(MovieDetailsActivity movieDetailsActivity);
}
