package com.arctouch.codechallenge.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;

import timber.log.Timber;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getMovie();
    }

    private void getMovie() {
        mMovie = getIntent().getExtras().getParcelable("movie");
        Timber.e("Movie: %s", mMovie.toString());
    }
}
