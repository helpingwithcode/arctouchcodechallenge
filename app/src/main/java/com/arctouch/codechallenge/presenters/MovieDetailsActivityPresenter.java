package com.arctouch.codechallenge.presenters;

import android.content.Intent;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.arctouch.codechallenge.views.MovieDetailsActivityView;
import java.util.List;
import static com.arctouch.codechallenge.activities.HomeActivity.MOVIE_INTENT_KEY;

public class MovieDetailsActivityPresenter {

    private static final String COMMA = ", ";
    private static final String LAST_SEPARATOR = " and ";
    MovieImageUrlBuilder movieImageUrlBuilder;
    MovieDetailsActivityView view;
    Movie mMovie;

    public MovieDetailsActivityPresenter(MovieImageUrlBuilder movieImageUrlBuilder){
        this.movieImageUrlBuilder = movieImageUrlBuilder;
    }

    public void setMovie(Intent intent){
        try{
            mMovie = intent.getExtras().getParcelable(MOVIE_INTENT_KEY);
            getInfo();
        }
        catch (Exception e){
            view.finishActivity();
        }
    }

    private void getInfo() {
        view.loadImages(movieImageUrlBuilder.buildBackdropUrl(mMovie.backdropPath), R.id.iv_backdrop);
        view.loadImages(movieImageUrlBuilder.buildPosterUrl(mMovie.posterPath), R.id.iv_poster);
        view.setTitle(mMovie.title);
        view.setReleaseDate(mMovie.releaseDate);
        view.setPlot(mMovie.overview);
        view.setGenres(parseGenres(mMovie.genres));
    }

    private String parseGenres(List<Genre> genres) {
        String movieGenres = "";
        for(int i = 0; i < genres.size(); i++){
            movieGenres += genres.get(i);
            if(genres.size() == 1) break;
            if(i < genres.size() - 2) movieGenres += COMMA;
            if(i == genres.size() - 2) movieGenres += LAST_SEPARATOR;
        }
        return movieGenres;
    }

    public void setView(MovieDetailsActivityView view){
        this.view = view;
    }
}