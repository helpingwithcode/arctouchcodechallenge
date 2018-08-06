package com.arctouch.codechallenge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.application.MyApplication;
import com.arctouch.codechallenge.adapter.HomeAdapter;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;
import com.arctouch.codechallenge.presenters.HomeActivityPresenter;
import com.arctouch.codechallenge.util.RecyclerViewPaginationListener;
import com.arctouch.codechallenge.views.HomeActivityView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity implements HomeActivityView, HomeAdapter.HomeAdapterItemOnClick {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    HomeActivityPresenter presenter;

    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private int currentPage = 1;
    private HomeAdapter moviesAdapter;
    public final static String MOVIE_INTENT_KEY = "movie";
    public final static String MOVIE_INSTANCE_KEY = "movies";
    public final static String CURRENT_PAGE = "current_page";
    private UpcomingMoviesResponse mMoviesList = new UpcomingMoviesResponse();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ((MyApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        setRecyclerViewPagination();
        presenter.setView(this);
        if(savedInstanceState == null) {
            mMoviesList.results = new ArrayList<>();
            presenter.getFirstInformationLoad();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_INSTANCE_KEY, mMoviesList);
        outState.putInt(CURRENT_PAGE, currentPage);
        Timber.e("onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Timber.e("onRestoreInstanceState");
        if(savedInstanceState == null) return;
        mMoviesList = savedInstanceState.getParcelable(MOVIE_INSTANCE_KEY);
        currentPage = savedInstanceState.getInt(CURRENT_PAGE);
        Timber.e("passing savedInstanceState to showResults");
        showResults(mMoviesList);
    }

    private void setRecyclerViewPagination() {
        moviesAdapter = new HomeAdapter(this);
        recyclerView.setAdapter(moviesAdapter);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerViewPaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                presenter.getUpcomingMovies(currentPage);
            }

            @Override
            public boolean isLoading() { return isLoading; }
        });
    }

    @Override
    public void showResults(UpcomingMoviesResponse upcomingMoviesResponse) {
        isLoading = false;
        moviesAdapter.addResultsToList(upcomingMoviesResponse.results);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        isLoading = false;
        currentPage--;
        Toast.makeText(this, getString(R.string.home_activity_no_more_results), Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveResults(UpcomingMoviesResponse response) {
        mMoviesList.results.addAll(response.results);
    }

    @Override
    public void movieSelected(Movie mMovie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_INTENT_KEY, mMovie);
        startActivity(intent);
    }
}