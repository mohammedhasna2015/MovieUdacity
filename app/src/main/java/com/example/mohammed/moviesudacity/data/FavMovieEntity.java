package com.example.mohammed.moviesudacity.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movies")
public class FavMovieEntity {
    @PrimaryKey
    private int movieId;
    private String movieName;
    private String moviePoster;
    private String releaseDate;
    private String vote ;

    public FavMovieEntity(int movieId, String movieName, String moviePoster,String releaseDate,String vote) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.moviePoster = moviePoster;
        this.releaseDate=releaseDate;
        this.vote=vote;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    @Override
    public String toString() {
        return "FavMovieEntity{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", moviePoster='" + moviePoster + '\'' +
                '}';
    }
}
