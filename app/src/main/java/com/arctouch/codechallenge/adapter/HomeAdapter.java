package com.arctouch.codechallenge.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    HomeAdapterItemOnClick adapterItemOnClick;

    public HomeAdapter(HomeAdapterItemOnClick adapterItemOnClick) {
        this.adapterItemOnClick = adapterItemOnClick;
    }

    public void addQueryResults(List<Movie> results) {
        movies = new ArrayList<>();
        for(Movie movie : results) movies.add(movie);
        notifyDataSetChanged();
    }

    public void reset() {
        movies = new ArrayList<>();
        notifyDataSetChanged();
    }

    public interface HomeAdapterItemOnClick {
        void movieSelected(Movie mMovie);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    public void addResultsToList(List<Movie> newMovies){
        for(Movie movie : newMovies){
            movies.add(movie);
            notifyItemInserted(movies.size() - 1);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();
        private Movie thisMovie;

        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.genresTextView)
        TextView genresTextView;
        @BindView(R.id.releaseDateTextView)
        TextView releaseDateTextView;
        @BindView(R.id.posterImageView)
        ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            setMovie(movie);
            titleTextView.setText(movie.title);
            if(movie.genres != null) genresTextView.setText(TextUtils.join(", ", movie.genres));
            releaseDateTextView.setText(movie.releaseDate);
            String posterPath = movie.posterPath;
            if (!TextUtils.isEmpty(posterPath)) {
                Glide.with(itemView)
                        .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(posterImageView);
            }
        }

        private void setMovie(Movie movie) {
            this.thisMovie = movie;
        }

        @Override
        public void onClick(View view) {
            adapterItemOnClick.movieSelected(this.thisMovie);
        }
    }
}
