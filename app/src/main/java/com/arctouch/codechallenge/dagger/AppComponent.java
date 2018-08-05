package com.arctouch.codechallenge.dagger;


import com.arctouch.codechallenge.home.HomeActivity;

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

}
