package com.example.mohammed.moviesudacity.Activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mohammed.moviesudacity.Adabter.MovieAdabter;
import com.example.mohammed.moviesudacity.R;
import com.example.mohammed.moviesudacity.data.FavMovieEntity;
import com.example.mohammed.moviesudacity.data.FavMoviesModelView;
import com.example.mohammed.moviesudacity.model.Movie;
import com.example.mohammed.moviesudacity.model.MoviesList;
import com.example.mohammed.moviesudacity.network.APIClient;
import com.example.mohammed.moviesudacity.network.ApiRequests;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mohammed.moviesudacity.util.CONST.EXTRA_MOVIE;

public class MainActivity extends AppCompatActivity {


    private static final String KEY_MOVIE ="callBack" ;
    @BindView(R.id.rv_movies)
     RecyclerView mrecyclerView_movie;
    private MovieAdabter movieAdabter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ApiRequests apiService = APIClient.getClient().create(ApiRequests.class);
    ArrayList<Movie> popularMoviesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieAdabter = new MovieAdabter(MainActivity.this, new MovieAdabter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(MainActivity.this, DetailsMovieActivity.class);
                intent.putExtra(EXTRA_MOVIE, movie);
                startActivity(intent);
            }
        });
        mrecyclerView_movie.setHasFixedSize(true);
        mrecyclerView_movie.setAdapter(movieAdabter);
        mrecyclerView_movie.setLayoutManager(layoutManager);

          if (savedInstanceState!=null){
              if (savedInstanceState.containsKey(KEY_MOVIE)){
                  Log.d("KEY_MOVIE","data > "+
                          savedInstanceState.getParcelableArrayList(KEY_MOVIE).size());
                  popularMoviesList=savedInstanceState.getParcelableArrayList(KEY_MOVIE);
                  movieAdabter.addAll(popularMoviesList);
              }
          }else {
              getPopularMovies();
          }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_popular_movies:
                getPopularMovies();
                break;
            case R.id.action_top_rated:
                getTopRatedMovies();
                break;
            case R.id.action_favorites_movies:

                getFavoritesMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPopularMovies() {
        apiService.getPopularMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                final MoviesList moviesResponse = response.body();
                movieAdabter.clear();
                movieAdabter.addAll(moviesResponse.getMovieList());

                popularMoviesList = (ArrayList<Movie>) moviesResponse.getMovieList();




            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getTopRatedMovies() {
        apiService.getTopRatedMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                final MoviesList moviesResponse = response.body();
                movieAdabter.clear();
                movieAdabter.addAll(moviesResponse.getMovieList());
                popularMoviesList = (ArrayList<Movie>) moviesResponse.getMovieList();

            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public void getFavoritesMovies() {


        FavMoviesModelView favviewModal = ViewModelProviders.of(this).get(FavMoviesModelView.class);
        favviewModal.getFavMoviesList().observe(this, new Observer<List<FavMovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavMovieEntity> favMovieEntities) {
                movieAdabter.clear();
                movieAdabter.addAllFav(favMovieEntities);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(KEY_MOVIE, popularMoviesList);
        super.onSaveInstanceState(outState);
    }
}
