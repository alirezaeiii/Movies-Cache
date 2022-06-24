package com.zattoo.movies.ui.home;

import com.zattoo.movies.data.home.Movie;

import java.util.List;

public interface Result {
    class Success implements Result {
        final List<Movie> movies;

        Success(List<Movie> movies) {
            this.movies = movies;
        }
    }

    class Failure implements Result {
        final String message;

        Failure(String message) {
            this.message = message;
        }
    }
}
