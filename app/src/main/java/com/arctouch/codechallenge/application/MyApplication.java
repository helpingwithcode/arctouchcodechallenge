package com.arctouch.codechallenge.application;

import android.app.Application;

import com.arctouch.codechallenge.dagger.AppComponent;
import com.arctouch.codechallenge.dagger.AppModule;
import com.arctouch.codechallenge.dagger.DaggerAppComponent;
import com.arctouch.codechallenge.dagger.PresenterModule;
import com.arctouch.codechallenge.dagger.TmdbApiModule;

import timber.log.Timber;

public class MyApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        component = DaggerAppComponent.builder()
                .tmdbApiModule(new TmdbApiModule())
                .presenterModule(new PresenterModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return component;
    }
}
