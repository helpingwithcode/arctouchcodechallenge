package com.arctouch.codechallenge.dagger;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.arctouch.codechallenge.util.MovieImageUrlBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return application;
    }

    @Provides
    Resources providesResources(){
        return application.getResources();
    }

    @Provides
    MovieImageUrlBuilder providesMovieImageUrlBuilder(){
        return new MovieImageUrlBuilder();
    }
}
