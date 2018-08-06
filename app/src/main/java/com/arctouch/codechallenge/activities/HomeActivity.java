package com.arctouch.codechallenge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.application.MyApplication;
import com.arctouch.codechallenge.adapter.HomeAdapter;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.MovieSearchResponse;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;
import com.arctouch.codechallenge.presenters.HomeActivityPresenter;
import com.arctouch.codechallenge.util.RecyclerViewPaginationListener;
import com.arctouch.codechallenge.views.HomeActivityView;
import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeActivityView, HomeAdapter.HomeAdapterItemOnClick {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.sv_query)
    SearchView querySv;

    @Inject
    HomeActivityPresenter presenter;

    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private int currentPage = 1;
    private HomeAdapter moviesAdapter;
    public final static String MOVIE_KEY = "movie";
    public final static String MOVIES_KEY = "movies";
    public final static String QUERY_KEY = "query";
    public final static String CURRENT_PAGE_KEY = "current_page";
    public final static String SHOWING_QUERY_KEY = "showing_query";
    private UpcomingMoviesResponse mMoviesList = new UpcomingMoviesResponse();
    private MovieSearchResponse mQueryList = new MovieSearchResponse();
    private String lastQuery;
    private boolean showingQueryResults = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ((MyApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        setRecyclerViewPagination();
        setSearchView();
        presenter.setView(this);
        presenter.checkSavedInstanceState(savedInstanceState);
    }

    private void setSearchView() {
        querySv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.equals(lastQuery)) {
                    lastQuery = query;
                    showingQueryResults = true;
                    presenter.queryMovie(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        querySv.setOnCloseListener(() -> {
            //TODO area of improvement >
            //TODO this logic should be handled by the presenter
            showingQueryResults = false;
            mQueryList.setResults(new ArrayList<>());
            resetResultView();
            return false;
        });
    }

    @Override
    public void resetResultView() {
        moviesAdapter.reset();
        if(mMoviesList.results != null) moviesAdapter.addResultsToList(mMoviesList.results);
        loadingStatus(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIES_KEY, mMoviesList);
        outState.putParcelable(QUERY_KEY, mQueryList);
        outState.putInt(CURRENT_PAGE_KEY, currentPage);
        outState.putBoolean(SHOWING_QUERY_KEY, showingQueryResults);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState == null) return;
        mMoviesList = savedInstanceState.getParcelable(MOVIES_KEY);
        mQueryList = savedInstanceState.getParcelable(QUERY_KEY);
        currentPage = savedInstanceState.getInt(CURRENT_PAGE_KEY);
        showingQueryResults = savedInstanceState.getBoolean(SHOWING_QUERY_KEY);
        if (mQueryList.getResults() == null || mQueryList.getResults().isEmpty()) showResults(mMoviesList);
        else showQueryResults(mQueryList);
    }

    private void setRecyclerViewPagination() {
        moviesAdapter = new HomeAdapter(this);
        recyclerView.setAdapter(moviesAdapter);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerViewPaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if(showingQueryResults) return;
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
        loadingStatus(false);
    }

    @Override
    public void loadingStatus(boolean status){
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(!status ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError() {
        isLoading = false;
        currentPage--;
        Toast.makeText(this, getString(R.string.home_activity_no_more_results), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showQueryError() {
        Toast.makeText(this, getString(R.string.home_activity_no_results), Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveResults(UpcomingMoviesResponse response) {
        mMoviesList.results.addAll(response.results);
    }

    @Override
    public void saveQueryResults(MovieSearchResponse movieSearchResponse) {
        mQueryList.setResults(movieSearchResponse.getResults());
    }

    @Override
    public void showQueryResults(MovieSearchResponse movieSearchResponse) {
        moviesAdapter.addQueryResults(movieSearchResponse.getResults());
        loadingStatus(false);
    }

    @Override
    public void resetMovieList() {
        mMoviesList.results = new ArrayList<>();
    }

    @Override
    public void movieSelected(Movie mMovie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_KEY, mMovie);
        startActivity(intent);
    }
}