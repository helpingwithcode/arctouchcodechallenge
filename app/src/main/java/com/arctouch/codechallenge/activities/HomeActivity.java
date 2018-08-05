package com.arctouch.codechallenge.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.arctouch.codechallenge.presenters.HomeActivityPresenter;
import com.arctouch.codechallenge.util.RecyclerViewPaginationListener;
import com.arctouch.codechallenge.views.HomeActivityView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ((MyApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        setRecyclerViewPagination();
        presenter.setView(this);
        presenter.getFirstInformationLoad();
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
    public void showResults(List<Movie> movies) {
        isLoading = false;
        moviesAdapter.addResultsToList(movies);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        isLoading = false;
        currentPage--;
        Toast.makeText(this, "There was a problem getting the movies, try again later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void movieSelected(Movie mMovie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", mMovie);
        startActivity(intent);
    }
}
