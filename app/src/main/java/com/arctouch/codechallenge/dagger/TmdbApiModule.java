package com.arctouch.codechallenge.dagger;

import com.arctouch.codechallenge.api.TmdbApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class TmdbApiModule {

    @Provides
    TmdbApi providesTheMovieDataBaseAPI(){
        return provideRetrofit(TmdbApi.URL).create(TmdbApi.class);
    }

    @Provides
    public Retrofit provideRetrofit(String baseURL){
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
