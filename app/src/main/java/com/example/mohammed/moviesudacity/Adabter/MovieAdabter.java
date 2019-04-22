package com.example.mohammed.moviesudacity.Adabter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.mohammed.moviesudacity.Activity.DetailsMovieActivity;
import com.example.mohammed.moviesudacity.R;
import com.example.mohammed.moviesudacity.data.FavMovieEntity;
import com.example.mohammed.moviesudacity.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mohammed.moviesudacity.util.CONST.BASE_IMDB_URL;
import static com.example.mohammed.moviesudacity.util.CONST.BASE_POSTERS_URL;
import static com.example.mohammed.moviesudacity.util.CONST.EXTRA_MOVIE;

public class MovieAdabter extends RecyclerView.Adapter<MovieAdabter.ViewHolder> {
    @NonNull

    private List<Movie> movieList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public MovieAdabter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.movieList = new ArrayList<>();
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_first_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movieList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = ViewHolder.class.getSimpleName();
        @BindView(R.id.imv_movie_first_photo)
        ImageView moviePosterImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void bind(Movie movie, OnItemClickListener onItemClickListener) {
            String posterUrl = BASE_POSTERS_URL + movie.getPosterPath();
            Picasso.get().load(posterUrl).into(moviePosterImageView);
            itemView.setOnClickListener(view -> {
                onItemClickListener.onItemClick(movie);
            });

        }
    }

    public void add(Movie movie) {
        movieList.add(movie);
        notifyItemInserted(movieList.size() - 1);
    }

    public void clear() {
        movieList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movieList) {
        if (movieList != null) {
            for (Movie movie : movieList) {
                add(movie);
            }
        }
    }

        public void addAllFav(List<FavMovieEntity> favMovieEntities) {
        for (FavMovieEntity favMovieEntity : favMovieEntities) {
            add(new Movie(favMovieEntity.getMovieId(), favMovieEntity.getMovieName(), favMovieEntity.getMoviePoster(),favMovieEntity.getReleaseDate(),Double.parseDouble(favMovieEntity.getVote())));
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }


}