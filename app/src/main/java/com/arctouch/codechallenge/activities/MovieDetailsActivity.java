package com.arctouch.codechallenge.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.application.MyApplication;
import com.arctouch.codechallenge.presenters.MovieDetailsActivityPresenter;
import com.arctouch.codechallenge.views.MovieDetailsActivityView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsActivityView {

    @BindView(R.id.iv_backdrop)
    ImageView backdropIv;
    @BindView(R.id.iv_poster)
    ImageView posterIv;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_genres)
    TextView genresTv;
    @BindView(R.id.tv_release_date)
    TextView releaseDateTv;
    @BindView(R.id.tv_plot)
    TextView plotTv;

    @Inject
    MovieDetailsActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ((MyApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        presenter.setMovie(getIntent());
    }

    @Override
    public void loadImages(String imagePath, int elementId) {
        Glide.with(this)
                .load(imagePath)
                .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into((elementId == R.id.iv_backdrop) ? backdropIv : posterIv);
    }

    @Override
    public void finishActivity() {
        Toast.makeText(this, getString(R.string.movie_details_error), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void setTitle(String title) {
        titleTv.setText(title);
    }

    @Override
    public void setReleaseDate(String releaseDate) {
        releaseDateTv.setText(releaseDate);
    }

    @Override
    public void setPlot(String overview) {
        plotTv.setText(overview);
    }

    @Override
    public void setGenres(String genres) {
        //TODO area of improvement >
        //TODO genres from QueryResults aren't showing, there's someting missing on the parsing from the API
        if(genres.isEmpty()) genresTv.setVisibility(View.GONE);
        else genresTv.setText(genres);
    }
}