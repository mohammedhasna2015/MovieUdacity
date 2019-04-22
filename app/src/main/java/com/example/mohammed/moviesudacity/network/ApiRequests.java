package com.example.mohammed.moviesudacity.network;


import com.example.mohammed.moviesudacity.BuildConfig;
import com.example.mohammed.moviesudacity.model.MovieReviewsEntity;
import com.example.mohammed.moviesudacity.model.MovieVideosEntity;
import com.example.mohammed.moviesudacity.model.MoviesList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiRequests {
   String API_KEY = BuildConfig.API_KEY;

    @GET("movie/popular?api_key=" + API_KEY)
    Call<MoviesList> getPopularMovies(@Query("language") String language,
                                      @Query("page") String page,
                                      @Query("region") String region);

    @GET("movie/top_rated?api_key=" + API_KEY)
    Call<MoviesList> getTopRatedMovies(@Query("language") String language,
                                       @Query("page") String page,
                                       @Query("region") String region);

    @GET("movie/{id}/videos?api_key=" + API_KEY + "&language=en-US")
    Call<MovieVideosEntity> getMovieVideos(@Path("id") String movieId);

        @GET("movie/{id}/reviews?api_key=" + API_KEY + "&language=en-US")
    Call<MovieReviewsEntity> getMovieReviews(@Path("id") String movieId);

}
