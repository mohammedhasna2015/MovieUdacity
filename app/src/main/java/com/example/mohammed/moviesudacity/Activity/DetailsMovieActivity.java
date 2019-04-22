package com.example.mohammed.moviesudacity.Activity;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.mohammed.moviesudacity.Adabter.MoviesReviewsAdapter;
import com.example.mohammed.moviesudacity.Adabter.MoviesTrailersAdapter;
import com.example.mohammed.moviesudacity.R;
import com.example.mohammed.moviesudacity.data.FavMovieEntity;
import com.example.mohammed.moviesudacity.data.MoviesDatabase;
import com.example.mohammed.moviesudacity.model.Movie;
import com.example.mohammed.moviesudacity.model.MovieReviewsEntity;
import com.example.mohammed.moviesudacity.model.MovieVideosEntity;
import com.example.mohammed.moviesudacity.network.APIClient;
import com.example.mohammed.moviesudacity.network.ApiRequests;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mohammed.moviesudacity.util.CONST.BASE_POSTERS_URL;
import static com.example.mohammed.moviesudacity.util.CONST.EXTRA_MOVIE;
import static com.example.mohammed.moviesudacity.util.IntentsUtill.watchYoutubeVideo;


public class DetailsMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailsMovieActivity.class.getSimpleName();

    @BindView(R.id.tv_movie_name)
    TextView mNameTextView;
    @BindView(R.id.iv_movie_poster)
    ImageView mPosterImageView;
    @BindView(R.id.tv_movie_release_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.tv_movie_user_rating)
    TextView mUserRatingTextView;
    @BindView(R.id.tv_movie_length)
    TextView movieLengthTextView;
    @BindView(R.id.tv_movie_overview)
    TextView mOverviewTextView;
    @BindView(R.id.rv_trailers)
    RecyclerView mTrailersRecyclerView;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsRecyclerView;
    @BindView(R.id.favoriteFab)
    FloatingActionButton fab;

    private ApiRequests apiService = APIClient.getClient().create(ApiRequests.class);
    private MoviesTrailersAdapter mTrailersAdapter;
    private MoviesReviewsAdapter mReviewsAdapter;
    private MoviesDatabase mDb;

    private Movie movie;
    private int movieId;
    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        ButterKnife.bind(this);
        mDb = MoviesDatabase.getsInstance(this);

        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailersAdapter = new MoviesTrailersAdapter(this,
                trailer -> watchYoutubeVideo(DetailsMovieActivity.this, trailer.getKey()));
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);

        mReviewsAdapter = new MoviesReviewsAdapter(this);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);

        movie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
        movieId = movie.getId();

        mNameTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mUserRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());
        Glide.with(this).load(BASE_POSTERS_URL + movie.getPosterPath()).into(mPosterImageView);

        getMovieVideos();
        getMovieReviews();
        setupViewModel();
    }

    private void setupViewModel() {
//        FavMoviesModelView favMoviesModelView = ViewModelProviders.of(this).get(FavMoviesModelView.class);
//        favMoviesModelView.getFavMoviesList().observe(this, new Observer<List<FavMovieEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<FavMovieEntity> favMovieEntities) {
//            }
//        });

        if (mDb.favMovieDao().loadMovieById(movieId) != null) {
            fab.setImageResource(R.drawable.ic_star_fill_24dp);
            isFavourite = true;
        } else {
            fab.setImageResource(R.drawable.ic_star_empty_24dp);
            isFavourite = false;
        }
    }


    private void getMovieVideos() {
        apiService.getMovieVideos(movieId + "").enqueue(new Callback<MovieVideosEntity>() {
            @Override
            public void onResponse(Call<MovieVideosEntity> call, Response<MovieVideosEntity> response) {
                final MovieVideosEntity movieVideosEntity = response.body();
                mTrailersAdapter.addAll(movieVideosEntity.getTrailers());
            }

            @Override
            public void onFailure(Call<MovieVideosEntity> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(DetailsMovieActivity.this, "Error Loading Trailers", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getMovieReviews() {
        apiService.getMovieReviews(movieId + "").enqueue(new Callback<MovieReviewsEntity>() {
            @Override
            public void onResponse(Call<MovieReviewsEntity> call, Response<MovieReviewsEntity> response) {
                final MovieReviewsEntity movieReviewsEntity = response.body();
                mReviewsAdapter.addAll(movieReviewsEntity.getReviewList());
            }

            @Override
            public void onFailure(Call<MovieReviewsEntity> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(DetailsMovieActivity.this, "Error Loading Reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onFabClicked(View view) {
        if (isFavourite) {
            mDb.favMovieDao().deleteMovie(mDb.favMovieDao().loadMovieById(movieId));
            fab.setImageResource(R.drawable.ic_star_empty_24dp);
            isFavourite = false;

        } else {
            mDb.favMovieDao().insertMovie(new FavMovieEntity(movieId, movie.getOriginalTitle(), movie.getPosterPath(),movie.getReleaseDate(),String.valueOf(movie.getVoteAverage())));
            fab.setImageResource(R.drawable.ic_star_fill_24dp);
            isFavourite = true;
        }
    }
}
